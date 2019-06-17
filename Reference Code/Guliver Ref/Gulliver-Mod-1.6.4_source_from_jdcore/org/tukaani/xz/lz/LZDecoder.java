package org.tukaani.xz.lz;

import java.io.DataInputStream;
import java.io.IOException;
import org.tukaani.xz.CorruptedInputException;

public final class LZDecoder
{
  private final byte[] buf;
  private int start = 0;
  private int pos = 0;
  private int full = 0;
  private int limit = 0;
  private int pendingLen = 0;
  private int pendingDist = 0;
  
  public LZDecoder(int paramInt, byte[] paramArrayOfByte)
  {
    buf = new byte[paramInt];
    if (paramArrayOfByte != null)
    {
      pos = Math.min(paramArrayOfByte.length, paramInt);
      full = pos;
      start = pos;
      System.arraycopy(paramArrayOfByte, paramArrayOfByte.length - pos, buf, 0, pos);
    }
  }
  
  public void reset()
  {
    start = 0;
    pos = 0;
    full = 0;
    limit = 0;
    buf[(buf.length - 1)] = 0;
  }
  
  public void setLimit(int paramInt)
  {
    if (buf.length - pos <= paramInt) {
      limit = buf.length;
    } else {
      limit = (pos + paramInt);
    }
  }
  
  public boolean hasSpace()
  {
    return pos < limit;
  }
  
  public boolean hasPending()
  {
    return pendingLen > 0;
  }
  
  public int getPos()
  {
    return pos;
  }
  
  public int getByte(int paramInt)
  {
    int i = pos - paramInt - 1;
    if (paramInt >= pos) {
      i += buf.length;
    }
    return buf[i] & 0xFF;
  }
  
  public void putByte(byte paramByte)
  {
    buf[(pos++)] = paramByte;
    if (full < pos) {
      full = pos;
    }
  }
  
  public void repeat(int paramInt1, int paramInt2)
    throws IOException
  {
    if ((paramInt1 < 0) || (paramInt1 >= full)) {
      throw new CorruptedInputException();
    }
    int i = Math.min(limit - pos, paramInt2);
    pendingLen = (paramInt2 - i);
    pendingDist = paramInt1;
    int j = pos - paramInt1 - 1;
    if (paramInt1 >= pos) {
      j += buf.length;
    }
    do
    {
      buf[(pos++)] = buf[(j++)];
      if (j == buf.length) {
        j = 0;
      }
      i--;
    } while (i > 0);
    if (full < pos) {
      full = pos;
    }
  }
  
  public void repeatPending()
    throws IOException
  {
    if (pendingLen > 0) {
      repeat(pendingDist, pendingLen);
    }
  }
  
  public void copyUncompressed(DataInputStream paramDataInputStream, int paramInt)
    throws IOException
  {
    int i = Math.min(buf.length - pos, paramInt);
    paramDataInputStream.readFully(buf, pos, i);
    pos += i;
    if (full < pos) {
      full = pos;
    }
  }
  
  public int flush(byte[] paramArrayOfByte, int paramInt)
  {
    int i = pos - start;
    if (pos == buf.length) {
      pos = 0;
    }
    System.arraycopy(buf, start, paramArrayOfByte, paramInt, i);
    start = pos;
    return i;
  }
}
