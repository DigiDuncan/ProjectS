package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
























final class EmptyImmutableSet
  extends ImmutableSet<Object>
{
  static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
  
  private EmptyImmutableSet() {}
  
  public int size()
  {
    return 0;
  }
  
  public boolean isEmpty() {
    return true;
  }
  
  public boolean contains(@Nullable Object target) {
    return false;
  }
  
  public boolean containsAll(Collection<?> targets) {
    return targets.isEmpty();
  }
  
  public UnmodifiableIterator<Object> iterator() {
    return Iterators.emptyIterator();
  }
  



  public Object[] toArray()
  {
    return ObjectArrays.EMPTY_ARRAY;
  }
  
  public <T> T[] toArray(T[] a) {
    return asList().toArray(a);
  }
  
  public ImmutableList<Object> asList()
  {
    return ImmutableList.of();
  }
  
  public boolean equals(@Nullable Object object) {
    if ((object instanceof Set)) {
      Set<?> that = (Set)object;
      return that.isEmpty();
    }
    return false;
  }
  
  public final int hashCode() {
    return 0;
  }
  
  boolean isHashCodeFast() {
    return true;
  }
  
  public String toString() {
    return "[]";
  }
}
