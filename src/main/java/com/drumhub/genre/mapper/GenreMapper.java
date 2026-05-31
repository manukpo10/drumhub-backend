package com.drumhub.genre.mapper;

import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper {

    @Mapping(target = "grooveCount", constant = "0L")
    GenreResponse toResponse(Genre genre);

    @Mapping(target = "grooveCount", constant = "0L")
    GenreDetailResponse toDetailResponse(Genre genre);

    List<GenreResponse> toResponseList(List<Genre> genres);
}
