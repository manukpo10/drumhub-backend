package com.drumhub.subscription.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.dto.UpdatePlanRequest;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

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
    void updatePlan_whenInvalidPlan_throwsBadRequestException() {
        UpdatePlanRequest request = new UpdatePlanRequest("premium");

        assertThatThrownBy(() -> subscriptionService.updatePlan("drummer", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid plan");
    }

    @Test
    void updatePlan_whenValid_updatesUserPlan() {
        User user = buildUser("drummer", Plan.FREE);
        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UpdatePlanRequest request = new UpdatePlanRequest("pro");
        CurrentPlanResponse response = subscriptionService.updatePlan("drummer", request);

        assertThat(response.plan()).isEqualTo("pro");
        assertThat(response.username()).isEqualTo("drummer");
        assertThat(response.features().badge()).isEqualTo("PRO");
        verify(userRepository).save(user);
    }
}
