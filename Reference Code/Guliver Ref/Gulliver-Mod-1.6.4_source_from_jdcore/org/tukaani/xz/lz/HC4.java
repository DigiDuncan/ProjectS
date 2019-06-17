package org.tukaani.xz.lz;

final class HC4
  extends LZEncoder
{
  private final Hash234 hash;
  private final int[] chain;
  private final Matches matches;
  private final int depthLimit;
  private final int cyclicSize;
  private int cyclicPos = -1;
  private int lzPos;
  
  HC4(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    hash = new Hash234(paramInt1);
    cyclicSize = (paramInt1 + 1);
    chain = new int[cyclicSize];
    lzPos = cyclicSize;
    matches = new Matches(paramInt4 - 1);
    depthLimit = (paramInt6 > 0 ? paramInt6 : 4 + paramInt4 / 4);
  }
  
  private int movePos()
  {
    int i = movePos(4, 4);
    if (i != 0)
    {
      if (++lzPos == Integer.MAX_VALUE)
      {
        int j = Integer.MAX_VALUE - cyclicSize;
        hash.normalize(j);
        normalize(chain, j);
        lzPos -= j;
      }
      if (++cyclicPos == cyclicSize) {
        cyclicPos = 0;
      }
    }
    return i;
  }
  
  public Matches getMatches()
  {
    matches.count = 0;
    int i = matchLenMax;
    int j = niceLen;
    int k = movePos();
    if (k < i)
    {
      if (k == 0) {
        return matches;
      }
      i = k;
      if (j > k) {
        j = k;
      }
    }
    hash.calcHashes(buf, readPos);
    int m = lzPos - hash.getHash2Pos();
    int n = lzPos - hash.getHash3Pos();
    int i1 = hash.getHash4Pos();
    hash.updateTables(lzPos);
    chain[cyclicPos] = i1;
    int i2 = 0;
    if ((m < cyclicSize) && (buf[(readPos - m)] == buf[readPos]))
    {
      i2 = 2;
      matches.len[0] = 2;
      matches.dist[0] = (m - 1);
      matches.count = 1;
    }
    if ((m != n) && (n < cyclicSize) && (buf[(readPos - n)] == buf[readPos]))
    {
      i2 = 3;
      matches.dist[(matches.count++)] = (n - 1);
      m = n;
    }
    if (matches.count > 0)
    {
      while ((i2 < i) && (buf[(readPos + i2 - m)] == buf[(readPos + i2)])) {
        i2++;
      }
      matches.len[(matches.count - 1)] = i2;
      if (i2 >= j) {
        return matches;
      }
    }
    if (i2 < 3) {
      i2 = 3;
    }
    int i3 = depthLimit;
    for (;;)
    {
      int i4 = lzPos - i1;
      if ((i3-- == 0) || (i4 >= cyclicSize)) {
        return matches;
      }
      i1 = chain[(cyclicPos - i4 + 0)];
      if ((buf[(readPos + i2 - i4)] == buf[(readPos + i2)]) && (buf[(readPos - i4)] == buf[readPos]))
      {
        int i5 = 0;
        for (;;)
        {
          i5++;
          if (i5 < i) {
            if (buf[(readPos + i5 - i4)] != buf[(readPos + i5)]) {
              break;
            }
          }
        }
        if (i5 > i2)
        {
          i2 = i5;
          matches.len[matches.count] = i5;
          matches.dist[matches.count] = (i4 - 1);
          matches.count += 1;
          if (i5 >= j) {
            return matches;
          }
        }
      }
    }
  }
  
  public void skip(int paramInt)
  {
    assert (paramInt >= 0);
    while (paramInt-- > 0) {
      if (movePos() != 0)
      {
        hash.calcHashes(buf, readPos);
        chain[cyclicPos] = hash.getHash4Pos();
        hash.updateTables(lzPos);
      }
    }
  }
}
