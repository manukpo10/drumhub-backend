package com.drumhub.user.mapper;

import com.drumhub.user.domain.Follow;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.dto.UserSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowMapper {

    /** Maps the follower side — used when listing followers of a user (who follows this user). */
    @Mapping(source = "follower.id",         target = "id")
    @Mapping(source = "follower.username",   target = "username")
    @Mapping(source = "follower.name",       target = "name")
    @Mapping(source = "follower.avatarSeed", target = "avatarSeed")
    @Mapping(source = "follower.color",      target = "color")
    @Mapping(source = "follower.init",       target = "init")
    @Mapping(source = "follower.plan",       target = "plan", qualifiedByName = "planToString")
    @Mapping(target = "followerCount",       constant = "0L")
    @Mapping(target = "followingCount",      constant = "0L")
    UserSummaryResponse followerToSummary(Follow follow);

    /** Maps the followee side — used when listing who a user follows. */
    @Mapping(source = "followee.id",         target = "id")
    @Mapping(source = "followee.username",   target = "username")
    @Mapping(source = "followee.name",       target = "name")
    @Mapping(source = "followee.avatarSeed", target = "avatarSeed")
    @Mapping(source = "followee.color",      target = "color")
    @Mapping(source = "followee.init",       target = "init")
    @Mapping(source = "followee.plan",       target = "plan", qualifiedByName = "planToString")
    @Mapping(target = "followerCount",       constant = "0L")
    @Mapping(target = "followingCount",      constant = "0L")
    UserSummaryResponse followeeToSummary(Follow follow);

    @Named("planToString")
    default String planToString(Plan plan) {
        return plan != null ? plan.name().toLowerCase() : "free";
    }
}
