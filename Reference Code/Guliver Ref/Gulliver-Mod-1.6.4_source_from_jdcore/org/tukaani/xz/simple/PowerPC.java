package org.tukaani.xz.simple;

public final class PowerPC
  implements SimpleFilter
{
  private final boolean isEncoder;
  private int pos;
  
  public PowerPC(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = paramInt;
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2 - 4;
    for (int j = paramInt1; j <= i; j += 4) {
      if (((paramArrayOfByte[j] & 0xFC) == 72) && ((paramArrayOfByte[(j + 3)] & 0x3) == 1))
      {
        int k = (paramArrayOfByte[j] & 0x3) << 24 | (paramArrayOfByte[(j + 1)] & 0xFF) << 16 | (paramArrayOfByte[(j + 2)] & 0xFF) << 8 | paramArrayOfByte[(j + 3)] & 0xFC;
        int m;
        if (isEncoder) {
          m = k + (pos + j - paramInt1);
        } else {
          m = k - (pos + j - paramInt1);
        }
        paramArrayOfByte[j] = ((byte)(0x48 | m >>> 24 & 0x3));
        paramArrayOfByte[(j + 1)] = ((byte)(m >>> 16));
        paramArrayOfByte[(j + 2)] = ((byte)(m >>> 8));
        paramArrayOfByte[(j + 3)] = ((byte)(paramArrayOfByte[(j + 3)] & 0x3 | m));
      }
    }
    j -= paramInt1;
    pos += j;
    return j;
  }
}
