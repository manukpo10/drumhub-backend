package com.drumhub.groove.mapper;

import com.drumhub.groove.domain.Favorite;
import com.drumhub.groove.dto.FavoriteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoriteMapper {

    @Mapping(source = "groove.id",              target = "grooveId")
    @Mapping(source = "groove.slug",            target = "grooveSlug")
    @Mapping(source = "groove.title",           target = "grooveTitle")
    @Mapping(source = "groove.author.username", target = "authorUsername")
    @Mapping(source = "groove.genre.name",      target = "genre")
    @Mapping(source = "groove.bpm",             target = "bpm")
    @Mapping(source = "groove.level",           target = "level")
    @Mapping(source = "createdAt",              target = "favoritedAt")
    FavoriteResponse toResponse(Favorite favorite);
}
