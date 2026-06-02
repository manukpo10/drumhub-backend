package com.drumhub.activity.web;

import com.drumhub.activity.dto.ActivityEventDto;
import com.drumhub.activity.service.ActivityService;
import com.drumhub.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@Tag(name = "Activity", description = "Community activity feed")
public class ActivityController {

    private static final int MAX_SIZE = 50;

    private final ActivityService activityService;

    @GetMapping("/feed")
    @Operation(summary = "Recent community activity: uploads, comments, likes and follows")
    public ApiResponse<List<ActivityEventDto>> getFeed(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String user) {
        return ApiResponse.ok(activityService.getRecentFeed(Math.min(size, MAX_SIZE), user));
    }
}
