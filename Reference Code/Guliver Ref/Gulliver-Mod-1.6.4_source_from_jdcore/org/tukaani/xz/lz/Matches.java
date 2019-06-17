package org.tukaani.xz.lz;

public final class Matches
{
  public final int[] len;
  public final int[] dist;
  public int count = 0;
  
  Matches(int paramInt)
  {
    len = new int[paramInt];
    dist = new int[paramInt];
  }
}
