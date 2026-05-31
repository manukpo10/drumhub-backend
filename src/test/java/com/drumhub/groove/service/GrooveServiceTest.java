package com.drumhub.groove.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.repository.GenreRepository;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.CreateGrooveRequest;
import com.drumhub.groove.dto.GrooveResponse;
import com.drumhub.groove.mapper.GrooveMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrooveServiceTest {

    @Mock private GrooveRepository grooveRepository;
    @Mock private GenreRepository genreRepository;
    @Mock private UserRepository userRepository;
    @Mock private GrooveMapper grooveMapper;
    @Mock private SlugService slugService;

    @InjectMocks
    private GrooveService grooveService;

    private static User buildUser(String username) {
        return User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .build();
    }

    private static Genre buildGenre(String slug) {
        return Genre.builder()
                .name("Funk")
                .slug(slug)
                .icon("🕺")
                .color("#ff6b00")
                .bpmMin(80)
                .bpmMax(120)
                .level("Avanzado")
                .timeSig("4/4")
                .build();
    }

    private static Groove buildGroove(String slug, User author, Genre genre) {
        return Groove.builder()
                .slug(slug)
                .title("Test Groove")
                .author(author)
                .genre(genre)
                .bpm(100)
                .level("Avanzado")
                .build();
    }

    @Test
    void findAll_withNoFilters_returnsActivePage() {
        Pageable pageable = PageRequest.of(0, 10);
        Groove groove = buildGroove("test-groove", buildUser("user1"), buildGenre("funk"));
        Page<Groove> groovePage = new PageImpl<>(List.of(groove));

        when(grooveRepository.findTrending(any(Pageable.class))).thenReturn(groovePage);

        GrooveResponse response = new GrooveResponse(
                1L, "test-groove", "Test Groove", "user1", "Test User",
                "Funk", "funk", 100, "Avanzado", 0L, 0L, false,
                List.of(), null, Map.of(), "4/4", 1, null
        );
        when(grooveMapper.toResponse(groove)).thenReturn(response);

        Page<GrooveResponse> result = grooveService.findAll(null, null, null, null, null, null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).slug()).isEqualTo("test-groove");
    }

    @Test
    void create_whenGenreNotFound_throwsBadRequestException() {
        User author = buildUser("drummer1");
        when(userRepository.findByUsername("drummer1")).thenReturn(Optional.of(author));
        when(genreRepository.findBySlug("unknown-genre")).thenReturn(Optional.empty());

        CreateGrooveRequest request = new CreateGrooveRequest(
                "My Groove", "unknown-genre", 100, "Avanzado",
                null, null, Map.of(), "4/4", 1
        );

        assertThatThrownBy(() -> grooveService.create("drummer1", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("unknown-genre");
    }

    @Test
    void create_whenValid_savesGrooveAndReturnsResponse() {
        User author = buildUser("drummer1");
        Genre genre = buildGenre("funk");
        Groove savedGroove = buildGroove("my-groove", author, genre);

        when(userRepository.findByUsername("drummer1")).thenReturn(Optional.of(author));
        when(genreRepository.findBySlug("funk")).thenReturn(Optional.of(genre));
        when(slugService.generateSlug("My Groove")).thenReturn("my-groove");
        when(grooveRepository.save(any(Groove.class))).thenReturn(savedGroove);

        GrooveResponse expectedResponse = new GrooveResponse(
                1L, "my-groove", "My Groove", "drummer1", "Test User",
                "Funk", "funk", 100, "Avanzado", 0L, 0L, false,
                List.of(), null, Map.of(), "4/4", 1, null
        );
        when(grooveMapper.toResponse(savedGroove)).thenReturn(expectedResponse);

        CreateGrooveRequest request = new CreateGrooveRequest(
                "My Groove", "funk", 100, "Avanzado",
                null, List.of(), Map.of(), "4/4", 1
        );

        GrooveResponse result = grooveService.create("drummer1", request);

        assertThat(result.slug()).isEqualTo("my-groove");
        assertThat(result.authorUsername()).isEqualTo("drummer1");
        verify(grooveRepository).save(any(Groove.class));
    }

    @Test
    void delete_whenNotOwner_throwsForbiddenException() {
        User owner = buildUser("owner");
        Genre genre = buildGenre("funk");
        Groove groove = buildGroove("some-groove", owner, genre);
        groove.setActivo(true);

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));

        assertThatThrownBy(() -> grooveService.delete("other-user", 1L))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void create_whenFreeUserExceedsLimit_throwsForbiddenException() {
        User author = buildUser("freeDrummer");
        author.setPlan(Plan.FREE);

        when(userRepository.findByUsername("freeDrummer")).thenReturn(Optional.of(author));
        when(grooveRepository.countByAuthorUsernameAndActivoTrue("freeDrummer"))
                .thenReturn((long) PlanLimits.FREE_MAX_GROOVES);

        CreateGrooveRequest request = new CreateGrooveRequest(
                "Extra Groove", "funk", 100, "Avanzado",
                null, List.of(), Map.of(), "4/4", 1
        );

        assertThatThrownBy(() -> grooveService.create("freeDrummer", request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Free plan allows up to");
    }

    @Test
    void delete_whenOwner_setsActivoFalse() {
        User owner = buildUser("owner");
        Genre genre = buildGenre("funk");
        Groove groove = buildGroove("some-groove", owner, genre);
        groove.setActivo(true);

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));
        when(grooveRepository.save(groove)).thenReturn(groove);

        grooveService.delete("owner", 1L);

        assertThat(groove.isActivo()).isFalse();
        verify(grooveRepository).save(groove);
    }
}
