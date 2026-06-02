package com.drumhub.payment.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanPricesTest {

    @Test
    void toArs_proMonthly_rate1000() {
        assertThat(PlanPrices.toArs(5, 1000)).isEqualTo(5000);
    }

    @Test
    void toArs_proMonthly_rate1460() {
        assertThat(PlanPrices.toArs(5, 1460)).isEqualTo(7300);
    }

    @Test
    void toArs_proAnnual_rate1000() {
        assertThat(PlanPrices.toArs(48, 1000)).isEqualTo(48000);
    }

    @Test
    void toArs_studioMonthly_rate1000() {
        assertThat(PlanPrices.toArs(12, 1000)).isEqualTo(12000);
    }

    @Test
    void toArs_free_isAlwaysZero() {
        assertThat(PlanPrices.toArs(0, 1460)).isEqualTo(0);
    }
}
