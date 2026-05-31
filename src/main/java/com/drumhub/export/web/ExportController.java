package com.drumhub.export.web;

import com.drumhub.export.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grooves")
@RequiredArgsConstructor
@Tag(name = "Export")
@SecurityRequirement(name = "bearerAuth")
public class ExportController {

    private final ExportService exportService;

    @GetMapping(value = "/{id}/export/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Export groove as JSON (Free)")
    public ResponseEntity<byte[]> exportJson(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        byte[] data = exportService.exportJson(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"groove-" + id + ".json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @GetMapping(value = "/{id}/export/midi", produces = "audio/midi")
    @Operation(summary = "Export groove as MIDI file (Pro+)")
    public ResponseEntity<byte[]> exportMidi(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        byte[] data = exportService.exportMidi(id, userDetails.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"groove-" + id + ".mid\"")
                .contentType(MediaType.parseMediaType("audio/midi"))
                .body(data);
    }

    @GetMapping(value = "/{id}/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Export groove as PDF (Pro+)")
    public ResponseEntity<byte[]> exportPdf(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        byte[] data = exportService.exportPdf(id, userDetails.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"groove-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    @GetMapping(value = "/{id}/export/mp3", produces = "audio/mpeg")
    @Operation(summary = "Export groove as MP3 (Pro+) — not yet implemented")
    public ResponseEntity<byte[]> exportMp3(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        exportService.exportMp3(id);
        return ResponseEntity.ok().build(); // never reached
    }
}
