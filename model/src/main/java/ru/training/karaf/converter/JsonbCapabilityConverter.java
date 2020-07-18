package ru.training.karaf.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.training.karaf.model.Capability;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class JsonbCapabilityConverter implements AttributeConverter<Capability, String> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Capability entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable serialize to jsonb field from entity object", e);
        }


    }

    @Override
    public Capability convertToEntityAttribute(String jsonb) {
        try {
            return mapper.readValue(jsonb, Capability.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable deserialize to entity object from jsonb field ", e);
        }
    }
}
