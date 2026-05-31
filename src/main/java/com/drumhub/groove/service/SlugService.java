package com.drumhub.groove.service;

import com.drumhub.groove.repository.GrooveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlugService {

    private final GrooveRepository grooveRepository;

    public String generateSlug(String title) {
        String base = normalize(title);
        if (!grooveRepository.existsBySlug(base)) {
            return base;
        }
        int suffix = 2;
        String candidate;
        do {
            candidate = base + "-" + suffix;
            suffix++;
        } while (grooveRepository.existsBySlug(candidate));
        return candidate;
    }

    private String normalize(String title) {
        String s = title.toLowerCase();
        // Replace accented characters
        s = s.replace('á', 'a').replace('à', 'a').replace('ä', 'a').replace('â', 'a')
             .replace('é', 'e').replace('è', 'e').replace('ë', 'e').replace('ê', 'e')
             .replace('í', 'i').replace('ì', 'i').replace('ï', 'i').replace('î', 'i')
             .replace('ó', 'o').replace('ò', 'o').replace('ö', 'o').replace('ô', 'o')
             .replace('ú', 'u').replace('ù', 'u').replace('ü', 'u').replace('û', 'u')
             .replace('ñ', 'n');
        // Replace non-alphanumeric characters with hyphens
        s = s.replaceAll("[^a-z0-9]+", "-");
        // Trim leading/trailing hyphens
        s = s.replaceAll("^-+|-+$", "");
        return s;
    }
}
