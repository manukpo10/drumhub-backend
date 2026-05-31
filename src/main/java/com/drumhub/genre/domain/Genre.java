package com.drumhub.genre.domain;

import com.drumhub.common.audit.BaseEntity;
import com.drumhub.common.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Genre extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String slug;

    @Column(nullable = false, length = 10)
    private String icon;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(nullable = false)
    private Integer bpmMin;

    @Column(nullable = false)
    private Integer bpmMax;

    @Column(nullable = false, length = 20)
    private String level;

    @Column(nullable = false, length = 10)
    private String timeSig;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> subgenres;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> related;
}
