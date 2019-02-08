package ru.training.karaf.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.Pair;


public class BookDTOSerializer extends StdSerializer<BookDTO> {

    public BookDTOSerializer() {
        this(null);
    }
    
    public BookDTOSerializer(Class<BookDTO> b) {
        super(b);
    }
    
    @Override
    public void serialize(BookDTO book, JsonGenerator jg, SerializerProvider sp)
            throws IOException {
        
        jg.writeStartObject();
        jg.writeStringField("author", book.getAuthor());
        jg.writeStringField("title", book.getTitle());
        jg.writeNumberField("year", book.getYear());
        jg.writeObjectField("genre", book.getGenre());
        jg.writeFieldName("properties");
        jg.writeStartObject();
        for (Pair p: book.getProperties()) {
            jg.writeStringField(p.getFirst(), p.getSecond());
        }
        jg.writeEndObject();
        jg.writeEndObject();
    }
    
}
