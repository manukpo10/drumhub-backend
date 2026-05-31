package com.drumhub.payment.service;

import com.drumhub.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanExpiryScheduler {

    private final SubscriptionService subscriptionService;

    /**
     * Runs daily at 03:00 UTC to downgrade users with expired paid plans or trials.
     * Cron: second minute hour day-of-month month day-of-week
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void expireOverduePlans() {
        log.info("Running plan expiry scheduler...");
        int expired = subscriptionService.expireOverduePlans();
        log.info("Plan expiry scheduler complete: {} subscription(s) expired.", expired);
    }
}
