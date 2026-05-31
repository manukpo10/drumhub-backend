package com.drumhub.subscription.domain;

public final class PlanLimits {

    private PlanLimits() {}

    public static final int FREE_MAX_FAVORITES = 20;
    public static final int FREE_MAX_GROOVES   = 5;
    public static final int PRO_MAX_FAVORITES  = Integer.MAX_VALUE;
    public static final int PRO_MAX_GROOVES    = Integer.MAX_VALUE;
}
