package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Groove;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public final class GrooveSpecification {

    private GrooveSpecification() {}

    public static Specification<Groove> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("activo"));
    }

    public static Specification<Groove> hasGenreSlug(String genreSlug) {
        return (root, query, cb) ->
                cb.equal(root.join("genre").get("slug"), genreSlug);
    }

    public static Specification<Groove> hasLevel(String level) {
        return (root, query, cb) -> cb.equal(root.get("level"), level);
    }

    public static Specification<Groove> hasBpmBetween(Integer min, Integer max) {
        return (root, query, cb) ->
                cb.between(root.get("bpm"), min, max);
    }

    public static Specification<Groove> titleOrDescContains(String q) {
        return (root, query, cb) -> {
            String pattern = "%" + q.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Groove> hasTag(String tag) {
        // tags is stored as a JSON string (StringListConverter), e.g. ["shuffle","ghost notes"].
        // Cast the converted attribute to String and match the quoted tag so we hit the exact
        // element, not partial substrings (e.g. "rock" must not match "rock-clasico").
        return (root, query, cb) ->
                cb.like(root.get("tags").as(String.class), "%\"" + tag + "\"%");
    }

    /**
     * Returns a Sort for the given sort string.
     * "trending" without filters is handled by a custom @Query in the repository.
     * When "trending" is used with filters, plays DESC + likes DESC serves as a proxy.
     */
    public static Sort sortBy(String sort) {
        return switch (sort == null ? "trending" : sort) {
            case "likes"    -> Sort.by("likes").descending();
            case "new"      -> Sort.by("createdAt").descending();
            case "bpm"      -> Sort.by("bpm").ascending();
            default         -> Sort.by("plays").descending().and(Sort.by("likes").descending());
        };
    }
}
