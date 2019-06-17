package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;



























final class EmptyImmutableList
  extends ImmutableList<Object>
{
  static final EmptyImmutableList INSTANCE = new EmptyImmutableList();
  
  private EmptyImmutableList() {}
  
  public int size()
  {
    return 0;
  }
  
  public boolean isEmpty() {
    return true;
  }
  



  public boolean contains(@Nullable Object target)
  {
    return false;
  }
  
  public boolean containsAll(Collection<?> targets) {
    return targets.isEmpty();
  }
  
  public UnmodifiableIterator<Object> iterator() {
    return listIterator();
  }
  
  public Object[] toArray() {
    return ObjectArrays.EMPTY_ARRAY;
  }
  
  public <T> T[] toArray(T[] a) {
    if (a.length > 0) {
      a[0] = null;
    }
    return a;
  }
  

  public Object get(int index)
  {
    Preconditions.checkElementIndex(index, 0);
    throw new AssertionError("unreachable");
  }
  
  public int indexOf(@Nullable Object target) {
    return -1;
  }
  
  public int lastIndexOf(@Nullable Object target) {
    return -1;
  }
  
  public ImmutableList<Object> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, 0);
    return this;
  }
  



  public UnmodifiableListIterator<Object> listIterator()
  {
    return Iterators.EMPTY_LIST_ITERATOR;
  }
  
  public UnmodifiableListIterator<Object> listIterator(int start) {
    Preconditions.checkPositionIndex(start, 0);
    return Iterators.EMPTY_LIST_ITERATOR;
  }
  
  public boolean equals(@Nullable Object object) {
    if ((object instanceof List)) {
      List<?> that = (List)object;
      return that.isEmpty();
    }
    return false;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "[]";
  }
}
