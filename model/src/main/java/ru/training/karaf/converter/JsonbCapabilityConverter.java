package ru.training.karaf.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import ru.training.karaf.model.CapabilityImpl;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class JsonbCapabilityConverter implements AttributeConverter<CapabilityImpl, Object> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object convertToDatabaseColumn(CapabilityImpl entity) {
        try {
            PGobject out = new PGobject();
            out.setType("jsonb");
            out.setValue(mapper.writeValueAsString(entity));
            return out;

        } catch (Exception e) {
            throw new IllegalArgumentException("Unable serialize to jsonb field from entity object", e);
        }


    }

    @Override
    public CapabilityImpl convertToEntityAttribute(Object jsonb) {
        try {

            return mapper.readValue(((PGobject)jsonb).getValue(), CapabilityImpl.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable deserialize to entity object from jsonb field ", e);
        }
    }
}


