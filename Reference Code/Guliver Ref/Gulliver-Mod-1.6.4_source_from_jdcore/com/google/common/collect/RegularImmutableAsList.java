package com.google.common.collect;









class RegularImmutableAsList<E>
  extends ImmutableAsList<E>
{
  private final ImmutableCollection<E> delegate;
  







  private final ImmutableList<? extends E> delegateList;
  







  RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList)
  {
    this.delegate = delegate;
    this.delegateList = delegateList;
  }
  
  RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
    this(delegate, ImmutableList.asImmutableList(array));
  }
  
  ImmutableCollection<E> delegateCollection()
  {
    return delegate;
  }
  





  public UnmodifiableListIterator<E> listIterator(int index)
  {
    return delegateList.listIterator(index);
  }
  
  public Object[] toArray()
  {
    return delegateList.toArray();
  }
  
  public <T> T[] toArray(T[] other)
  {
    return delegateList.toArray(other);
  }
  
  public int indexOf(Object object)
  {
    return delegateList.indexOf(object);
  }
  
  public int lastIndexOf(Object object)
  {
    return delegateList.lastIndexOf(object);
  }
  
  public boolean equals(Object obj)
  {
    return delegateList.equals(obj);
  }
  
  public int hashCode()
  {
    return delegateList.hashCode();
  }
  
  public E get(int index)
  {
    return delegateList.get(index);
  }
}
