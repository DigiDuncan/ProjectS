package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
































final class SingletonImmutableSet<E>
  extends ImmutableSet<E>
{
  final transient E element;
  private transient int cachedHashCode;
  
  SingletonImmutableSet(E element)
  {
    this.element = Preconditions.checkNotNull(element);
  }
  
  SingletonImmutableSet(E element, int hashCode)
  {
    this.element = element;
    cachedHashCode = hashCode;
  }
  
  public int size()
  {
    return 1;
  }
  
  public boolean isEmpty() {
    return false;
  }
  
  public boolean contains(Object target) {
    return element.equals(target);
  }
  
  public UnmodifiableIterator<E> iterator() {
    return Iterators.singletonIterator(element);
  }
  



  public Object[] toArray()
  {
    return new Object[] { element };
  }
  
  public <T> T[] toArray(T[] array) {
    if (array.length == 0) {
      array = ObjectArrays.newArray(array, 1);
    } else if (array.length > 1) {
      array[1] = null;
    }
    
    Object[] objectArray = array;
    objectArray[0] = element;
    return array;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this) {
      return true;
    }
    if ((object instanceof Set)) {
      Set<?> that = (Set)object;
      return (that.size() == 1) && (element.equals(that.iterator().next()));
    }
    return false;
  }
  
  public final int hashCode()
  {
    int code = cachedHashCode;
    if (code == 0) {
      cachedHashCode = (code = element.hashCode());
    }
    return code;
  }
  
  boolean isHashCodeFast() {
    return cachedHashCode != 0;
  }
  
  public String toString() {
    String elementToString = element.toString();
    return elementToString.length() + 2 + '[' + elementToString + ']';
  }
}
