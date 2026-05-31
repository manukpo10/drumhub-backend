package com.drumhub.groove.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.groove.domain.Comment;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.CommentResponse;
import com.drumhub.groove.dto.CreateCommentRequest;
import com.drumhub.groove.mapper.CommentMapper;
import com.drumhub.groove.repository.CommentRepository;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.notification.domain.NotificationType;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.service.NotificationService;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final GrooveRepository grooveRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Lazy
    @Autowired
    private NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<CommentResponse> findByGrooveId(Long grooveId, Pageable pageable) {
        grooveRepository.findById(grooveId)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + grooveId));

        return commentRepository
                .findByGrooveIdAndActivoTrueOrderByCreatedAtDesc(grooveId, pageable)
                .map(commentMapper::toResponse);
    }

    @Transactional
    public CommentResponse addComment(String currentUsername, Long grooveId, CreateCommentRequest request) {
        Groove groove = grooveRepository.findById(grooveId)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + grooveId));

        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        Comment comment = Comment.builder()
                .groove(groove)
                .author(author)
                .text(request.text())
                .build();

        CommentResponse response = commentMapper.toResponse(commentRepository.save(comment));

        if (!author.getUsername().equals(groove.getAuthor().getUsername())) {
            String text = request.text();
            String snippet = text.length() > 100 ? text.substring(0, 97) + "..." : text;
            notificationService.push(new CreateNotificationRequest(
                    groove.getAuthor().getUsername(),
                    NotificationType.COMMENT,
                    author.getUsername(),
                    groove.getSlug(),
                    groove.getTitle(),
                    snippet
            ));
        }

        return response;
    }
}
