package com.drumhub.groove.mapper;

import com.drumhub.groove.domain.Comment;
import com.drumhub.groove.dto.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "author.username",   target = "authorUsername")
    @Mapping(source = "author.name",       target = "authorName")
    @Mapping(source = "author.avatarSeed", target = "authorAvatarSeed")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
