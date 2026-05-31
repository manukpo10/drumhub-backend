package com.drumhub.user.mapper;

import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "plan", target = "plan", qualifiedByName = "planToString")
    UserResponse toResponse(User user);

    @Named("planToString")
    default String planToString(Plan plan) {
        return plan != null ? plan.name().toLowerCase() : "free";
    }
}
