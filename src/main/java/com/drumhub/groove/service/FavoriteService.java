package com.drumhub.groove.service;

import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.groove.domain.Favorite;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.FavoriteResponse;
import com.drumhub.groove.dto.FavoriteStatusResponse;
import com.drumhub.groove.mapper.FavoriteMapper;
import com.drumhub.groove.repository.FavoriteRepository;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.subscription.domain.PlanLimits;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final GrooveRepository grooveRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

    @Transactional(readOnly = true)
    public Page<FavoriteResponse> getFavorites(String currentUsername, Pageable pageable) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        return favoriteRepository
                .findByUserIdAndActivoTrueOrderByCreatedAtDesc(user.getId(), pageable)
                .map(favoriteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public FavoriteStatusResponse getStatus(String currentUsername, Long grooveId) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        boolean favorited = favoriteRepository.existsByUserIdAndGrooveId(user.getId(), grooveId);
        long total = favoriteRepository.countByGrooveIdAndActivoTrue(grooveId);

        return new FavoriteStatusResponse(grooveId, favorited, total);
    }

    @Transactional
    public FavoriteStatusResponse addFavorite(String currentUsername, Long grooveId) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        if (user.getPlan() == Plan.FREE) {
            long favCount = favoriteRepository.countByUserUsernameAndActivoTrue(user.getUsername());
            if (favCount >= PlanLimits.FREE_MAX_FAVORITES) {
                throw new ForbiddenException("Free plan allows up to " + PlanLimits.FREE_MAX_FAVORITES + " favorites. Upgrade to Pro for unlimited.");
            }
        }

        Groove groove = grooveRepository.findById(grooveId)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + grooveId));

        if (favoriteRepository.existsByUserIdAndGrooveId(user.getId(), grooveId)) {
            throw new ConflictException("Already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .groove(groove)
                .build();

        favoriteRepository.save(favorite);

        long total = favoriteRepository.countByGrooveIdAndActivoTrue(grooveId);
        return new FavoriteStatusResponse(grooveId, true, total);
    }

    @Transactional
    public FavoriteStatusResponse removeFavorite(String currentUsername, Long grooveId) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        // Verify groove exists (active check optional for delete, but consistent)
        grooveRepository.findById(grooveId)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + grooveId));

        Favorite favorite = favoriteRepository.findByUserIdAndGrooveId(user.getId(), grooveId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favorite.setActivo(false);
        favoriteRepository.save(favorite);

        long total = favoriteRepository.countByGrooveIdAndActivoTrue(grooveId);
        return new FavoriteStatusResponse(grooveId, false, total);
    }
}
