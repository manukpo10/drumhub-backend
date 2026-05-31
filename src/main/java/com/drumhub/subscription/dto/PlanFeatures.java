package com.drumhub.subscription.dto;

public record PlanFeatures(
        String maxFavorites,
        String maxGrooves,
        boolean jsonExport,
        boolean midiPdfMp3Export,
        boolean grooveStats,
        String badge,
        boolean privateGrooves,
        boolean publicApiAccess,
        String collaborativeAccounts,
        boolean prioritySupport
) {}
