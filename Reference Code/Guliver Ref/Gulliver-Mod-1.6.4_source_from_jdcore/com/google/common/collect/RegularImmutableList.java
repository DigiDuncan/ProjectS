package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
























class RegularImmutableList<E>
  extends ImmutableList<E>
{
  private final transient int offset;
  private final transient int size;
  private final transient Object[] array;
  
  RegularImmutableList(Object[] array, int offset, int size)
  {
    this.offset = offset;
    this.size = size;
    this.array = array;
  }
  
  RegularImmutableList(Object[] array) {
    this(array, 0, array.length);
  }
  
  public int size()
  {
    return size;
  }
  
  public boolean isEmpty() {
    return false;
  }
  



  public Object[] toArray()
  {
    Object[] newArray = new Object[size()];
    System.arraycopy(array, offset, newArray, 0, size);
    return newArray;
  }
  
  public <T> T[] toArray(T[] other) {
    if (other.length < size) {
      other = ObjectArrays.newArray(other, size);
    } else if (other.length > size) {
      other[size] = null;
    }
    System.arraycopy(array, offset, other, 0, size);
    return other;
  }
  


  public E get(int index)
  {
    Preconditions.checkElementIndex(index, size);
    return array[(index + offset)];
  }
  
  ImmutableList<E> subListUnchecked(int fromIndex, int toIndex)
  {
    return new RegularImmutableList(array, offset + fromIndex, toIndex - fromIndex);
  }
  




  public UnmodifiableListIterator<E> listIterator(int index)
  {
    return Iterators.forArray(array, offset, size, index);
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if (!(object instanceof List)) {
      return false;
    }
    
    List<?> that = (List)object;
    if (size() != that.size()) {
      return false;
    }
    
    int index = offset;
    if ((object instanceof RegularImmutableList)) {
      RegularImmutableList<?> other = (RegularImmutableList)object;
      for (int i = offset; i < offset + size; i++) {
        if (!array[(index++)].equals(array[i])) {
          return false;
        }
      }
    } else {
      for (Object element : that) {
        if (!array[(index++)].equals(element)) {
          return false;
        }
      }
    }
    return true;
  }
  
  public String toString() {
    StringBuilder sb = Collections2.newStringBuilderForCollection(size()).append('[').append(array[offset]);
    
    for (int i = offset + 1; i < offset + size; i++) {
      sb.append(", ").append(array[i]);
    }
    return ']';
  }
}
