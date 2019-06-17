package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;




























































public abstract class ImmutableList<E>
  extends ImmutableCollection<E>
  implements List<E>, RandomAccess
{
  public static <E> ImmutableList<E> of()
  {
    return EmptyImmutableList.INSTANCE;
  }
  







  public static <E> ImmutableList<E> of(E element)
  {
    return new SingletonImmutableList(element);
  }
  

















































































































































































































  static <E> ImmutableList<E> asImmutableList(Object[] elements)
  {
    switch (elements.length) {
    case 0: 
      return of();
    
    case 1: 
      ImmutableList<E> list = new SingletonImmutableList(elements[0]);
      return list;
    }
    return construct(elements);
  }
  






  private static <E> ImmutableList<E> construct(Object... elements)
  {
    for (int i = 0; i < elements.length; i++) {
      ObjectArrays.checkElementNotNull(elements[i], i);
    }
    return new RegularImmutableList(elements);
  }
  

  ImmutableList() {}
  
  public UnmodifiableIterator<E> iterator()
  {
    return listIterator();
  }
  
  public UnmodifiableListIterator<E> listIterator() {
    return listIterator(0);
  }
  
  public UnmodifiableListIterator<E> listIterator(int index) {
    new AbstractIndexedListIterator(size(), index)
    {
      protected E get(int index) {
        return ImmutableList.this.get(index);
      }
    };
  }
  
  public int indexOf(@Nullable Object object)
  {
    return object == null ? -1 : Lists.indexOfImpl(this, object);
  }
  
  public int lastIndexOf(@Nullable Object object)
  {
    return object == null ? -1 : Lists.lastIndexOfImpl(this, object);
  }
  
  public boolean contains(@Nullable Object object)
  {
    return indexOf(object) >= 0;
  }
  








  public ImmutableList<E> subList(int fromIndex, int toIndex)
  {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    switch (length) {
    case 0: 
      return of();
    case 1: 
      return of(get(fromIndex));
    }
    return subListUnchecked(fromIndex, toIndex);
  }
  





  ImmutableList<E> subListUnchecked(int fromIndex, int toIndex)
  {
    return new SubList(fromIndex, toIndex - fromIndex);
  }
  
  class SubList extends ImmutableList<E> {
    final transient int offset;
    final transient int length;
    
    SubList(int offset, int length) {
      this.offset = offset;
      this.length = length;
    }
    
    public int size()
    {
      return length;
    }
    
    public E get(int index)
    {
      Preconditions.checkElementIndex(index, length);
      return ImmutableList.this.get(index + offset);
    }
    
    public ImmutableList<E> subList(int fromIndex, int toIndex)
    {
      Preconditions.checkPositionIndexes(fromIndex, toIndex, length);
      return ImmutableList.this.subList(fromIndex + offset, toIndex + offset);
    }
  }
  











  @Deprecated
  public final boolean addAll(int index, Collection<? extends E> newElements)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final E set(int index, E element)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final void add(int index, E element)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final E remove(int index)
  {
    throw new UnsupportedOperationException();
  }
  




  public ImmutableList<E> asList()
  {
    return this;
  }
  









































































































  public boolean equals(Object obj)
  {
    return Lists.equalsImpl(this, obj);
  }
  
  public int hashCode() {
    return Lists.hashCodeImpl(this);
  }
}
