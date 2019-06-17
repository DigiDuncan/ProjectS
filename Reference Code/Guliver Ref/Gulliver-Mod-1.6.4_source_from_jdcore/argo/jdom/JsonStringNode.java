package argo.jdom;

import java.util.List;
import java.util.Map;











public final class JsonStringNode
  extends JsonNode
  implements JsonNodeBuilder<JsonStringNode>, Comparable<JsonStringNode>
{
  private final String value;
  
  JsonStringNode(String value)
  {
    if (value == null) {
      throw new NullPointerException("Attempt to construct a JsonString with a null value.");
    }
    this.value = value;
  }
  
  public JsonNodeType getType() {
    return JsonNodeType.STRING;
  }
  



  public String getText()
  {
    return value;
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
  
  public boolean equals(Object that)
  {
    if (this == that) return true;
    if ((that == null) || (getClass() != that.getClass())) { return false;
    }
    JsonStringNode thatJsonTextNode = (JsonStringNode)that;
    return value.equals(value);
  }
  
  public int hashCode()
  {
    return value.hashCode();
  }
  
  public String toString()
  {
    return "JsonStringNode{value='" + value + "'}";
  }
  
  public int compareTo(JsonStringNode that) {
    return value.compareTo(value);
  }
  
  public JsonStringNode build() {
    return this;
  }
}
