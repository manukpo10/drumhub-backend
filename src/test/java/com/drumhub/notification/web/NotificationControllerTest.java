package com.drumhub.notification.web;

import com.drumhub.notification.dto.NotificationResponse;
import com.drumhub.notification.dto.UnreadCountResponse;
import com.drumhub.notification.service.NotificationService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = NotificationController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final NotificationResponse SAMPLE = new NotificationResponse(
            1L, "follow", "drummer", false, null, null, null, Instant.now()
    );

    @Test
    void getNotifications_withoutAuth_returns403() throws Exception {
        when(notificationService.getNotifications(any(), any()))
                .thenThrow(new NullPointerException("No principal"));

        mockMvc.perform(get("/api/notifications").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "bernardp")
    void getNotifications_withAuth_returns200() throws Exception {
        when(notificationService.getNotifications(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(SAMPLE), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/notifications").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].type").value("follow"));
    }

    @Test
    @WithMockUser(username = "bernardp")
    void getUnreadCount_withAuth_returns200() throws Exception {
        when(notificationService.getUnreadCount(anyString()))
                .thenReturn(new UnreadCountResponse(3L));

        mockMvc.perform(get("/api/notifications/unread-count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.unreadCount").value(3));
    }

    @Test
    @WithMockUser(username = "bernardp")
    void markAllRead_withAuth_returns204() throws Exception {
        doNothing().when(notificationService).markAllRead(anyString());

        mockMvc.perform(put("/api/notifications/read-all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
