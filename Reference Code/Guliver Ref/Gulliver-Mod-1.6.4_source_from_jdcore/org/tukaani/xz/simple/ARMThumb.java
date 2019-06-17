package org.tukaani.xz.simple;

public final class ARMThumb
  implements SimpleFilter
{
  private final boolean isEncoder;
  private int pos;
  
  public ARMThumb(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = (paramInt + 4);
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2 - 4;
    for (int j = paramInt1; j <= i; j += 2) {
      if (((paramArrayOfByte[(j + 1)] & 0xF8) == 240) && ((paramArrayOfByte[(j + 3)] & 0xF8) == 248))
      {
        int k = (paramArrayOfByte[(j + 1)] & 0x7) << 19 | (paramArrayOfByte[j] & 0xFF) << 11 | (paramArrayOfByte[(j + 3)] & 0x7) << 8 | paramArrayOfByte[(j + 2)] & 0xFF;
        k <<= 1;
        int m;
        if (isEncoder) {
          m = k + (pos + j - paramInt1);
        } else {
          m = k - (pos + j - paramInt1);
        }
        m >>>= 1;
        paramArrayOfByte[(j + 1)] = ((byte)(0xF0 | m >>> 19 & 0x7));
        paramArrayOfByte[j] = ((byte)(m >>> 11));
        paramArrayOfByte[(j + 3)] = ((byte)(0xF8 | m >>> 8 & 0x7));
        paramArrayOfByte[(j + 2)] = ((byte)m);
        j += 2;
      }
    }
    j -= paramInt1;
    pos += j;
    return j;
  }
}
