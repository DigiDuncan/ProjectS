package argo.jdom;

import java.util.List;
import java.util.Map;






abstract class AbstractJsonArray
  extends JsonRootNode
{
  AbstractJsonArray() {}
  
  public JsonNodeType getType()
  {
    return JsonNodeType.ARRAY;
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
  




  public boolean equals(Object that)
  {
    if (this == that) return true;
    if ((that == null) || (!AbstractJsonArray.class.isAssignableFrom(that.getClass()))) { return false;
    }
    AbstractJsonArray thatJsonArray = (AbstractJsonArray)that;
    return getElements().equals(thatJsonArray.getElements());
  }
  
  public int hashCode()
  {
    return getElements().hashCode();
  }
  
  public String toString()
  {
    return "JsonArray{elements=" + getElements() + "}";
  }
}
