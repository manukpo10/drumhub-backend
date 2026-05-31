package com.drumhub.payment.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.payment.dto.CheckoutRequest;
import com.drumhub.payment.dto.CheckoutResponse;
import com.drumhub.payment.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Mercado Pago Checkout Pro integration")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    @Operation(summary = "Create a Mercado Pago Checkout Pro preference")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CheckoutResponse>> createCheckout(
            @Valid @RequestBody CheckoutRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        CheckoutResponse response = checkoutService.createCheckout(request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
