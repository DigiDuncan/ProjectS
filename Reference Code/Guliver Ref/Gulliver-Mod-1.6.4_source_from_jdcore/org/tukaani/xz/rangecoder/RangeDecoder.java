package org.tukaani.xz.rangecoder;

import java.io.DataInputStream;
import java.io.IOException;
import org.tukaani.xz.CorruptedInputException;

public final class RangeDecoder
  extends RangeCoder
{
  private final byte[] buf;
  private int pos = 0;
  private int end = 0;
  private int range = 0;
  private int code = 0;
  
  public RangeDecoder(int paramInt)
  {
    buf = new byte[paramInt - 5];
  }
  
  public void prepareInputBuffer(DataInputStream paramDataInputStream, int paramInt)
    throws IOException
  {
    if (paramInt < 5) {
      throw new CorruptedInputException();
    }
    if (paramDataInputStream.readUnsignedByte() != 0) {
      throw new CorruptedInputException();
    }
    code = paramDataInputStream.readInt();
    range = -1;
    pos = 0;
    end = (paramInt - 5);
    paramDataInputStream.readFully(buf, 0, end);
  }
  
  public boolean isInBufferOK()
  {
    return pos <= end;
  }
  
  public boolean isFinished()
  {
    return (pos == end) && (code == 0);
  }
  
  public void normalize()
    throws IOException
  {
    if ((range & 0xFF000000) == 0) {
      try
      {
        code = (code << 8 | buf[(pos++)] & 0xFF);
        range <<= 8;
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
        throw new CorruptedInputException();
      }
    }
  }
  
  public int decodeBit(short[] paramArrayOfShort, int paramInt)
    throws IOException
  {
    normalize();
    int i = paramArrayOfShort[paramInt];
    int j = (range >>> 11) * i;
    int k;
    if ((code ^ 0x80000000) < (j ^ 0x80000000))
    {
      range = j;
      paramArrayOfShort[paramInt] = ((short)(i + (2048 - i >>> 5)));
      k = 0;
    }
    else
    {
      range -= j;
      code -= j;
      paramArrayOfShort[paramInt] = ((short)(i - (i >>> 5)));
      k = 1;
    }
    return k;
  }
  
  public int decodeBitTree(short[] paramArrayOfShort)
    throws IOException
  {
    int i = 1;
    do
    {
      i = i << 1 | decodeBit(paramArrayOfShort, i);
    } while (i < paramArrayOfShort.length);
    return i - paramArrayOfShort.length;
  }
  
  public int decodeReverseBitTree(short[] paramArrayOfShort)
    throws IOException
  {
    int i = 1;
    int j = 0;
    int k = 0;
    do
    {
      int m = decodeBit(paramArrayOfShort, i);
      i = i << 1 | m;
      k |= m << j++;
    } while (i < paramArrayOfShort.length);
    return k;
  }
  
  public int decodeDirectBits(int paramInt)
    throws IOException
  {
    int i = 0;
    do
    {
      normalize();
      range >>>= 1;
      int j = code - range >>> 31;
      code -= (range & j - 1);
      i = i << 1 | 1 - j;
      paramInt--;
    } while (paramInt != 0);
    return i;
  }
}
