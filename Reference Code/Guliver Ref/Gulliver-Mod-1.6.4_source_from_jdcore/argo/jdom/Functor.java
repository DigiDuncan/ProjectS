package argo.jdom;

abstract interface Functor<T, V>
{
  public abstract boolean matchesNode(T paramT);
  
  public abstract V applyTo(T paramT);
  
  public abstract String shortForm();
}
