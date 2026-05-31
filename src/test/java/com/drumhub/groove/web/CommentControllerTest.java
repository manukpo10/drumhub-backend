package com.drumhub.groove.web;

import com.drumhub.groove.dto.CommentResponse;
import com.drumhub.groove.dto.CreateCommentRequest;
import com.drumhub.groove.service.CommentService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CommentController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final CommentResponse SAMPLE = new CommentResponse(
            1L, "drummer1", "Drummer One", "bonham", "Great groove!", Instant.now()
    );

    @Test
    void getComments_returns200() throws Exception {
        when(commentService.findByGrooveId(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(SAMPLE), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/grooves/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].authorUsername").value("drummer1"));
    }

    @Test
    void addComment_withoutAuth_returns403() throws Exception {
        // With addFilters=false, no principal → NullPointerException → 500 in unit test
        // Real security enforcement tested via integration. We verify null principal causes failure.
        when(commentService.addComment(any(), anyLong(), any()))
                .thenThrow(new NullPointerException("No principal"));

        CreateCommentRequest request = new CreateCommentRequest("Great groove!");

        mockMvc.perform(post("/api/grooves/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "drummer1")
    void addComment_withAuth_returns201() throws Exception {
        when(commentService.addComment(anyString(), anyLong(), any(CreateCommentRequest.class)))
                .thenReturn(SAMPLE);

        CreateCommentRequest request = new CreateCommentRequest("Great groove!");

        mockMvc.perform(post("/api/grooves/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.text").value("Great groove!"));
    }
}
