package argo.jdom;

import java.util.List;
import java.util.Map;









final class JsonConstants
  extends JsonNode
  implements JsonNodeBuilder<JsonNode>
{
  static final JsonConstants NULL = new JsonConstants(JsonNodeType.NULL);
  static final JsonConstants TRUE = new JsonConstants(JsonNodeType.TRUE);
  static final JsonConstants FALSE = new JsonConstants(JsonNodeType.FALSE);
  private final JsonNodeType jsonNodeType;
  
  private JsonConstants(JsonNodeType jsonNodeType)
  {
    this.jsonNodeType = jsonNodeType;
  }
  
  public JsonNodeType getType() {
    return jsonNodeType;
  }
  



  public String getText()
  {
    throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
  }
  



  public Map<JsonStringNode, JsonNode> getFields()
  {
    throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
  }
  
  public List<JsonField> getFieldList()
  {
    throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
  }
  



  public List<JsonNode> getElements()
  {
    throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
  }
  
  public JsonNode build() {
    return this;
  }
  
  public String toString()
  {
    return "JsonNode{jsonNodeType=" + jsonNodeType + '}';
  }
}
