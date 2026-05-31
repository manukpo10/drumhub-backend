package com.drumhub.export.service;

import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.export.domain.MidiNotes;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final GrooveRepository grooveRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // -------------------------------------------------------------------------
    // JSON — free for all authenticated users
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public byte[] exportJson(Long grooveId) {
        Groove groove = loadActiveGroove(grooveId);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id",             groove.getId());
        payload.put("slug",           groove.getSlug());
        payload.put("title",          groove.getTitle());
        payload.put("authorUsername", groove.getAuthor().getUsername());
        payload.put("genre",          groove.getGenre().getName());
        payload.put("bpm",            groove.getBpm());
        payload.put("level",          groove.getLevel());
        payload.put("timeSig",        groove.getTimeSig());
        payload.put("bars",           groove.getBars());
        payload.put("tags",           groove.getTags());
        payload.put("description",    groove.getDescription());
        payload.put("pattern",        groove.getPattern());

        try {
            return objectMapper.writeValueAsBytes(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize groove to JSON", e);
        }
    }

    // -------------------------------------------------------------------------
    // MIDI — Pro+ only
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public byte[] exportMidi(Long grooveId, String currentUsername) {
        Groove groove = loadActiveGroove(grooveId);
        requireProOrStudio(currentUsername, "MIDI");

        try {
            // 480 PPQ — standard for MIDI; 16th note = PPQ/4 = 120 ticks
            Sequence sequence = new Sequence(Sequence.PPQ, 480);
            Track track = sequence.createTrack();

            // Tempo meta-message (type 0x51): microseconds per beat
            int microsPerBeat = 60_000_000 / groove.getBpm();
            byte[] tempoData = {
                (byte) (microsPerBeat >> 16),
                (byte) (microsPerBeat >> 8),
                (byte)  microsPerBeat
            };
            track.add(new MidiEvent(new MetaMessage(0x51, tempoData, 3), 0));

            int ticksPerStep = 480 / 4; // 16th note grid
            Map<String, List<Integer>> pattern = groove.getPattern();

            for (Map.Entry<String, List<Integer>> entry : pattern.entrySet()) {
                int note   = MidiNotes.NOTES.getOrDefault(entry.getKey(), 38);
                List<Integer> steps = entry.getValue();
                for (int i = 0; i < steps.size(); i++) {
                    if (steps.get(i) == 1) {
                        long tick = (long) i * ticksPerStep;
                        ShortMessage noteOn  = new ShortMessage(ShortMessage.NOTE_ON,  9, note, 100);
                        ShortMessage noteOff = new ShortMessage(ShortMessage.NOTE_OFF, 9, note, 0);
                        track.add(new MidiEvent(noteOn,  tick));
                        track.add(new MidiEvent(noteOff, tick + 20));
                    }
                }
            }

            // End of track
            track.add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0),
                    sequence.getTickLength()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MidiSystem.write(sequence, 1, baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate MIDI file", e);
        }
    }

    // -------------------------------------------------------------------------
    // PDF — Pro+ only
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public byte[] exportPdf(Long grooveId, String currentUsername) {
        Groove groove = loadActiveGroove(grooveId);
        requireProOrStudio(currentUsername, "PDF");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin    = 50;
                float pageWidth = page.getMediaBox().getWidth();
                float y         = page.getMediaBox().getHeight() - margin;

                // Title
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                cs.beginText();
                cs.newLineAtOffset(margin, y);
                cs.showText(groove.getTitle());
                cs.endText();
                y -= 25;

                // Subtitle: author | genre | BPM | level
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                cs.beginText();
                cs.newLineAtOffset(margin, y);
                cs.showText(groove.getAuthor().getUsername() + "  |  " +
                            groove.getGenre().getName() + "  |  " +
                            groove.getBpm() + " BPM  |  " + groove.getLevel());
                cs.endText();
                y -= 30;

                // Pattern grid
                List<String> rowOrder = List.of(
                    "china", "crash", "splash", "ride", "ride_bell",
                    "hihat", "hihat_open", "snare", "tom1", "tom2", "tom3", "kick"
                );

                Map<String, List<Integer>> pattern = groove.getPattern();
                float labelWidth = 70;
                float cellSize   = (pageWidth - margin * 2 - labelWidth) / 16f;

                for (String instrument : rowOrder) {
                    List<Integer> steps = pattern.get(instrument);
                    if (steps == null || steps.isEmpty()) continue;

                    // Row label
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
                    cs.beginText();
                    cs.newLineAtOffset(margin, y + 3);
                    cs.showText(instrument);
                    cs.endText();

                    // 16 step cells
                    for (int i = 0; i < Math.min(steps.size(), 16); i++) {
                        float x = margin + labelWidth + i * cellSize;

                        // beat separator every 4 steps
                        if (i % 4 == 0) {
                            cs.setLineWidth(0.5f);
                            cs.moveTo(x, y - 2);
                            cs.lineTo(x, y + cellSize - 2);
                            cs.stroke();
                        }

                        if (steps.get(i) == 1) {
                            cs.addRect(x + 1, y, cellSize - 2, cellSize - 2);
                            cs.fill();
                        } else {
                            cs.setLineWidth(0.3f);
                            cs.addRect(x + 1, y, cellSize - 2, cellSize - 2);
                            cs.stroke();
                        }
                    }

                    y -= cellSize + 2;
                    if (y < margin + 50) break; // prevent page overflow
                }

                // Tags
                if (groove.getTags() != null && !groove.getTags().isEmpty()) {
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 9);
                    cs.beginText();
                    cs.newLineAtOffset(margin, margin + 20);
                    cs.showText("Tags: " + String.join(", ", groove.getTags()));
                    cs.endText();
                }

                // Footer
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
                cs.beginText();
                cs.newLineAtOffset(margin, margin);
                cs.showText("Generated by DrumHub — drumhub.app");
                cs.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF file", e);
        }
    }

    // -------------------------------------------------------------------------
    // MP3 — not yet implemented
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public byte[] exportMp3(Long grooveId) {
        throw new UnsupportedOperationException(
            "MP3 export is not yet implemented on the server. Use the browser player to record audio.");
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Groove loadActiveGroove(Long id) {
        return grooveRepository.findById(id)
            .filter(g -> g.isActivo())
            .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + id));
    }

    private void requireProOrStudio(String username, String format) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        if (user.getPlan() == Plan.FREE) {
            throw new ForbiddenException(
                "Export to " + format + " requires a Pro or Studio plan. Upgrade at /api/pricing/plans");
        }
    }
}
