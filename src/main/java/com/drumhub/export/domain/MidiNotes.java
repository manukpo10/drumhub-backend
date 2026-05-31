package com.drumhub.export.domain;

import java.util.Map;

/**
 * Maps drum instrument identifiers to General MIDI percussion note numbers.
 * Percussion channel 9 (0-indexed) uses note numbers to address specific sounds.
 */
public final class MidiNotes {

    private MidiNotes() {
        // utility class — no instances
    }

    public static final Map<String, Integer> NOTES = Map.ofEntries(
        Map.entry("kick",       36),
        Map.entry("snare",      38),
        Map.entry("hihat",      42),
        Map.entry("hihat_open", 46),
        Map.entry("crash",      49),
        Map.entry("ride",       51),
        Map.entry("ride_bell",  53),
        Map.entry("splash",     55),
        Map.entry("china",      52),
        Map.entry("tom1",       50),
        Map.entry("tom2",       47),
        Map.entry("tom3",       43),
        Map.entry("clap",       39),
        Map.entry("cowbell",    56),
        Map.entry("cabasa",     70),
        Map.entry("tamb",       54),
        Map.entry("conga",      63),
        Map.entry("stick",      37)
    );
}
