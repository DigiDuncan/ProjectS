package org.tukaani.xz.lz;

import java.io.IOException;
import java.io.OutputStream;

public abstract class LZEncoder
{
  private final int keepSizeBefore;
  private final int keepSizeAfter;
  final int matchLenMax;
  final int niceLen;
  final byte[] buf;
  int readPos = -1;
  private int readLimit = -1;
  private boolean finishing = false;
  private int writePos = 0;
  private int pendingSize = 0;
  
  static void normalize(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i] <= paramInt) {
        paramArrayOfInt[i] = 0;
      } else {
        paramArrayOfInt[i] -= paramInt;
      }
    }
  }
  
  private static int getBufSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 + paramInt1;
    int j = paramInt3 + paramInt4;
    int k = Math.min(paramInt1 / 2 + 262144, 536870912);
    return i + j + k;
  }
  
  public static LZEncoder getInstance(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    switch (paramInt6)
    {
    case 4: 
      return new HC4(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt7);
    case 20: 
      return new BT4(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt7);
    }
    throw new IllegalArgumentException();
  }
  
  LZEncoder(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    buf = new byte[getBufSize(paramInt1, paramInt2, paramInt3, paramInt5)];
    keepSizeBefore = (paramInt2 + paramInt1);
    keepSizeAfter = (paramInt3 + paramInt5);
    matchLenMax = paramInt5;
    niceLen = paramInt4;
  }
  
  public void setPresetDict(int paramInt, byte[] paramArrayOfByte)
  {
    assert (!isStarted());
    assert (writePos == 0);
    if (paramArrayOfByte != null)
    {
      int i = Math.min(paramArrayOfByte.length, paramInt);
      int j = paramArrayOfByte.length - i;
      System.arraycopy(paramArrayOfByte, j, buf, 0, i);
      writePos += i;
      skip(i);
    }
  }
  
  private void moveWindow()
  {
    int i = readPos + 1 - keepSizeBefore & 0xFFFFFFF0;
    int j = writePos - i;
    System.arraycopy(buf, i, buf, 0, j);
    readPos -= i;
    readLimit -= i;
    writePos -= i;
  }
  
  public int fillWindow(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    assert (!finishing);
    if (readPos >= buf.length - keepSizeAfter) {
      moveWindow();
    }
    if (paramInt2 > buf.length - writePos) {
      paramInt2 = buf.length - writePos;
    }
    System.arraycopy(paramArrayOfByte, paramInt1, buf, writePos, paramInt2);
    writePos += paramInt2;
    if (writePos >= keepSizeAfter) {
      readLimit = (writePos - keepSizeAfter);
    }
    processPendingBytes();
    return paramInt2;
  }
  
  private void processPendingBytes()
  {
    if ((pendingSize > 0) && (readPos < readLimit))
    {
      readPos -= pendingSize;
      int i = pendingSize;
      pendingSize = 0;
      skip(i);
      assert (pendingSize < i);
    }
  }
  
  public boolean isStarted()
  {
    return readPos != -1;
  }
  
  public void setFlushing()
  {
    readLimit = (writePos - 1);
    processPendingBytes();
  }
  
  public void setFinishing()
  {
    readLimit = (writePos - 1);
    finishing = true;
    processPendingBytes();
  }
  
  public boolean hasEnoughData(int paramInt)
  {
    return readPos - paramInt < readLimit;
  }
  
  public void copyUncompressed(OutputStream paramOutputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    paramOutputStream.write(buf, readPos + 1 - paramInt1, paramInt2);
  }
  
  public int getAvail()
  {
    assert (isStarted());
    return writePos - readPos;
  }
  
  public int getPos()
  {
    return readPos;
  }
  
  public int getByte(int paramInt)
  {
    return buf[(readPos - paramInt)] & 0xFF;
  }
  
  public int getByte(int paramInt1, int paramInt2)
  {
    return buf[(readPos + paramInt1 - paramInt2)] & 0xFF;
  }
  
  public int getMatchLen(int paramInt1, int paramInt2)
  {
    int i = readPos - paramInt1 - 1;
    for (int j = 0; (j < paramInt2) && (buf[(readPos + j)] == buf[(i + j)]); j++) {}
    return j;
  }
  
  public int getMatchLen(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = readPos + paramInt1;
    int j = i - paramInt2 - 1;
    for (int k = 0; (k < paramInt3) && (buf[(i + k)] == buf[(j + k)]); k++) {}
    return k;
  }
  
  public boolean verifyMatches(Matches paramMatches)
  {
    int i = Math.min(getAvail(), matchLenMax);
    for (int j = 0; j < count; j++) {
      if (getMatchLen(dist[j], i) != len[j]) {
        return false;
      }
    }
    return true;
  }
  
  int movePos(int paramInt1, int paramInt2)
  {
    assert (paramInt1 >= paramInt2);
    readPos += 1;
    int i = writePos - readPos;
    if ((i < paramInt1) && ((i < paramInt2) || (!finishing)))
    {
      pendingSize += 1;
      i = 0;
    }
    return i;
  }
  
  public abstract Matches getMatches();
  
  public abstract void skip(int paramInt);
}
