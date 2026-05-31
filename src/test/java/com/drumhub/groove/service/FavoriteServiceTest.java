package com.drumhub.groove.service;

import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.groove.domain.Favorite;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.FavoriteStatusResponse;
import com.drumhub.groove.mapper.FavoriteMapper;
import com.drumhub.groove.repository.FavoriteRepository;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.subscription.domain.PlanLimits;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock private FavoriteRepository favoriteRepository;
    @Mock private GrooveRepository grooveRepository;
    @Mock private UserRepository userRepository;
    @Mock private FavoriteMapper favoriteMapper;

    @InjectMocks
    private FavoriteService favoriteService;

    private static User buildUser(String username, Long id) {
        User user = User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .build();
        user.setId(id);
        return user;
    }

    private static Groove buildActiveGroove(Long id) {
        Groove groove = Groove.builder()
                .slug("test-groove")
                .title("Test Groove")
                .author(buildUser("author", 100L))
                .bpm(120)
                .level("Básico")
                .build();
        groove.setId(id);
        groove.setActivo(true);
        return groove;
    }

    @Test
    void addFavorite_whenAlreadyFavorited_throwsConflictException() {
        User user = buildUser("drummer", 1L);
        Groove groove = buildActiveGroove(10L);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(grooveRepository.findById(10L)).thenReturn(Optional.of(groove));
        when(favoriteRepository.existsByUserIdAndGrooveId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> favoriteService.addFavorite("drummer", 10L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Already in favorites");
    }

    @Test
    void addFavorite_whenValid_savesAndReturnsFavorited() {
        User user = buildUser("drummer", 1L);
        Groove groove = buildActiveGroove(10L);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(grooveRepository.findById(10L)).thenReturn(Optional.of(groove));
        when(favoriteRepository.existsByUserIdAndGrooveId(1L, 10L)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(inv -> inv.getArgument(0));
        when(favoriteRepository.countByGrooveIdAndActivoTrue(10L)).thenReturn(1L);

        FavoriteStatusResponse result = favoriteService.addFavorite("drummer", 10L);

        assertThat(result.favorited()).isTrue();
        assertThat(result.totalFavorites()).isEqualTo(1L);
        assertThat(result.grooveId()).isEqualTo(10L);
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_whenFreeUserExceedsLimit_throwsForbiddenException() {
        User user = buildUser("freeDrummer", 1L);
        user.setPlan(Plan.FREE);

        when(userRepository.findByUsername("freeDrummer")).thenReturn(Optional.of(user));
        when(favoriteRepository.countByUserUsernameAndActivoTrue("freeDrummer"))
                .thenReturn((long) PlanLimits.FREE_MAX_FAVORITES);

        assertThatThrownBy(() -> favoriteService.addFavorite("freeDrummer", 10L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Free plan allows up to");
    }

    @Test
    void removeFavorite_whenNotFavorited_throwsResourceNotFoundException() {
        User user = buildUser("drummer", 1L);
        Groove groove = buildActiveGroove(10L);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(user));
        when(grooveRepository.findById(10L)).thenReturn(Optional.of(groove));
        when(favoriteRepository.findByUserIdAndGrooveId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.removeFavorite("drummer", 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Favorite not found");
    }
}
