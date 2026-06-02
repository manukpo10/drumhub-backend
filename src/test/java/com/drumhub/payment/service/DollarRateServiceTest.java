package com.drumhub.payment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DollarRateServiceTest {

    private static final double FALLBACK = 1400.0;

    @Mock
    private DollarRateProvider provider;

    // ── Happy path ──────────────────────────────────────────────────────────────

    @Test
    void currentMep_whenProviderReturns1500_returnsAndCaches() {
        when(provider.fetchMep()).thenReturn(Optional.of(1500.0));
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        double rate = service.currentMep();

        assertThat(rate).isEqualTo(1500.0);
        verify(provider, times(1)).fetchMep();
    }

    @Test
    void currentMep_withinTtl_returnsCachedWithoutCallingProvider() {
        when(provider.fetchMep()).thenReturn(Optional.of(1500.0));
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        service.currentMep(); // populates cache at t=0
        now.set(DollarRateService.TTL_MS - 1); // still within TTL
        double second = service.currentMep();

        assertThat(second).isEqualTo(1500.0);
        verify(provider, times(1)).fetchMep(); // provider called only once
    }

    @Test
    void currentMep_afterTtlExpiry_callsProviderAgain() {
        when(provider.fetchMep()).thenReturn(Optional.of(1500.0));
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        service.currentMep(); // first fetch at t=0
        now.set(DollarRateService.TTL_MS + 1); // TTL expired
        service.currentMep(); // should trigger second fetch

        verify(provider, times(2)).fetchMep();
    }

    // ── Failure / fallback paths ─────────────────────────────────────────────────

    @Test
    void currentMep_whenFetchFailsAndHasCache_returnsStaleCache() {
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        // First call succeeds → populates cache
        when(provider.fetchMep()).thenReturn(Optional.of(1500.0));
        service.currentMep();

        // TTL expires, next fetch fails
        now.set(DollarRateService.TTL_MS + 1);
        when(provider.fetchMep()).thenReturn(Optional.empty());
        double rate = service.currentMep();

        assertThat(rate).isEqualTo(1500.0); // stale cache returned
    }

    @Test
    void currentMep_whenFetchFailsAndNoCache_returnsFallback() {
        when(provider.fetchMep()).thenReturn(Optional.empty());
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        double rate = service.currentMep();

        assertThat(rate).isEqualTo(FALLBACK);
    }

    @Test
    void currentMep_whenProviderReturnsZero_treatsAsFailure_returnsFallback() {
        when(provider.fetchMep()).thenReturn(Optional.of(0.0));
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        double rate = service.currentMep();

        assertThat(rate).isEqualTo(FALLBACK);
    }

    @Test
    void currentMep_whenProviderReturnsNegative_treatsAsFailure_returnsFallback() {
        when(provider.fetchMep()).thenReturn(Optional.of(-100.0));
        AtomicLong now = new AtomicLong(0);
        DollarRateService service = new DollarRateService(provider, FALLBACK, now::get);

        double rate = service.currentMep();

        assertThat(rate).isEqualTo(FALLBACK);
    }
}
