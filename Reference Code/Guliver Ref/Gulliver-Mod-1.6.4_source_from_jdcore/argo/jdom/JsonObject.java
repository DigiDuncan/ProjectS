package argo.jdom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;







final class JsonObject
  extends AbstractJsonObject
{
  private final List<JsonField> fields;
  
  JsonObject(final Iterable<JsonField> fields)
  {
    this.fields = Collections.unmodifiableList(new ArrayList() {});
  }
  




  public Map<JsonStringNode, JsonNode> getFields()
  {
    Collections.unmodifiableMap(new LinkedHashMap() {});
  }
  




  public List<JsonField> getFieldList()
  {
    return fields;
  }
}
