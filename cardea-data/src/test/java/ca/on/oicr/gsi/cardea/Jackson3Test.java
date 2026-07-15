package ca.on.oicr.gsi.cardea;

import tools.jackson.databind.json.JsonMapper;

public class Jackson3Test extends JacksonTest {

  private final JsonMapper mapper = new JsonMapper();

  @Override
  protected String serialize(Object value) {
    return mapper.writeValueAsString(value);
  }

  @Override
  protected <T> T deserialize(String value, Class<T> clazz) throws Exception {
    return mapper.readerFor(clazz).readValue(value);
  }
}
