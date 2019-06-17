package argo.jdom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;









public final class JsonObjectNodeBuilder
  implements JsonNodeBuilder<JsonRootNode>
{
  private final FieldCollector fieldCollector;
  
  static JsonObjectNodeBuilder duplicateFieldPermittingJsonObjectNodeBuilder()
  {
    new JsonObjectNodeBuilder(new FieldCollector() {
      private final List<JsonFieldBuilder> fieldBuilders = new LinkedList();
      
      public void add(JsonFieldBuilder jsonFieldBuilder) {
        fieldBuilders.add(jsonFieldBuilder);
      }
      
      public Iterator<JsonField> iterator() {
        final Iterator<JsonFieldBuilder> delegate = fieldBuilders.iterator();
        new Iterator() {
          public boolean hasNext() {
            return delegate.hasNext();
          }
          
          public JsonField next() {
            return ((JsonFieldBuilder)delegate.next()).build();
          }
          
          public void remove() {
            delegate.remove();
          }
        };
      }
    });
  }
  





































  private JsonObjectNodeBuilder(FieldCollector fieldCollector)
  {
    this.fieldCollector = fieldCollector;
  }
  



























  public JsonObjectNodeBuilder withFieldBuilder(JsonFieldBuilder jsonFieldBuilder)
  {
    fieldCollector.add(jsonFieldBuilder);
    return this;
  }
  
  public JsonRootNode build() {
    JsonNodeFactories.object(new ArrayList() {});
  }
  
  private static abstract interface FieldCollector
    extends Iterable<JsonField>
  {
    public abstract void add(JsonFieldBuilder paramJsonFieldBuilder);
  }
}
