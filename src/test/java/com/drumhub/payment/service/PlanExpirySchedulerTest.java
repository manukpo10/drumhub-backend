package com.drumhub.payment.service;

import com.drumhub.subscription.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanExpirySchedulerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private PlanExpiryScheduler planExpiryScheduler;

    @Test
    void expireOverduePlans_callsSubscriptionServiceOnce() {
        when(subscriptionService.expireOverduePlans()).thenReturn(5);

        planExpiryScheduler.expireOverduePlans();

        verify(subscriptionService, times(1)).expireOverduePlans();
    }

    @Test
    void expireOverduePlans_whenNoExpiredPlans_stillCallsService() {
        when(subscriptionService.expireOverduePlans()).thenReturn(0);

        planExpiryScheduler.expireOverduePlans();

        verify(subscriptionService, times(1)).expireOverduePlans();
    }
}
