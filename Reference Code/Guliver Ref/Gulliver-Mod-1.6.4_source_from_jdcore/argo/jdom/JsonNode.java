package argo.jdom;

import java.util.List;
import java.util.Map;















































































public abstract class JsonNode
{
  JsonNode() {}
  
  public abstract JsonNodeType getType();
  
  public abstract String getText();
  
  public abstract Map<JsonStringNode, JsonNode> getFields();
  
  public abstract List<JsonField> getFieldList();
  
  public abstract List<JsonNode> getElements();
  
  public JsonNode getNode(Object... pathElements)
  {
    return (JsonNode)wrapExceptionsFor(JsonNodeSelectors.anyNode(pathElements), this, pathElements);
  }
  


























  public final boolean isBooleanValue(Object... pathElements)
  {
    return JsonNodeSelectors.aBooleanNode(pathElements).matches(this);
  }
  






  public final Boolean getBooleanValue(Object... pathElements)
  {
    return (Boolean)wrapExceptionsFor(JsonNodeSelectors.aBooleanNode(pathElements), this, pathElements);
  }
  


























  public final boolean isStringValue(Object... pathElements)
  {
    return JsonNodeSelectors.aStringNode(pathElements).matches(this);
  }
  






  public final String getStringValue(Object... pathElements)
  {
    return (String)wrapExceptionsFor(JsonNodeSelectors.aStringNode(pathElements), this, pathElements);
  }
  



































































































































  public final boolean isArrayNode(Object... pathElements)
  {
    return JsonNodeSelectors.anArrayNode(pathElements).matches(this);
  }
  






  public final List<JsonNode> getArrayNode(Object... pathElements)
  {
    return (List)wrapExceptionsFor(JsonNodeSelectors.anArrayNode(pathElements), this, pathElements);
  }
  


















  private <T, V extends JsonNode> T wrapExceptionsFor(JsonNodeSelector<V, T> value, V node, Object[] pathElements)
    throws JsonNodeDoesNotMatchPathElementsException
  {
    try
    {
      return value.getValue(node);
    } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException e) {
      throw JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException(e, pathElements, JsonNodeFactories.array(new JsonNode[] { node }));
    }
  }
}
