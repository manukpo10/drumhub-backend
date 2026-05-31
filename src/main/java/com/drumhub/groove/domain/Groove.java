package com.drumhub.groove.domain;

import com.drumhub.common.audit.BaseEntity;
import com.drumhub.common.converter.PatternConverter;
import com.drumhub.common.converter.StringListConverter;
import com.drumhub.genre.domain.Genre;
import com.drumhub.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "grooves")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Groove extends BaseEntity {

    @Column(unique = true, nullable = false, length = 150)
    private String slug;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private Integer bpm;

    @Column(nullable = false, length = 20)
    private String level;

    @Column(nullable = false)
    @Builder.Default
    private Long likes = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long plays = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Boolean featured = false;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = PatternConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    @Builder.Default
    private Map<String, List<Integer>> pattern = new LinkedHashMap<>();

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String timeSig = "4/4";

    @Column(nullable = false)
    @Builder.Default
    private Integer bars = 1;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String kit = "pearl";
}
