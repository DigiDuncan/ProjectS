package org.tukaani.xz.simple;

public final class SPARC
  implements SimpleFilter
{
  private final boolean isEncoder;
  private int pos;
  
  public SPARC(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = paramInt;
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2 - 4;
    for (int j = paramInt1; j <= i; j += 4) {
      if (((paramArrayOfByte[j] == 64) && ((paramArrayOfByte[(j + 1)] & 0xC0) == 0)) || ((paramArrayOfByte[j] == Byte.MAX_VALUE) && ((paramArrayOfByte[(j + 1)] & 0xC0) == 192)))
      {
        int k = (paramArrayOfByte[j] & 0xFF) << 24 | (paramArrayOfByte[(j + 1)] & 0xFF) << 16 | (paramArrayOfByte[(j + 2)] & 0xFF) << 8 | paramArrayOfByte[(j + 3)] & 0xFF;
        k <<= 2;
        if (isEncoder) {
          m = k + (pos + j - paramInt1);
        } else {
          m = k - (pos + j - paramInt1);
        }
        m >>>= 2;
        int m = 0 - (m >>> 22 & 0x1) << 22 & 0x3FFFFFFF | m & 0x3FFFFF | 0x40000000;
        paramArrayOfByte[j] = ((byte)(m >>> 24));
        paramArrayOfByte[(j + 1)] = ((byte)(m >>> 16));
        paramArrayOfByte[(j + 2)] = ((byte)(m >>> 8));
        paramArrayOfByte[(j + 3)] = ((byte)m);
      }
    }
    j -= paramInt1;
    pos += j;
    return j;
  }
}
