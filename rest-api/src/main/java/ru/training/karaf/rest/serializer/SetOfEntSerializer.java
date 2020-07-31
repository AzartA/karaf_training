package ru.training.karaf.rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.training.karaf.model.Entity;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class SetOfEntSerializer extends StdSerializer<Set<Entity>>{

    public SetOfEntSerializer() {
        this(null);
    }

    public SetOfEntSerializer(Class<Set<Entity>> t) {
        super(t);
    }

        @Override
        public void serialize(Set<Entity> eSet, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        //Set<Long> ids= eSet.stream().map(en -> en.getId()).collect(Collectors.toSet());
        //generator.writeObject(ids)
        generator.writeObject(eSet);


    }
    }
