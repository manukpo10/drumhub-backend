package com.drumhub.activity.web;

import com.drumhub.activity.dto.ActivityEventDto;
import com.drumhub.activity.service.ActivityService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ActivityController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void feed_withNoEvents_returns200AndEmptyList() throws Exception {
        when(activityService.getRecentFeed(anyInt(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/activity/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void feed_withEvents_returnsEventList() throws Exception {
        var event = new ActivityEventDto(
                "upload", "drummer1", "Drummer One",
                "Funk Groove", "funk-groove",
                null, null,
                Instant.now()
        );
        when(activityService.getRecentFeed(anyInt(), any())).thenReturn(List.of(event));

        mockMvc.perform(get("/api/activity/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].type").value("upload"))
                .andExpect(jsonPath("$.data[0].actor").value("drummer1"))
                .andExpect(jsonPath("$.data[0].grooveTitle").value("Funk Groove"));
    }

    @Test
    void feed_capsAtMaxSize50() throws Exception {
        when(activityService.getRecentFeed(anyInt(), any())).thenReturn(List.of());

        // size=999 should be capped to 50
        mockMvc.perform(get("/api/activity/feed").param("size", "999"))
                .andExpect(status().isOk());
    }
}
