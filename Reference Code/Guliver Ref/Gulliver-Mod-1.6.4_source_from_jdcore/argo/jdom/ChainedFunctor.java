package argo.jdom;





final class ChainedFunctor<T, U, V>
  implements Functor<T, V>
{
  private final JsonNodeSelector<T, U> parentJsonNodeSelector;
  


  private final JsonNodeSelector<U, V> childJsonNodeSelector;
  



  ChainedFunctor(JsonNodeSelector<T, U> parentJsonNodeSelector, JsonNodeSelector<U, V> childJsonNodeSelector)
  {
    this.parentJsonNodeSelector = parentJsonNodeSelector;
    this.childJsonNodeSelector = childJsonNodeSelector;
  }
  
  public boolean matchesNode(T jsonNode) {
    return (parentJsonNodeSelector.matches(jsonNode)) && (childJsonNodeSelector.matches(parentJsonNodeSelector.getValue(jsonNode)));
  }
  
  public V applyTo(T jsonNode) {
    U parent;
    try {
      parent = parentJsonNodeSelector.getValue(jsonNode);
    } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException e) {
      throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createUnchainedJsonNodeDoesNotMatchJsonNodeSelectorException(e, parentJsonNodeSelector);
    }
    V value;
    try {
      value = childJsonNodeSelector.getValue(parent);
    } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException e) {
      throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createChainedJsonNodeDoesNotMatchJsonNodeSelectorException(e, parentJsonNodeSelector);
    }
    return value;
  }
  
  public String shortForm() {
    return childJsonNodeSelector.shortForm();
  }
  
  public String toString()
  {
    return parentJsonNodeSelector.toString() + ", with " + childJsonNodeSelector.toString();
  }
}
