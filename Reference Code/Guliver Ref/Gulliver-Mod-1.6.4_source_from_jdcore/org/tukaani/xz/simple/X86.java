package org.tukaani.xz.simple;

public final class X86
  implements SimpleFilter
{
  private static final boolean[] MASK_TO_ALLOWED_STATUS = { true, true, true, false, true, false, false, false };
  private static final int[] MASK_TO_BIT_NUMBER = { 0, 1, 2, 2, 3, 3, 3, 3 };
  private final boolean isEncoder;
  private int pos;
  private int prevMask = 0;
  
  private static boolean test86MSByte(byte paramByte)
  {
    int i = paramByte & 0xFF;
    return (i == 0) || (i == 255);
  }
  
  public X86(boolean paramBoolean, int paramInt)
  {
    isEncoder = paramBoolean;
    pos = (paramInt + 5);
  }
  
  public int code(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 - 1;
    int j = paramInt1 + paramInt2 - 5;
    for (int k = paramInt1; k <= j; k++) {
      if ((paramArrayOfByte[k] & 0xFE) == 232)
      {
        i = k - i;
        if ((i & 0xFFFFFFFC) != 0)
        {
          prevMask = 0;
        }
        else
        {
          prevMask = (prevMask << i - 1 & 0x7);
          if ((prevMask != 0) && ((MASK_TO_ALLOWED_STATUS[prevMask] == 0) || (test86MSByte(paramArrayOfByte[(k + 4 - MASK_TO_BIT_NUMBER[prevMask])]))))
          {
            i = k;
            prevMask = (prevMask << 1 | 0x1);
            continue;
          }
        }
        i = k;
        if (test86MSByte(paramArrayOfByte[(k + 4)]))
        {
          int n;
          int i1;
          for (int m = paramArrayOfByte[(k + 1)] & 0xFF | (paramArrayOfByte[(k + 2)] & 0xFF) << 8 | (paramArrayOfByte[(k + 3)] & 0xFF) << 16 | (paramArrayOfByte[(k + 4)] & 0xFF) << 24;; m = n ^ (1 << 32 - i1) - 1)
          {
            if (isEncoder) {
              n = m + (pos + k - paramInt1);
            } else {
              n = m - (pos + k - paramInt1);
            }
            if (prevMask == 0) {
              break;
            }
            i1 = MASK_TO_BIT_NUMBER[prevMask] * 8;
            if (!test86MSByte((byte)(n >>> 24 - i1))) {
              break;
            }
          }
          paramArrayOfByte[(k + 1)] = ((byte)n);
          paramArrayOfByte[(k + 2)] = ((byte)(n >>> 8));
          paramArrayOfByte[(k + 3)] = ((byte)(n >>> 16));
          paramArrayOfByte[(k + 4)] = ((byte)((n >>> 24 & 0x1) - 1 ^ 0xFFFFFFFF));
          k += 4;
        }
        else
        {
          prevMask = (prevMask << 1 | 0x1);
        }
      }
    }
    i = k - i;
    prevMask = ((i & 0xFFFFFFFC) != 0 ? 0 : prevMask << i - 1);
    k -= paramInt1;
    pos += k;
    return k;
  }
}
