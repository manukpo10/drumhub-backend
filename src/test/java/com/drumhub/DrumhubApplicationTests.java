package com.drumhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class DrumhubApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring application context loads successfully
        // with the dev profile (H2 in-memory, Flyway with zero migrations).
    }
}
