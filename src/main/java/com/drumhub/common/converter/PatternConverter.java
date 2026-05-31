package com.drumhub.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Converter(autoApply = false)
public class PatternConverter implements AttributeConverter<Map<String, List<Integer>>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, List<Integer>>> TYPE_REF =
            new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(Map<String, List<Integer>> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize Map<String, List<Integer>> to JSON", e);
        }
    }

    @Override
    public Map<String, List<Integer>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new LinkedHashMap<>();
        }
        try {
            return MAPPER.readValue(dbData, TYPE_REF);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize JSON to Map<String, List<Integer>>: " + dbData, e);
        }
    }
}
