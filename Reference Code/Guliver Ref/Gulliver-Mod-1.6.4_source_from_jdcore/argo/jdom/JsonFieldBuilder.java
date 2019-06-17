package argo.jdom;




final class JsonFieldBuilder
{
  private JsonStringNode key;
  


  private JsonNodeBuilder valueBuilder;
  



  private JsonFieldBuilder() {}
  



  static JsonFieldBuilder aJsonFieldBuilder()
  {
    return new JsonFieldBuilder();
  }
  
  JsonFieldBuilder withKey(JsonStringNode jsonStringNode) {
    key = jsonStringNode;
    return this;
  }
  
  JsonFieldBuilder withValue(JsonNodeBuilder jsonNodeBuilder) {
    valueBuilder = jsonNodeBuilder;
    return this;
  }
  
  JsonStringNode buildKey() {
    return key;
  }
  
  JsonNode buildValue() {
    return valueBuilder.build();
  }
  
  JsonField build() {
    return JsonNodeFactories.field(buildKey(), buildValue());
  }
}
