package org.tukaani.xz.simple;

public final class IA64
  implements SimpleFilter
{
  private static final int[] BRANCH_TABLE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 6, 6, 0, 0, 7, 7, 4, 4, 0, 0, 4, 4, 0, 0 };
  private final boolean isEncoder;
  private int pos;
  
  public IA64(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = paramInt;
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2 - 16;
    for (int j = paramInt1; j <= i; j += 16)
    {
      int k = paramArrayOfByte[j] & 0x1F;
      int m = BRANCH_TABLE[k];
      int n = 0;
      for (int i1 = 5; n < 3; i1 += 41)
      {
        if ((m >>> n & 0x1) != 0)
        {
          int i2 = i1 >>> 3;
          int i3 = i1 & 0x7;
          long l1 = 0L;
          for (int i4 = 0; i4 < 6; i4++) {
            l1 |= (paramArrayOfByte[(j + i2 + i4)] & 0xFF) << 8 * i4;
          }
          long l2 = l1 >>> i3;
          if (((l2 >>> 37 & 0xF) == 5L) && ((l2 >>> 9 & 0x7) == 0L))
          {
            int i5 = (int)(l2 >>> 13 & 0xFFFFF);
            i5 |= ((int)(l2 >>> 36) & 0x1) << 20;
            i5 <<= 4;
            int i6;
            if (isEncoder) {
              i6 = i5 + (pos + j - paramInt1);
            } else {
              i6 = i5 - (pos + j - paramInt1);
            }
            i6 >>>= 4;
            l2 &= 0xFFFFFFEE00001FFF;
            l2 |= (i6 & 0xFFFFF) << 13;
            l2 |= (i6 & 0x100000) << 16;
            l1 &= (1 << i3) - 1;
            l1 |= l2 << i3;
            for (int i7 = 0; i7 < 6; i7++) {
              paramArrayOfByte[(j + i2 + i7)] = ((byte)(int)(l1 >>> 8 * i7));
            }
          }
        }
        n++;
      }
    }
    j -= paramInt1;
    pos += j;
    return j;
  }
}
