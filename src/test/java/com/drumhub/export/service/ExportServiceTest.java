package com.drumhub.export.service;

import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock private GrooveRepository grooveRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private ExportService exportService;

    @BeforeEach
    void setUp() {
        // Inject a real ObjectMapper (Spring is not running here)
        exportService = new ExportService(grooveRepository, userRepository, new ObjectMapper());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static Genre buildGenre() {
        return Genre.builder().name("Funk").slug("funk").build();
    }

    private static User buildUser(String username, Plan plan) {
        return User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .plan(plan)
                .build();
    }

    private static Groove buildGroove(User author) {
        return Groove.builder()
                .slug("purdie-shuffle")
                .title("Purdie Shuffle")
                .author(author)
                .genre(buildGenre())
                .bpm(98)
                .level("Avanzado")
                .timeSig("4/4")
                .bars(1)
                .tags(List.of("shuffle", "funk"))
                .description("The classic shuffle groove")
                .pattern(Map.of(
                    "kick",  List.of(1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0),
                    "snare", List.of(0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0),
                    "hihat", List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
                ))
                .build();
    }

    // -------------------------------------------------------------------------
    // exportJson
    // -------------------------------------------------------------------------

    @Test
    void exportJson_whenGrooveNotFound_throwsResourceNotFoundException() {
        when(grooveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> exportService.exportJson(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void exportJson_whenValid_returnsByteArray() throws Exception {
        User author = buildUser("bernardp", Plan.FREE);
        Groove groove = buildGroove(author);
        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));

        byte[] result = exportService.exportJson(1L);

        assertThat(result).isNotEmpty();
        // Verify it's valid JSON containing expected fields
        ObjectMapper om = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = om.readValue(result, Map.class);
        assertThat(parsed).containsKey("slug");
        assertThat(parsed.get("slug")).isEqualTo("purdie-shuffle");
        assertThat(parsed).containsKey("bpm");
        assertThat(parsed.get("bpm")).isEqualTo(98);
        assertThat(parsed).containsKey("pattern");
    }

    // -------------------------------------------------------------------------
    // exportMidi
    // -------------------------------------------------------------------------

    @Test
    void exportMidi_whenFreeUser_throwsForbiddenException() {
        User author   = buildUser("bernardp", Plan.FREE);
        Groove groove = buildGroove(author);

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(author));

        assertThatThrownBy(() -> exportService.exportMidi(1L, "bernardp"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("MIDI")
                .hasMessageContaining("/api/pricing/plans");
    }

    @Test
    void exportMidi_whenProUser_returnsMidiBytes() {
        User proUser  = buildUser("funkybones", Plan.PRO);
        Groove groove = buildGroove(proUser);

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));
        when(userRepository.findByUsername("funkybones")).thenReturn(Optional.of(proUser));

        byte[] result = exportService.exportMidi(1L, "funkybones");

        assertThat(result).isNotEmpty();
        // MIDI files start with "MThd" header
        assertThat(new String(result, 0, 4)).isEqualTo("MThd");
    }

    // -------------------------------------------------------------------------
    // exportPdf
    // -------------------------------------------------------------------------

    @Test
    void exportPdf_whenFreeUser_throwsForbiddenException() {
        User freeUser = buildUser("bernardp", Plan.FREE);
        Groove groove = buildGroove(freeUser);

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(freeUser));

        assertThatThrownBy(() -> exportService.exportPdf(1L, "bernardp"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("PDF");
    }

    // -------------------------------------------------------------------------
    // exportMp3
    // -------------------------------------------------------------------------

    @Test
    void exportMp3_throwsUnsupportedOperationException() {
        assertThatThrownBy(() -> exportService.exportMp3(1L))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("MP3");
    }
}
