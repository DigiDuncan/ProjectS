package argo.jdom;

import java.util.LinkedList;
import java.util.List;













public final class JsonArrayNodeBuilder
  implements JsonNodeBuilder<JsonRootNode>
{
  private final List<JsonNodeBuilder> elementBuilders = new LinkedList();
  



  JsonArrayNodeBuilder() {}
  



  public JsonArrayNodeBuilder withElement(JsonNodeBuilder elementBuilder)
  {
    elementBuilders.add(elementBuilder);
    return this;
  }
  
  public JsonRootNode build() {
    List<JsonNode> elements = new LinkedList();
    for (JsonNodeBuilder elementBuilder : elementBuilders) {
      elements.add(elementBuilder.build());
    }
    return JsonNodeFactories.array(elements);
  }
}
