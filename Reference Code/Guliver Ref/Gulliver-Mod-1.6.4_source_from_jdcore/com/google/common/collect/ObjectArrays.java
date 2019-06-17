package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Collection;




























public final class ObjectArrays
{
  static final Object[] EMPTY_ARRAY = new Object[0];
  









  public static <T> T[] newArray(Class<T> type, int length)
  {
    return (Object[])Array.newInstance(type, length);
  }
  






  public static <T> T[] newArray(T[] reference, int length)
  {
    return Platform.newArray(reference, length);
  }
  














































  static <T> T[] arraysCopyOf(T[] original, int newLength)
  {
    T[] copy = newArray(original, newLength);
    System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
    
    return copy;
  }
  























  static <T> T[] toArrayImpl(Collection<?> c, T[] array)
  {
    int size = c.size();
    if (array.length < size) {
      array = newArray(array, size);
    }
    fillArray(c, array);
    if (array.length > size) {
      array[size] = null;
    }
    return array;
  }
  













  static Object[] toArrayImpl(Collection<?> c)
  {
    return fillArray(c, new Object[c.size()]);
  }
  
  private static Object[] fillArray(Iterable<?> elements, Object[] array) {
    int i = 0;
    for (Object element : elements) {
      array[(i++)] = element;
    }
    return array;
  }
  










  static Object checkElementNotNull(Object element, int index)
  {
    if (element == null) {
      throw new NullPointerException("at index " + index);
    }
    return element;
  }
}
