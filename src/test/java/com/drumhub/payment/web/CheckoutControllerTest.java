package com.drumhub.payment.web;

import com.drumhub.payment.dto.CheckoutRequest;
import com.drumhub.payment.dto.CheckoutResponse;
import com.drumhub.payment.service.CheckoutService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CheckoutController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CheckoutService checkoutService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @WithMockUser(username = "drummer")
    void checkout_withValidPlan_returns200WithCheckoutUrl() throws Exception {
        CheckoutResponse checkoutResponse = new CheckoutResponse("https://mp.com/checkout/123");
        when(checkoutService.createCheckout(any(CheckoutRequest.class), eq("drummer")))
                .thenReturn(checkoutResponse);

        CheckoutRequest request = new CheckoutRequest("pro", "monthly");

        mockMvc.perform(post("/api/payments/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.checkoutUrl").value("https://mp.com/checkout/123"));
    }

    @Test
    @WithMockUser(username = "drummer")
    void checkout_withFreePlan_returns400() throws Exception {
        when(checkoutService.createCheckout(any(CheckoutRequest.class), any()))
                .thenThrow(new com.drumhub.common.exception.BadRequestException("Cannot checkout with plan: free"));

        CheckoutRequest request = new CheckoutRequest("free", "monthly");

        mockMvc.perform(post("/api/payments/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
