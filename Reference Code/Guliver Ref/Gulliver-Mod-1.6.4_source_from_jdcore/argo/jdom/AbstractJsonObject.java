package argo.jdom;

import java.util.List;







abstract class AbstractJsonObject
  extends JsonRootNode
{
  AbstractJsonObject() {}
  
  public JsonNodeType getType()
  {
    return JsonNodeType.OBJECT;
  }
  



  public String getText()
  {
    throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
  }
  







  public List<JsonNode> getElements()
  {
    throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
  }
  
  public boolean equals(Object that)
  {
    if (this == that) return true;
    if ((that == null) || (!AbstractJsonObject.class.isAssignableFrom(that.getClass()))) { return false;
    }
    AbstractJsonObject thatJsonObject = (AbstractJsonObject)that;
    return getFieldList().equals(thatJsonObject.getFieldList());
  }
  
  public int hashCode()
  {
    return getFieldList().hashCode();
  }
  
  public String toString()
  {
    return "JsonObject{fields=" + getFieldList() + "}";
  }
}
