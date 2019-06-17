package org.tukaani.xz.rangecoder;

import java.util.Arrays;

public abstract class RangeCoder
{
  public RangeCoder() {}
  
  public static final void initProbs(short[] paramArrayOfShort)
  {
    Arrays.fill(paramArrayOfShort, (short)1024);
  }
}
