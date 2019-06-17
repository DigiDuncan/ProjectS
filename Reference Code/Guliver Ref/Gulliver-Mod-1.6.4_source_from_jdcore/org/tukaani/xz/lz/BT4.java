package org.tukaani.xz.lz;

final class BT4
  extends LZEncoder
{
  private final Hash234 hash;
  private final int[] tree;
  private final Matches matches;
  private final int depthLimit;
  private final int cyclicSize;
  private int cyclicPos = -1;
  private int lzPos;
  
  BT4(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    cyclicSize = (paramInt1 + 1);
    lzPos = cyclicSize;
    hash = new Hash234(paramInt1);
    tree = new int[cyclicSize * 2];
    matches = new Matches(paramInt4 - 1);
    depthLimit = (paramInt6 > 0 ? paramInt6 : 16 + paramInt4 / 2);
  }
  
  private int movePos()
  {
    int i = movePos(niceLen, 4);
    if (i != 0)
    {
      if (++lzPos == Integer.MAX_VALUE)
      {
        int j = Integer.MAX_VALUE - cyclicSize;
        hash.normalize(j);
        normalize(tree, j);
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
      if (i2 >= j)
      {
        skip(j, i1);
        return matches;
      }
    }
    if (i2 < 3) {
      i2 = 3;
    }
    int i3 = depthLimit;
    int i4 = (cyclicPos << 1) + 1;
    int i5 = cyclicPos << 1;
    int i6 = 0;
    int i7 = 0;
    for (;;)
    {
      int i8 = lzPos - i1;
      if ((i3-- == 0) || (i8 >= cyclicSize))
      {
        tree[i4] = 0;
        tree[i5] = 0;
        return matches;
      }
      int i9 = cyclicPos - i8 + (i8 > cyclicPos ? cyclicSize : 0) << 1;
      int i10 = Math.min(i6, i7);
      if (buf[(readPos + i10 - i8)] == buf[(readPos + i10)])
      {
        for (;;)
        {
          i10++;
          if (i10 < i) {
            if (buf[(readPos + i10 - i8)] != buf[(readPos + i10)]) {
              break;
            }
          }
        }
        if (i10 > i2)
        {
          i2 = i10;
          matches.len[matches.count] = i10;
          matches.dist[matches.count] = (i8 - 1);
          matches.count += 1;
          if (i10 >= j)
          {
            tree[i5] = tree[i9];
            tree[i4] = tree[(i9 + 1)];
            return matches;
          }
        }
      }
      if ((buf[(readPos + i10 - i8)] & 0xFF) < (buf[(readPos + i10)] & 0xFF))
      {
        tree[i5] = i1;
        i5 = i9 + 1;
        i1 = tree[i5];
        i7 = i10;
      }
      else
      {
        tree[i4] = i1;
        i4 = i9;
        i1 = tree[i4];
        i6 = i10;
      }
    }
  }
  
  private void skip(int paramInt1, int paramInt2)
  {
    int i = depthLimit;
    int j = (cyclicPos << 1) + 1;
    int k = cyclicPos << 1;
    int m = 0;
    int n = 0;
    for (;;)
    {
      int i1 = lzPos - paramInt2;
      if ((i-- == 0) || (i1 >= cyclicSize))
      {
        tree[j] = 0;
        tree[k] = 0;
        return;
      }
      int i2 = cyclicPos - i1 + (i1 > cyclicPos ? cyclicSize : 0) << 1;
      int i3 = Math.min(m, n);
      if (buf[(readPos + i3 - i1)] == buf[(readPos + i3)]) {
        do
        {
          i3++;
          if (i3 == paramInt1)
          {
            tree[k] = tree[i2];
            tree[j] = tree[(i2 + 1)];
            return;
          }
        } while (buf[(readPos + i3 - i1)] == buf[(readPos + i3)]);
      }
      if ((buf[(readPos + i3 - i1)] & 0xFF) < (buf[(readPos + i3)] & 0xFF))
      {
        tree[k] = paramInt2;
        k = i2 + 1;
        paramInt2 = tree[k];
        n = i3;
      }
      else
      {
        tree[j] = paramInt2;
        j = i2;
        paramInt2 = tree[j];
        m = i3;
      }
    }
  }
  
  public void skip(int paramInt)
  {
    while (paramInt-- > 0)
    {
      int i = niceLen;
      int j = movePos();
      if (j < i)
      {
        if (j != 0) {
          i = j;
        }
      }
      else
      {
        hash.calcHashes(buf, readPos);
        int k = hash.getHash4Pos();
        hash.updateTables(lzPos);
        skip(i, k);
      }
    }
  }
}
