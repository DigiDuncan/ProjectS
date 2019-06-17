package argo.jdom;




abstract class LeafFunctor<T, V>
  implements Functor<T, V>
{
  LeafFunctor() {}
  


  public final V applyTo(T jsonNode)
  {
    if (!matchesNode(jsonNode)) {
      throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createJsonNodeDoesNotMatchJsonNodeSelectorException(this);
    }
    return typeSafeApplyTo(jsonNode);
  }
  
  protected abstract V typeSafeApplyTo(T paramT);
}
