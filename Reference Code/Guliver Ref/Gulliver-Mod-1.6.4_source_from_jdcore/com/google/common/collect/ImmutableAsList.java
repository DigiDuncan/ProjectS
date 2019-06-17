package com.google.common.collect;











abstract class ImmutableAsList<E>
  extends ImmutableList<E>
{
  ImmutableAsList() {}
  










  abstract ImmutableCollection<E> delegateCollection();
  









  public boolean contains(Object target)
  {
    return delegateCollection().contains(target);
  }
  
  public int size()
  {
    return delegateCollection().size();
  }
  
  public boolean isEmpty()
  {
    return delegateCollection().isEmpty();
  }
}
