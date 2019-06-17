package org.tukaani.xz.simple;

public final class ARM
  implements SimpleFilter
{
  private final boolean isEncoder;
  private int pos;
  
  public ARM(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = (paramInt + 8);
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2 - 4;
    for (int j = paramInt1; j <= i; j += 4) {
      if ((paramArrayOfByte[(j + 3)] & 0xFF) == 235)
      {
        int k = (paramArrayOfByte[(j + 2)] & 0xFF) << 16 | (paramArrayOfByte[(j + 1)] & 0xFF) << 8 | paramArrayOfByte[j] & 0xFF;
        k <<= 2;
        int m;
        if (isEncoder) {
          m = k + (pos + j - paramInt1);
        } else {
          m = k - (pos + j - paramInt1);
        }
        m >>>= 2;
        paramArrayOfByte[(j + 2)] = ((byte)(m >>> 16));
        paramArrayOfByte[(j + 1)] = ((byte)(m >>> 8));
        paramArrayOfByte[j] = ((byte)m);
      }
    }
    j -= paramInt1;
    pos += j;
    return j;
  }
}
