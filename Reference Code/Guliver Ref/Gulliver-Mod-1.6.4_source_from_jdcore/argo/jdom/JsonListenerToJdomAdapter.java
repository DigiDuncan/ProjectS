package argo.jdom;

import argo.saj.JsonListener;
import java.util.Stack;









final class JsonListenerToJdomAdapter
  implements JsonListener
{
  private final Stack<NodeContainer> stack;
  private JsonNodeBuilder<JsonRootNode> root;
  
  JsonListenerToJdomAdapter()
  {
    stack = new Stack();
  }
  
  JsonRootNode getDocument() {
    return (JsonRootNode)root.build();
  }
  

  public void startDocument() {}
  
  public void endDocument() {}
  
  public void startArray()
  {
    final JsonArrayNodeBuilder arrayBuilder = JsonNodeBuilders.anArrayBuilder();
    addRootNode(arrayBuilder);
    stack.push(new NodeContainer() {
      public void addNode(JsonNodeBuilder jsonNodeBuilder) {
        arrayBuilder.withElement(jsonNodeBuilder);
      }
      
      public void addField(JsonFieldBuilder jsonFieldBuilder) {
        throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
      }
    });
  }
  
  public void endArray() {
    stack.pop();
  }
  
  public void startObject() {
    final JsonObjectNodeBuilder objectNodeBuilder = JsonNodeBuilders.anObjectBuilder();
    addRootNode(objectNodeBuilder);
    stack.push(new NodeContainer() {
      public void addNode(JsonNodeBuilder jsonNodeBuilder) {
        throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
      }
      
      public void addField(JsonFieldBuilder jsonFieldBuilder) {
        objectNodeBuilder.withFieldBuilder(jsonFieldBuilder);
      }
    });
  }
  
  public void endObject() {
    stack.pop();
  }
  
  public void startField(String name) {
    final JsonFieldBuilder fieldBuilder = JsonFieldBuilder.aJsonFieldBuilder().withKey(JsonNodeFactories.string(name));
    ((NodeContainer)stack.peek()).addField(fieldBuilder);
    stack.push(new NodeContainer() {
      public void addNode(JsonNodeBuilder jsonNodeBuilder) {
        fieldBuilder.withValue(jsonNodeBuilder);
      }
      
      public void addField(JsonFieldBuilder jsonFieldBuilder) {
        throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
      }
    });
  }
  
  public void endField() {
    stack.pop();
  }
  
  public void numberValue(String value) {
    addValue(JsonNodeBuilders.aNumberBuilder(value));
  }
  
  public void trueValue() {
    addValue(JsonNodeBuilders.aTrueBuilder());
  }
  
  public void stringValue(String value) {
    addValue(JsonNodeBuilders.aStringBuilder(value));
  }
  
  public void falseValue() {
    addValue(JsonNodeBuilders.aFalseBuilder());
  }
  
  public void nullValue() {
    addValue(JsonNodeBuilders.aNullBuilder());
  }
  
  private void addRootNode(JsonNodeBuilder<JsonRootNode> rootNodeBuilder) {
    if (root == null) {
      root = rootNodeBuilder;
    } else {
      addValue(rootNodeBuilder);
    }
  }
  
  private void addValue(JsonNodeBuilder nodeBuilder) {
    ((NodeContainer)stack.peek()).addNode(nodeBuilder);
  }
  
  private static abstract interface NodeContainer
  {
    public abstract void addNode(JsonNodeBuilder paramJsonNodeBuilder);
    
    public abstract void addField(JsonFieldBuilder paramJsonFieldBuilder);
  }
}
