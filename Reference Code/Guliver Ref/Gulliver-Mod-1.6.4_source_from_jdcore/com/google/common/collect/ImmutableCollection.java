package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;
import javax.annotation.Nullable;




































public abstract class ImmutableCollection<E>
  implements Serializable, Collection<E>
{
  static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection(null);
  
  private transient ImmutableList<E> asList;
  

  ImmutableCollection() {}
  

  public abstract UnmodifiableIterator<E> iterator();
  

  public Object[] toArray()
  {
    return ObjectArrays.toArrayImpl(this);
  }
  
  public <T> T[] toArray(T[] other)
  {
    return ObjectArrays.toArrayImpl(this, other);
  }
  
  public boolean contains(@Nullable Object object)
  {
    return (object != null) && (Iterators.contains(iterator(), object));
  }
  
  public boolean containsAll(Collection<?> targets)
  {
    return Collections2.containsAllImpl(this, targets);
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public String toString() {
    return Collections2.toStringImpl(this);
  }
  






  @Deprecated
  public final boolean add(E e)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final boolean remove(Object object)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final boolean addAll(Collection<? extends E> newElements)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final boolean removeAll(Collection<?> oldElements)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final boolean retainAll(Collection<?> elementsToKeep)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final void clear()
  {
    throw new UnsupportedOperationException();
  }
  










  public ImmutableList<E> asList()
  {
    ImmutableList<E> list = asList;
    return list == null ? (this.asList = createAsList()) : list;
  }
  
  ImmutableList<E> createAsList() {
    switch (size()) {
    case 0: 
      return ImmutableList.of();
    case 1: 
      return ImmutableList.of(iterator().next());
    }
    return new RegularImmutableAsList(this, toArray());
  }
  
  private static class EmptyImmutableCollection
    extends ImmutableCollection<Object>
  {
    private EmptyImmutableCollection() {}
    
    public int size()
    {
      return 0;
    }
    
    public boolean isEmpty() {
      return true;
    }
    
    public boolean contains(@Nullable Object object) {
      return false;
    }
    
    public UnmodifiableIterator<Object> iterator() {
      return Iterators.EMPTY_LIST_ITERATOR;
    }
    
    private static final Object[] EMPTY_ARRAY = new Object[0];
    
    public Object[] toArray() {
      return EMPTY_ARRAY;
    }
    
    public <T> T[] toArray(T[] array) {
      if (array.length > 0) {
        array[0] = null;
      }
      return array;
    }
    
    ImmutableList<Object> createAsList() {
      return ImmutableList.of();
    }
  }
}
