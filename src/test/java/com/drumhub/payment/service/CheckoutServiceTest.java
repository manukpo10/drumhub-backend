package com.drumhub.payment.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.payment.config.MercadoPagoProperties;
import com.drumhub.payment.dto.CheckoutRequest;
import com.drumhub.payment.dto.CheckoutResponse;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CheckoutServiceTest {

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @Mock
    private MercadoPagoProperties mpProperties;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DollarRateService dollarRateService;

    @InjectMocks
    private CheckoutService checkoutService;

    private static User buildAlice() {
        User user = User.builder()
                .username("alice")
                .name("Alice")
                .email("alice@test.com")
                .passwordHash("hash")
                .build();
        user.setPlan(Plan.FREE);
        return user;
    }

    @BeforeEach
    void setUp() {
        when(mpProperties.getAccessToken()).thenReturn("TEST-access-token");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(buildAlice()));
        // Rate 1000 → pro:monthly = 5*1000 = 5000, pro:annual = 48*1000 = 48000, studio:monthly = 12*1000 = 12000
        when(dollarRateService.currentMep()).thenReturn(1000.0);
    }

    @Test
    void checkout_withFreePlan_throwsBadRequest() {
        CheckoutRequest request = new CheckoutRequest("free", "monthly");

        assertThatThrownBy(() -> checkoutService.createCheckout(request, "alice"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("free");
    }

    @Test
    void checkout_withProMonthly_returnsCheckoutUrl() {
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=123");

        CheckoutResponse response = checkoutService.createCheckout(request, "alice");

        assertThat(response.checkoutUrl()).startsWith("https://");
    }

    @Test
    void checkout_buildsCorrectExternalReference() {
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertThat(payload.get("external_reference")).isEqualTo("alice:pro:monthly");
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_buildsItemsWithCorrectPriceAndName() {
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        Map<String, Object> payload = captor.getValue();
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");
        assertThat(items).hasSize(1);
        Map<String, Object> item = items.get(0);
        assertThat(item.get("unit_price")).isEqualTo(5000);
        assertThat(item.get("title").toString()).containsIgnoringCase("pro");
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_annualPro_usesAnnualPrice() {
        CheckoutRequest request = new CheckoutRequest("pro", "annual");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        List<Map<String, Object>> items = (List<Map<String, Object>>) captor.getValue().get("items");
        assertThat(items.get(0).get("unit_price")).isEqualTo(48000);
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_studioMonthly_usesStudioPrice() {
        CheckoutRequest request = new CheckoutRequest("studio", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        List<Map<String, Object>> items = (List<Map<String, Object>>) captor.getValue().get("items");
        assertThat(items.get(0).get("unit_price")).isEqualTo(12000);
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_includesPayerEmailInPayload() {
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        Map<String, Object> payload = captor.getValue();
        Map<String, String> payer = (Map<String, String>) payload.get("payer");
        assertThat(payer).isNotNull();
        assertThat(payer.get("email")).isEqualTo("alice@test.com");
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_includesBackUrlsAndAutoReturn() {
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertThat(payload.get("auto_return")).isEqualTo("approved");
        assertThat(payload.get("back_urls")).isNotNull();

        Map<String, String> backUrls = (Map<String, String>) payload.get("back_urls");
        assertThat(backUrls).containsKeys("success", "failure", "pending");
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkout_withRate1460_proMonthlyUnitPriceIs7300() {
        when(dollarRateService.currentMep()).thenReturn(1460.0);
        CheckoutRequest request = new CheckoutRequest("pro", "monthly");
        when(mercadoPagoClient.createPreference(anyString(), any()))
                .thenReturn("https://mp.com/checkout");

        checkoutService.createCheckout(request, "alice");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(mercadoPagoClient).createPreference(anyString(), captor.capture());

        List<Map<String, Object>> items = (List<Map<String, Object>>) captor.getValue().get("items");
        // 5 USD × 1460 → round(5*1460/100)*100 = round(73)*100 = 7300
        assertThat(items.get(0).get("unit_price")).isEqualTo(7300);
    }

    @Test
    void checkout_withInvalidPeriod_throwsBadRequestContainingPeriod() {
        CheckoutRequest request = new CheckoutRequest("pro", "weekly");

        assertThatThrownBy(() -> checkoutService.createCheckout(request, "alice"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("period");
    }
}
