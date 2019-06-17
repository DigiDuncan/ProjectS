package org.tukaani.xz.common;

public class Util
{
  public static int getVLISize(long paramLong)
  {
    int i = 0;
    do
    {
      i++;
      paramLong >>= 7;
    } while (paramLong != 0L);
    return i;
  }
}
