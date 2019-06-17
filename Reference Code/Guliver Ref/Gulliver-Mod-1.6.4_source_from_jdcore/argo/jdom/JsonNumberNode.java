package argo.jdom;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;










final class JsonNumberNode
  extends JsonNode
  implements JsonNodeBuilder<JsonNode>
{
  private static final Pattern PATTERN = Pattern.compile("(-?)(0|([1-9]([0-9]*)))(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
  private final String value;
  
  JsonNumberNode(String value)
  {
    if (value == null) {
      throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
    }
    if (!PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("Attempt to construct a JsonNumber with a String [" + value + "] that does not match the JSON number specification.");
    }
    this.value = value;
  }
  
  public JsonNodeType getType() {
    return JsonNodeType.NUMBER;
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
    JsonNumberNode thatJsonNumberNode = (JsonNumberNode)that;
    return value.equals(value);
  }
  
  public int hashCode()
  {
    return value.hashCode();
  }
  
  public String toString()
  {
    return "JsonNumberNode{value='" + value + "'}";
  }
  
  public JsonNode build() {
    return this;
  }
}
