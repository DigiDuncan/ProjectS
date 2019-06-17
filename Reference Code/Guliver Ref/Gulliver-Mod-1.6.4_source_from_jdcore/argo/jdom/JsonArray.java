package argo.jdom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;










final class JsonArray
  extends AbstractJsonArray
{
  private final List<JsonNode> elements;
  
  JsonArray(final Iterable<? extends JsonNode> elements)
  {
    this.elements = Collections.unmodifiableList(new ArrayList() {});
  }
  



  public List<JsonNode> getElements()
  {
    return elements;
  }
}
