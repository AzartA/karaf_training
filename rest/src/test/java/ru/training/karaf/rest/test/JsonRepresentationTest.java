package ru.training.karaf.rest.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.GenreDTO;
import ru.training.karaf.json.Pair;


public class JsonRepresentationTest {
    
    private BookDTO book = new BookDTO();
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private static final String PROPS =
            "{\"k1\":\"v1\",\"k2\":\"v2\",\"k3\":\"v3\"}";
    
    @BeforeEach
    public void initBook() {
        book.setAuthor("Author");
        book.setGenre(new GenreDTO("Genre"));
        book.setTitle("Title");
        book.setYear(2000);
        
        List<Pair> props = new ArrayList<>();
        props.add(new Pair("k1", "v1"));
        props.add(new Pair("k2", "v2"));
        props.add(new Pair("k3", "v3"));
        
        book.setProperties(props);
    }
    
    @Test
    public void serializerTest() throws JsonProcessingException, IOException {
        String serialized = mapper.writeValueAsString(book);
        JsonNode node = mapper.readTree(serialized).get("properties");
        
        assertEquals(PROPS, node.toString());
    }
    
    @Test
    public void deserializerTest() throws JsonProcessingException, IOException {
        String serialized = mapper.writeValueAsString(book);
        
        BookDTO constructedFromJson = mapper.readValue(serialized, BookDTO.class);
        
        assertEquals(book, constructedFromJson);
    }
}
