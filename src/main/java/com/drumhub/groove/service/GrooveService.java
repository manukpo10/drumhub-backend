package com.drumhub.groove.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ForbiddenException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.repository.GenreRepository;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.CreateGrooveRequest;
import com.drumhub.groove.dto.GrooveResponse;
import com.drumhub.groove.dto.UpdateGrooveRequest;
import com.drumhub.groove.mapper.GrooveMapper;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.groove.repository.GrooveSpecification;
import com.drumhub.subscription.domain.PlanLimits;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GrooveService {

    private static final Set<String> VALID_LEVELS = Set.of("Básico", "Intermedio", "Avanzado");

    private final GrooveRepository grooveRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final GrooveMapper grooveMapper;
    private final SlugService slugService;

    @Transactional(readOnly = true)
    public Page<GrooveResponse> findAll(
            String q,
            String genre,
            String level,
            String tag,
            Integer bpmMin,
            Integer bpmMax,
            String sort,
            Pageable pageable
    ) {
        List<Specification<Groove>> specs = new ArrayList<>();
        specs.add(GrooveSpecification.isActive());

        if (q != null && !q.isBlank()) {
            specs.add(GrooveSpecification.titleOrDescContains(q));
        }
        if (genre != null && !genre.isBlank()) {
            specs.add(GrooveSpecification.hasGenreSlug(genre));
        }
        if (level != null && !level.isBlank()) {
            specs.add(GrooveSpecification.hasLevel(level));
        }
        if (tag != null && !tag.isBlank()) {
            specs.add(GrooveSpecification.hasTag(tag));
        }
        if (bpmMin != null && bpmMax != null) {
            specs.add(GrooveSpecification.hasBpmBetween(bpmMin, bpmMax));
        }

        Specification<Groove> combined = Specification.allOf(specs);

        // Trending: no filters applied → use optimized custom query for performance.
        // With filters: fall back to Specification + sort by plays+likes as proxy.
        boolean hasFilters = (q != null && !q.isBlank())
                || (genre != null && !genre.isBlank())
                || (level != null && !level.isBlank())
                || (tag != null && !tag.isBlank())
                || (bpmMin != null || bpmMax != null);

        if ((sort == null || "trending".equals(sort)) && !hasFilters) {
            Page<Groove> page = grooveRepository.findTrending(
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
            );
            return page.map(grooveMapper::toResponse);
        }

        org.springframework.data.domain.Sort sorting = GrooveSpecification.sortBy(sort);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);

        return grooveRepository.findAll(combined, sortedPageable).map(grooveMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public GrooveResponse findByIdOrSlug(String idOrSlug) {
        Groove groove;
        try {
            long id = Long.parseLong(idOrSlug);
            groove = grooveRepository.findById(id)
                    .filter(Groove::isActivo)
                    .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + idOrSlug));
        } catch (NumberFormatException e) {
            groove = grooveRepository.findBySlugAndActivoTrue(idOrSlug)
                    .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + idOrSlug));
        }
        return grooveMapper.toResponse(groove);
    }

    @Transactional(readOnly = true)
    public GrooveResponse findFeatured() {
        Groove groove = grooveRepository.findFirstByFeaturedTrueAndActivoTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No featured groove found"));
        return grooveMapper.toResponse(groove);
    }

    @Transactional
    public GrooveResponse create(String currentUsername, CreateGrooveRequest request) {
        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        if (author.getPlan() == Plan.FREE) {
            long grooveCount = grooveRepository.countByAuthorUsernameAndActivoTrue(author.getUsername());
            if (grooveCount >= PlanLimits.FREE_MAX_GROOVES) {
                throw new ForbiddenException("Free plan allows up to " + PlanLimits.FREE_MAX_GROOVES + " grooves. Upgrade to Pro for unlimited uploads.");
            }
        }

        Genre genre = genreRepository.findBySlug(request.genre())
                .orElseThrow(() -> new BadRequestException("Genre not found: " + request.genre()));

        String level = request.level();
        if (!VALID_LEVELS.contains(level)) {
            throw new BadRequestException("Invalid level. Must be one of: Básico, Intermedio, Avanzado");
        }

        String slug = slugService.generateSlug(request.title());

        Groove groove = Groove.builder()
                .title(request.title())
                .slug(slug)
                .author(author)
                .genre(genre)
                .bpm(request.bpm())
                .level(level)
                .description(request.description())
                .tags(request.tags() != null ? request.tags() : new ArrayList<>())
                .pattern(request.pattern())
                .timeSig(request.timeSig() != null ? request.timeSig() : "4/4")
                .bars(request.bars() != null ? request.bars() : 1)
                .kit(request.kit() != null ? request.kit() : "pearl")
                .build();

        return grooveMapper.toResponse(grooveRepository.save(groove));
    }

    @Transactional
    public GrooveResponse update(String currentUsername, Long id, UpdateGrooveRequest request) {
        Groove groove = grooveRepository.findById(id)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + id));

        if (!currentUsername.equals(groove.getAuthor().getUsername())) {
            throw new ForbiddenException("You are not the author of this groove");
        }

        Genre genre = genreRepository.findBySlug(request.genre())
                .orElseThrow(() -> new BadRequestException("Genre not found: " + request.genre()));

        String level = request.level();
        if (!VALID_LEVELS.contains(level)) {
            throw new BadRequestException("Invalid level. Must be one of: Básico, Intermedio, Avanzado");
        }

        groove.setTitle(request.title());
        groove.setGenre(genre);
        groove.setBpm(request.bpm());
        groove.setLevel(level);
        groove.setDescription(request.description());
        groove.setTags(request.tags() != null ? request.tags() : new ArrayList<>());
        groove.setPattern(request.pattern());
        if (request.timeSig() != null) {
            groove.setTimeSig(request.timeSig());
        }
        if (request.bars() != null) {
            groove.setBars(request.bars());
        }
        if (request.kit() != null) {
            groove.setKit(request.kit());
        }

        return grooveMapper.toResponse(grooveRepository.save(groove));
    }

    @Transactional
    public void delete(String currentUsername, Long id) {
        Groove groove = grooveRepository.findById(id)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + id));

        if (!currentUsername.equals(groove.getAuthor().getUsername())) {
            throw new ForbiddenException("You are not the author of this groove");
        }

        groove.setActivo(false);
        grooveRepository.save(groove);
    }

    @Transactional
    public GrooveResponse incrementPlays(Long id) {
        Groove groove = grooveRepository.findById(id)
                .filter(Groove::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Groove not found: " + id));

        groove.setPlays(groove.getPlays() + 1);
        return grooveMapper.toResponse(grooveRepository.save(groove));
    }
}
