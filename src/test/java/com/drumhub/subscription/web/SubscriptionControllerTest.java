package com.drumhub.subscription.web;

import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanFeatures;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.dto.PricingTier;
import com.drumhub.subscription.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = SubscriptionController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final PlanFeatures FREE_FEATURES = new PlanFeatures(
            "20", "5", true, false, false, "none", false, false, "no", false
    );

    private static final PlanFeatures PRO_FEATURES = new PlanFeatures(
            "unlimited", "unlimited", true, true, true, "PRO", false, false, "no", false
    );

    @Test
    void getPlans_isPublic_returns200() throws Exception {
        List<PlanResponse> plans = List.of(
                new PlanResponse("free", "Free", new PricingTier(0, 0, 0), FREE_FEATURES),
                new PlanResponse("pro", "Pro", new PricingTier(5, 48, 60), PRO_FEATURES)
        );
        when(subscriptionService.getPlans()).thenReturn(plans);

        mockMvc.perform(get("/api/pricing/plans").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value("free"))
                .andExpect(jsonPath("$.data[1].id").value("pro"));
    }

    @Test
    void getCurrentPlan_withoutAuth_returns500() throws Exception {
        when(subscriptionService.getCurrentPlan(null))
                .thenThrow(new NullPointerException("No principal"));

        mockMvc.perform(get("/api/users/me/plan").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "drummer")
    void getCurrentPlan_withAuth_returns200() throws Exception {
        CurrentPlanResponse response = new CurrentPlanResponse("free", "drummer", FREE_FEATURES);
        when(subscriptionService.getCurrentPlan("drummer")).thenReturn(response);

        mockMvc.perform(get("/api/users/me/plan").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.plan").value("free"))
                .andExpect(jsonPath("$.data.username").value("drummer"));
    }

    @Test
    @WithMockUser(username = "drummer")
    void cancelPlan_withAuth_downgradestoFree() throws Exception {
        CurrentPlanResponse response = new CurrentPlanResponse("free", "drummer", FREE_FEATURES);
        when(subscriptionService.downgradePlan(anyString())).thenReturn(response);

        mockMvc.perform(delete("/api/users/me/plan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.plan").value("free"));
    }
}
