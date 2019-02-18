package ru.training.karaf.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.SQLException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter
public class JsonConverter implements AttributeConverter<JsonNode, Object> {

    private static ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public Object convertToDatabaseColumn(JsonNode appAddress) {
        try {
            PGobject dbAddress = new PGobject();
            dbAddress.setType("jsonb");
            dbAddress.setValue(appAddress.toString());
            return dbAddress;
        } catch (SQLException ex) {
            System.err.println(
                    "An error occurred while converting to db column: " + ex);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(Object dbAddress) {
        try {
            if (dbAddress instanceof PGobject
                && ((PGobject)dbAddress).getType().equals("jsonb")) {
                return mapper.readTree(((PGobject)dbAddress).getValue());
            }
        } catch (IOException ex) {
            System.err.println(
                    "An error occurred while converting to JsonNode: " + ex);
            throw new IllegalArgumentException();
        }
        return null;
    }
}
