package argo.jdom;













public final class JsonNodeSelector<T, U>
{
  private final Functor<T, U> valueGetter;
  











  JsonNodeSelector(Functor<T, U> valueGetter)
  {
    this.valueGetter = valueGetter;
  }
  





  public boolean matches(T jsonNode)
  {
    return valueGetter.matchesNode(jsonNode);
  }
  






  public U getValue(T argument)
  {
    return valueGetter.applyTo(argument);
  }
  











  public <V> JsonNodeSelector<T, V> with(JsonNodeSelector<U, V> childJsonNodeSelector)
  {
    return new JsonNodeSelector(new ChainedFunctor(this, childJsonNodeSelector));
  }
  
  String shortForm() {
    return valueGetter.shortForm();
  }
  
  public String toString()
  {
    return valueGetter.toString();
  }
}
