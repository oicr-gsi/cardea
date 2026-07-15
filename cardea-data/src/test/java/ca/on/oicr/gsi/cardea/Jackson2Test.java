package ca.on.oicr.gsi.cardea;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;

public class Jackson2Test extends JacksonTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @BeforeAll
  public static void setup() {
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
  }

  @Override
  protected String serialize(Object value) throws JsonProcessingException {
    return mapper.writeValueAsString(value);
  }

  @Override
  protected <T> T deserialize(String value, Class<T> clazz)
      throws JsonMappingException, JsonProcessingException {
    return mapper.readerFor(clazz).readValue(value);
  }
}
