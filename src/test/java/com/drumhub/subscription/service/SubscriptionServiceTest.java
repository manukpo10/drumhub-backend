package com.drumhub.subscription.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.payment.service.DollarRateService;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.dto.PricingTier;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DollarRateService dollarRateService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        // Default rate: 1000 for clean math (pro:monthly=5000, pro:annual=48000, studio=12000)
        org.mockito.Mockito.when(dollarRateService.currentMep()).thenReturn(1000.0);
    }

    private static User buildUser(String username, Plan plan) {
        User user = User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .build();
        user.setPlan(plan);
        return user;
    }

    @Test
    void getPlans_returnsThreePlans() {
        List<PlanResponse> plans = subscriptionService.getPlans();

        assertThat(plans).hasSize(3);
        assertThat(plans).extracting(PlanResponse::id)
                .containsExactly("free", "pro", "studio");
    }

    @Test
    void getCurrentPlan_whenUserNotFound_throwsResourceNotFoundException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscriptionService.getCurrentPlan("ghost"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void getCurrentPlan_returnsCorrectPlanWithFeatures() {
        User user = buildUser("drummer", Plan.PRO);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));

        CurrentPlanResponse response = subscriptionService.getCurrentPlan("drummer");

        assertThat(response.plan()).isEqualTo("pro");
        assertThat(response.username()).isEqualTo("drummer");
        assertThat(response.features().maxFavorites()).isEqualTo("unlimited");
        assertThat(response.features().maxGrooves()).isEqualTo("unlimited");
        assertThat(response.features().badge()).isEqualTo("PRO");
    }

    @Test
    void downgradePlan_setsUserPlanToFree() {
        User user = buildUser("drummer", Plan.PRO);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        CurrentPlanResponse response = subscriptionService.downgradePlan("drummer");

        assertThat(response.plan()).isEqualTo("free");
        assertThat(response.username()).isEqualTo("drummer");
        verify(userRepository).save(user);
    }

    @Test
    void activatePaidPlan_withValidPlan_updatesPlan() {
        User user = buildUser("drummer", Plan.FREE);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        CurrentPlanResponse response = subscriptionService.activatePaidPlan("drummer", "pro");

        assertThat(response.plan()).isEqualTo("pro");
        assertThat(response.features().badge()).isEqualTo("PRO");
        verify(userRepository).save(user);
    }

    @Test
    void activatePaidPlan_withFreePlan_throwsBadRequestException() {
        assertThatThrownBy(() -> subscriptionService.activatePaidPlan("drummer", "free"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid paid plan");
    }

    // --- 4-arg activatePaidPlan ---

    @Test
    void activatePaidPlan_4arg_setsPlanExpiresAtAndMpPaymentId() {
        User user = buildUser("drummer", Plan.FREE);
        Instant expiry = Instant.now().plus(30, ChronoUnit.DAYS);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        subscriptionService.activatePaidPlan("drummer", "pro", expiry, "mp-pay-123");

        assertThat(user.getPlan()).isEqualTo(Plan.PRO);
        assertThat(user.getPlanExpiresAt()).isEqualTo(expiry);
        assertThat(user.getMpPaymentId()).isEqualTo("mp-pay-123");
        verify(userRepository).save(user);
    }

    @Test
    void activatePaidPlan_4arg_clearTrialEndsAt() {
        User user = buildUser("drummer", Plan.PRO);
        user.setTrialEndsAt(Instant.now().plus(5, ChronoUnit.DAYS));
        Instant expiry = Instant.now().plus(30, ChronoUnit.DAYS);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        subscriptionService.activatePaidPlan("drummer", "pro", expiry, "mp-pay-456");

        assertThat(user.getTrialEndsAt()).isNull();
    }

    @Test
    void activatePaidPlan_2arg_delegatesTo4argWithNulls() {
        User user = buildUser("drummer", Plan.FREE);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        subscriptionService.activatePaidPlan("drummer", "pro");

        assertThat(user.getPlan()).isEqualTo(Plan.PRO);
        assertThat(user.getPlanExpiresAt()).isNull();
        assertThat(user.getMpPaymentId()).isNull();
    }

    // --- startTrial ---

    @Test
    void startTrial_setsProAndTrialEndsAt() {
        User user = buildUser("newbie", Plan.FREE);
        when(userRepository.findByUsername("newbie")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        subscriptionService.startTrial("newbie");

        assertThat(user.getPlan()).isEqualTo(Plan.PRO);
        assertThat(user.getTrialEndsAt()).isAfter(Instant.now());
        assertThat(user.getTrialEndsAt()).isBefore(Instant.now().plus(8, ChronoUnit.DAYS));
    }

    @Test
    void startTrial_throwsConflict_whenActivePaidPlan() {
        User user = buildUser("drummer", Plan.PRO);
        user.setPlanExpiresAt(Instant.now().plus(10, ChronoUnit.DAYS));
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> subscriptionService.startTrial("drummer"))
                .isInstanceOf(ConflictException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void startTrial_throwsConflict_whenActiveTrialRunning() {
        User user = buildUser("drummer", Plan.PRO);
        user.setTrialEndsAt(Instant.now().plus(5, ChronoUnit.DAYS));
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> subscriptionService.startTrial("drummer"))
                .isInstanceOf(ConflictException.class);

        verify(userRepository, never()).save(any());
    }

    // --- expireOverduePlans ---

    @Test
    void expireOverduePlans_callsRepositoryBulkUpdate() {
        when(userRepository.expireSubscriptions(any(Instant.class))).thenReturn(3);

        int count = subscriptionService.expireOverduePlans();

        assertThat(count).isEqualTo(3);
        verify(userRepository).expireSubscriptions(any(Instant.class));
    }

    // ── getPlans ARS enrichment ──────────────────────────────────────────────

    @Test
    void getPlans_withMep1000_enrichesArsFieldsCorrectly() {
        // dollarRateService.currentMep() returns 1000.0 from setUp()
        List<PlanResponse> plans = subscriptionService.getPlans();

        PlanResponse pro = plans.stream().filter(p -> "pro".equals(p.id())).findFirst().orElseThrow();
        PricingTier proTier = pro.pricing();
        assertThat(proTier.monthlyArs()).isEqualTo(5000);     // 5 * 1000
        assertThat(proTier.annualArs()).isEqualTo(48000);     // 48 * 1000
        assertThat(proTier.annualFullArs()).isEqualTo(60000); // 60 * 1000

        PlanResponse studio = plans.stream().filter(p -> "studio".equals(p.id())).findFirst().orElseThrow();
        PricingTier studioTier = studio.pricing();
        assertThat(studioTier.monthlyArs()).isEqualTo(12000); // 12 * 1000

        PlanResponse free = plans.stream().filter(p -> "free".equals(p.id())).findFirst().orElseThrow();
        PricingTier freeTier = free.pricing();
        assertThat(freeTier.monthlyArs()).isEqualTo(0);
        assertThat(freeTier.annualArs()).isEqualTo(0);
    }

    @Test
    void getPlans_withMep1000_usdFieldsUnchanged() {
        List<PlanResponse> plans = subscriptionService.getPlans();

        PlanResponse pro = plans.stream().filter(p -> "pro".equals(p.id())).findFirst().orElseThrow();
        assertThat(pro.pricing().monthly()).isEqualTo(5.0);
        assertThat(pro.pricing().annual()).isEqualTo(48.0);

        PlanResponse studio = plans.stream().filter(p -> "studio".equals(p.id())).findFirst().orElseThrow();
        assertThat(studio.pricing().monthly()).isEqualTo(12.0);
    }
}
