package ru.training.karaf.rest.serializer;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.training.karaf.model.Entity;

public class SetOfEntSerializer extends StdSerializer<Set<Entity>> {

    public SetOfEntSerializer() {
        this(null);
    }

    public SetOfEntSerializer(Class<Set<Entity>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Entity> eSet, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeObject(eSet);
    }
}
