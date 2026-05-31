package com.drumhub.groove.mapper;

import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.GrooveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrooveMapper {

    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "author.name",     target = "authorName")
    @Mapping(source = "genre.name",      target = "genre")
    @Mapping(source = "genre.slug",      target = "genreSlug")
    GrooveResponse toResponse(Groove groove);

    List<GrooveResponse> toResponseList(List<Groove> grooves);
}
