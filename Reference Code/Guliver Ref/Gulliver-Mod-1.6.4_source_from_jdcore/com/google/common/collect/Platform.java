package com.google.common.collect;

import java.lang.reflect.Array;














































class Platform
{
  static <T> T[] newArray(T[] reference, int length)
  {
    Class<?> type = reference.getClass().getComponentType();
    



    T[] result = (Object[])Array.newInstance(type, length);
    return result;
  }
}
