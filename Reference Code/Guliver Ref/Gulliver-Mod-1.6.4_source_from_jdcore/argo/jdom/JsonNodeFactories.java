package argo.jdom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;











































public final class JsonNodeFactories
{
  public static JsonStringNode string(String value)
  {
    return new JsonStringNode(value);
  }
  



































  public static JsonRootNode array(Iterable<? extends JsonNode> elements)
  {
    return new JsonArray(elements);
  }
  



  public static JsonRootNode array(JsonNode... elements)
  {
    return array(Arrays.asList(elements));
  }
  































  public static JsonRootNode object(Map<JsonStringNode, ? extends JsonNode> fields)
  {
    new JsonObject(new ArrayList() {});
  }
  







  public static JsonRootNode object(JsonField... fields)
  {
    return object(Arrays.asList(fields));
  }
  



  public static JsonRootNode object(Iterable<JsonField> fields)
  {
    return new JsonObject(fields);
  }
  
























































  public static JsonField field(String name, JsonNode value)
  {
    return new JsonField(string(name), value);
  }
  




  public static JsonField field(JsonStringNode name, JsonNode value)
  {
    return new JsonField(name, value);
  }
}
