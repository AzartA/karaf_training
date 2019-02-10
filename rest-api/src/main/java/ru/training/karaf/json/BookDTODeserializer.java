package ru.training.karaf.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.GenreDTO;
import ru.training.karaf.rest.dto.Pair;

public class BookDTODeserializer extends StdDeserializer<BookDTO> {

    public BookDTODeserializer() {
        this(null);
    }
    
    public BookDTODeserializer(Class<?> b) {
        super(b);
    }
    
    @Override
    public BookDTO deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
    
        JsonNode node = parser.getCodec().readTree(parser);
        String title = node.get("title").textValue();
        String author = node.get("author").textValue();
        Integer year = (Integer)((IntNode)node.get("year")).numberValue();
        GenreDTO genre = new GenreDTO();
        genre.setName(node.get("genre").findValue("name").textValue());
        
        List<Pair> properties = new ArrayList<>();
        JsonNode propsNode = node.get("properties");
        if (propsNode != null) {
            Iterator<Entry<String, JsonNode>> it = propsNode.fields();
            while (it.hasNext()) {
                Entry<String, JsonNode> entry = it.next();
                Pair pair = new Pair(entry.getKey(), entry.getValue().textValue());
                properties.add(pair);
            }
        }
        
        BookDTO book = new BookDTO();
        book.setAuthor(author);
        book.setGenre(genre);
        book.setProperties(properties);
        book.setTitle(title);
        book.setYear(year);
        
        return book;
    }
    
}
