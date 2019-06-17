package argo.jdom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;





























public final class JsonNodeSelectors
{
  public static JsonNodeSelector<JsonNode, JsonNode> anyNode(Object... pathElements)
  {
    chainOn(pathElements, new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(JsonNode jsonNode) {
        return true;
      }
      
      public String shortForm() {
        return "A node";
      }
      
      public JsonNode typeSafeApplyTo(JsonNode jsonNode) {
        return jsonNode;
      }
      
      public String toString()
      {
        return "any node";
      }
    }));
  }
  




















  public static JsonNodeSelector<JsonNode, String> aStringNode(Object... pathElements)
  {
    chainOn(pathElements, new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(JsonNode jsonNode) {
        return JsonNodeType.STRING == jsonNode.getType();
      }
      
      public String shortForm() {
        return "A short form string";
      }
      
      public String typeSafeApplyTo(JsonNode jsonNode) {
        return jsonNode.getText();
      }
      
      public String toString()
      {
        return "a value that is a string";
      }
    }));
  }
  






























































  public static JsonNodeSelector<JsonNode, Boolean> aBooleanNode(Object... pathElements)
  {
    chainOn(pathElements, new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(JsonNode jsonNode) {
        return (JsonNodeType.TRUE == jsonNode.getType()) || (JsonNodeType.FALSE == jsonNode.getType());
      }
      
      public String shortForm() {
        return "A short form boolean";
      }
      
      public Boolean typeSafeApplyTo(JsonNode jsonNode) {
        return Boolean.valueOf(JsonNodeType.TRUE == jsonNode.getType());
      }
      
      public String toString()
      {
        return "a true or false";
      }
    }));
  }
  

















































  public static JsonNodeSelector<JsonNode, List<JsonNode>> anArrayNode(Object... pathElements)
  {
    chainOn(pathElements, new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(JsonNode jsonNode) {
        return JsonNodeType.ARRAY == jsonNode.getType();
      }
      
      public String shortForm() {
        return "A short form array";
      }
      
      public List<JsonNode> typeSafeApplyTo(JsonNode jsonNode) {
        return jsonNode.getElements();
      }
      
      public String toString()
      {
        return "an array";
      }
    }));
  }
  


























  public static JsonNodeSelector<JsonNode, Map<JsonStringNode, JsonNode>> anObjectNode(Object... pathElements)
  {
    chainOn(pathElements, new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(JsonNode jsonNode) {
        return JsonNodeType.OBJECT == jsonNode.getType();
      }
      
      public String shortForm() {
        return "A short form object";
      }
      
      public Map<JsonStringNode, JsonNode> typeSafeApplyTo(JsonNode jsonNode) {
        return jsonNode.getFields();
      }
      
      public String toString()
      {
        return "an object";
      }
    }));
  }
  



























  public static JsonNodeSelector<Map<JsonStringNode, JsonNode>, JsonNode> aField(String fieldName)
  {
    return aField(JsonNodeFactories.string(fieldName));
  }
  
  public static JsonNodeSelector<Map<JsonStringNode, JsonNode>, JsonNode> aField(JsonStringNode fieldName) {
    new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(Map<JsonStringNode, JsonNode> jsonNode) {
        return jsonNode.containsKey(val$fieldName);
      }
      
      public String shortForm() {
        return "\"" + val$fieldName.getText() + "\"";
      }
      
      public JsonNode typeSafeApplyTo(Map<JsonStringNode, JsonNode> jsonNode) {
        return (JsonNode)jsonNode.get(val$fieldName);
      }
      
      public String toString()
      {
        return "a field called [\"" + val$fieldName.getText() + "\"]";
      }
    });
  }
  



  public static JsonNodeSelector<JsonNode, JsonNode> anObjectNodeWithField(String fieldName)
  {
    return anObjectNode(new Object[0]).with(aField(fieldName));
  }
  
  public static JsonNodeSelector<List<JsonNode>, JsonNode> anElement(int index) {
    new JsonNodeSelector(new LeafFunctor() {
      public boolean matchesNode(List<JsonNode> jsonNode) {
        return jsonNode.size() > val$index;
      }
      
      public String shortForm() {
        return Integer.toString(val$index);
      }
      
      public JsonNode typeSafeApplyTo(List<JsonNode> jsonNode) {
        return (JsonNode)jsonNode.get(val$index);
      }
      
      public String toString()
      {
        return "an element at index [" + val$index + "]";
      }
    });
  }
  
  public static JsonNodeSelector<JsonNode, JsonNode> anArrayNodeWithElement(int index) {
    return anArrayNode(new Object[0]).with(anElement(index));
  }
  
  private static <T> JsonNodeSelector<JsonNode, T> chainOn(Object[] pathElements, JsonNodeSelector<JsonNode, T> parentSelector) {
    JsonNodeSelector<JsonNode, T> result = parentSelector;
    for (int i = pathElements.length - 1; i >= 0; i--) {
      if ((pathElements[i] instanceof Integer)) {
        result = chainedJsonNodeSelector(anArrayNodeWithElement(((Integer)pathElements[i]).intValue()), result);
      } else if ((pathElements[i] instanceof String)) {
        result = chainedJsonNodeSelector(anObjectNodeWithField((String)pathElements[i]), result);
      } else {
        throw new IllegalArgumentException("Element [" + pathElements[i] + "] of path elements" + " [" + Arrays.toString(pathElements) + "] was of illegal type [" + pathElements[i].getClass().getCanonicalName() + "]; only Integer and String are valid.");
      }
    }
    

    return result;
  }
  
  private static <T, U, V> JsonNodeSelector<T, V> chainedJsonNodeSelector(JsonNodeSelector<T, U> parent, JsonNodeSelector<U, V> child) {
    return new JsonNodeSelector(new ChainedFunctor(parent, child));
  }
}
