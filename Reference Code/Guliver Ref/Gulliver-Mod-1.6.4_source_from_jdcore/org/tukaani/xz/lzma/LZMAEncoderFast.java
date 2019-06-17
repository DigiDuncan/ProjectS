package org.tukaani.xz.lzma;

import org.tukaani.xz.lz.LZEncoder;
import org.tukaani.xz.lz.Matches;
import org.tukaani.xz.rangecoder.RangeEncoder;

final class LZMAEncoderFast
  extends LZMAEncoder
{
  private static int EXTRA_SIZE_BEFORE = 1;
  private static int EXTRA_SIZE_AFTER = 272;
  private Matches matches = null;
  
  LZMAEncoderFast(RangeEncoder paramRangeEncoder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    super(paramRangeEncoder, LZEncoder.getInstance(paramInt4, Math.max(paramInt5, EXTRA_SIZE_BEFORE), EXTRA_SIZE_AFTER, paramInt6, 273, paramInt7, paramInt8), paramInt1, paramInt2, paramInt3, paramInt4, paramInt6);
  }
  
  private boolean changePair(int paramInt1, int paramInt2)
  {
    return paramInt1 < paramInt2 >>> 7;
  }
  
  int getNextSymbol()
  {
    if (readAhead == -1) {
      matches = getMatches();
    }
    back = -1;
    int i = Math.min(lz.getAvail(), 273);
    if (i < 2) {
      return 1;
    }
    int j = 0;
    int k = 0;
    for (int m = 0; m < 4; m++)
    {
      n = lz.getMatchLen(reps[m], i);
      if (n >= 2)
      {
        if (n >= niceLen)
        {
          back = m;
          skip(n - 1);
          return n;
        }
        if (n > j)
        {
          k = m;
          j = n;
        }
      }
    }
    m = 0;
    int n = 0;
    if (matches.count > 0)
    {
      m = matches.len[(matches.count - 1)];
      n = matches.dist[(matches.count - 1)];
      if (m >= niceLen)
      {
        back = (n + 4);
        skip(m - 1);
        return m;
      }
      while ((matches.count > 1) && (m == matches.len[(matches.count - 2)] + 1) && (changePair(matches.dist[(matches.count - 2)], n)))
      {
        matches.count -= 1;
        m = matches.len[(matches.count - 1)];
        n = matches.dist[(matches.count - 1)];
      }
      if ((m == 2) && (n >= 128)) {
        m = 1;
      }
    }
    if ((j >= 2) && ((j + 1 >= m) || ((j + 2 >= m) && (n >= 512)) || ((j + 3 >= m) && (n >= 32768))))
    {
      back = k;
      skip(j - 1);
      return j;
    }
    if ((m < 2) || (i <= 2)) {
      return 1;
    }
    matches = getMatches();
    if (matches.count > 0)
    {
      i1 = matches.len[(matches.count - 1)];
      i2 = matches.dist[(matches.count - 1)];
      if (((i1 >= m) && (i2 < n)) || ((i1 == m + 1) && (!changePair(n, i2))) || (i1 > m + 1) || ((i1 + 1 >= m) && (m >= 3) && (changePair(i2, n)))) {
        return 1;
      }
    }
    int i1 = Math.max(m - 1, 2);
    for (int i2 = 0; i2 < 4; i2++) {
      if (lz.getMatchLen(reps[i2], i1) == i1) {
        return 1;
      }
    }
    back = (n + 4);
    skip(m - 2);
    return m;
  }
}
