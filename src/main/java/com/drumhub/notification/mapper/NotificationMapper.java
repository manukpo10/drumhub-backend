package com.drumhub.notification.mapper;

import com.drumhub.notification.domain.Notification;
import com.drumhub.notification.domain.NotificationType;
import com.drumhub.notification.dto.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(source = "type", target = "type", qualifiedByName = "typeToString")
    NotificationResponse toResponse(Notification notification);

    List<NotificationResponse> toResponseList(List<Notification> notifications);

    @Named("typeToString")
    default String typeToString(NotificationType type) {
        return type != null ? type.name().toLowerCase() : null;
    }
}
