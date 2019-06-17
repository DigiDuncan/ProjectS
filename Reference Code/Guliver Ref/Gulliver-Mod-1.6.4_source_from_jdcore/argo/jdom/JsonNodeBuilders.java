package argo.jdom;


















public final class JsonNodeBuilders
{
  public static JsonNodeBuilder<JsonNode> aNullBuilder()
  {
    return JsonConstants.NULL;
  }
  
  public static JsonNodeBuilder<JsonNode> aTrueBuilder() {
    return JsonConstants.TRUE;
  }
  
  public static JsonNodeBuilder<JsonNode> aFalseBuilder() {
    return JsonConstants.FALSE;
  }
  






  public static JsonNodeBuilder<JsonNode> aNumberBuilder(String value)
  {
    return new JsonNumberNode(value);
  }
  





  public static JsonNodeBuilder<JsonStringNode> aStringBuilder(String value)
  {
    return new JsonStringNode(value);
  }
  





  public static JsonObjectNodeBuilder anObjectBuilder()
  {
    return JsonObjectNodeBuilder.duplicateFieldPermittingJsonObjectNodeBuilder();
  }
  














  public static JsonArrayNodeBuilder anArrayBuilder()
  {
    return new JsonArrayNodeBuilder();
  }
}
