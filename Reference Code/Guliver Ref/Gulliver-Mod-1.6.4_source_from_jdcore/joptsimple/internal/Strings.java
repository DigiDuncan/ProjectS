package joptsimple.internal;




































public final class Strings
{
  public static final String LINE_SEPARATOR = System.getProperty("line.separator");
  










  public static String repeat(char ch, int count)
  {
    StringBuilder buffer = new StringBuilder();
    
    for (int i = 0; i < count; i++) {
      buffer.append(ch);
    }
    return buffer.toString();
  }
  





  public static boolean isNullOrEmpty(String target)
  {
    return (target == null) || ("".equals(target));
  }
  








  public static String surround(String target, char begin, char end)
  {
    return begin + target + end;
  }
}
