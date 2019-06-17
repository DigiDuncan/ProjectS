package joptsimple.internal;

import java.util.HashMap;
import java.util.Map;




























public final class Classes
{
  private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap(13);
  
  static {
    WRAPPERS.put(Boolean.TYPE, Boolean.class);
    WRAPPERS.put(Byte.TYPE, Byte.class);
    WRAPPERS.put(Character.TYPE, Character.class);
    WRAPPERS.put(Double.TYPE, Double.class);
    WRAPPERS.put(Float.TYPE, Float.class);
    WRAPPERS.put(Integer.TYPE, Integer.class);
    WRAPPERS.put(Long.TYPE, Long.class);
    WRAPPERS.put(Short.TYPE, Short.class);
    WRAPPERS.put(Void.TYPE, Void.class);
  }
  









  public static String shortNameOf(String className)
  {
    return className.substring(className.lastIndexOf('.') + 1);
  }
}
