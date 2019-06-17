package com.google.common.base;

import javax.annotation.Nullable;















































public final class Objects
{
  public static boolean equal(@Nullable Object a, @Nullable Object b)
  {
    return (a == b) || ((a != null) && (a.equals(b)));
  }
}
