package org.tukaani.xz.rangecoder;

import java.io.IOException;
import java.io.OutputStream;

public final class RangeEncoder
  extends RangeCoder
{
  private static final int[] prices;
  private long low;
  private int range;
  private int cacheSize;
  private byte cache;
  private final byte[] buf;
  private int bufPos;
  
  public RangeEncoder(int paramInt)
  {
    buf = new byte[paramInt];
    reset();
  }
  
  public void reset()
  {
    low = 0L;
    range = -1;
    cache = 0;
    cacheSize = 1;
    bufPos = 0;
  }
  
  public int getPendingSize()
  {
    return bufPos + cacheSize + 5 - 1;
  }
  
  public int finish()
  {
    for (int i = 0; i < 5; i++) {
      shiftLow();
    }
    return bufPos;
  }
  
  public void write(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(buf, 0, bufPos);
  }
  
  private void shiftLow()
  {
    int i = (int)(low >>> 32);
    if ((i != 0) || (low < 4278190080L))
    {
      int j = cache;
      do
      {
        buf[(bufPos++)] = ((byte)(j + i));
        j = 255;
      } while (--cacheSize != 0);
      cache = ((byte)(int)(low >>> 24));
    }
    cacheSize += 1;
    low = ((low & 0xFFFFFF) << 8);
  }
  
  public void encodeBit(short[] paramArrayOfShort, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfShort[paramInt1];
    int j = (range >>> 11) * i;
    if (paramInt2 == 0)
    {
      range = j;
      paramArrayOfShort[paramInt1] = ((short)(i + (2048 - i >>> 5)));
    }
    else
    {
      low += (j & 0xFFFFFFFF);
      range -= j;
      paramArrayOfShort[paramInt1] = ((short)(i - (i >>> 5)));
    }
    if ((range & 0xFF000000) == 0)
    {
      range <<= 8;
      shiftLow();
    }
  }
  
  public static int getBitPrice(int paramInt1, int paramInt2)
  {
    assert ((paramInt2 == 0) || (paramInt2 == 1));
    return prices[((paramInt1 ^ -paramInt2 & 0x7FF) >>> 4)];
  }
  
  public void encodeBitTree(short[] paramArrayOfShort, int paramInt)
  {
    int i = 1;
    int j = paramArrayOfShort.length;
    do
    {
      j >>>= 1;
      int k = paramInt & j;
      encodeBit(paramArrayOfShort, i, k);
      i <<= 1;
      if (k != 0) {
        i |= 0x1;
      }
    } while (j != 1);
  }
  
  public static int getBitTreePrice(short[] paramArrayOfShort, int paramInt)
  {
    int i = 0;
    paramInt |= paramArrayOfShort.length;
    do
    {
      int j = paramInt & 0x1;
      paramInt >>>= 1;
      i += getBitPrice(paramArrayOfShort[paramInt], j);
    } while (paramInt != 1);
    return i;
  }
  
  public void encodeReverseBitTree(short[] paramArrayOfShort, int paramInt)
  {
    int i = 1;
    paramInt |= paramArrayOfShort.length;
    do
    {
      int j = paramInt & 0x1;
      paramInt >>>= 1;
      encodeBit(paramArrayOfShort, i, j);
      i = i << 1 | j;
    } while (paramInt != 1);
  }
  
  public static int getReverseBitTreePrice(short[] paramArrayOfShort, int paramInt)
  {
    int i = 0;
    int j = 1;
    paramInt |= paramArrayOfShort.length;
    do
    {
      int k = paramInt & 0x1;
      paramInt >>>= 1;
      i += getBitPrice(paramArrayOfShort[j], k);
      j = j << 1 | k;
    } while (paramInt != 1);
    return i;
  }
  
  public void encodeDirectBits(int paramInt1, int paramInt2)
  {
    do
    {
      range >>>= 1;
      low += (range & 0 - (paramInt1 >>> --paramInt2 & 0x1));
      if ((range & 0xFF000000) == 0)
      {
        range <<= 8;
        shiftLow();
      }
    } while (paramInt2 != 0);
  }
  
  public static int getDirectBitsPrice(int paramInt)
  {
    return paramInt << 4;
  }
  
  static
  {
    prices = new int[''];
    for (int i = 8; i < 2048; i += 16)
    {
      int j = i;
      int k = 0;
      for (int m = 0; m < 4; m++)
      {
        j *= j;
        k <<= 1;
        while ((j & 0xFFFF0000) != 0)
        {
          j >>>= 1;
          k++;
        }
      }
      prices[(i >> 4)] = (161 - k);
    }
  }
}
