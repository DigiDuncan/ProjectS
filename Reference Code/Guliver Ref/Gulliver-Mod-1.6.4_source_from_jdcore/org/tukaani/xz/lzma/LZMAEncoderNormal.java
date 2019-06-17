package org.tukaani.xz.lzma;

import org.tukaani.xz.lz.LZEncoder;
import org.tukaani.xz.lz.Matches;
import org.tukaani.xz.rangecoder.RangeEncoder;

final class LZMAEncoderNormal
  extends LZMAEncoder
{
  private static int EXTRA_SIZE_BEFORE = 4096;
  private static int EXTRA_SIZE_AFTER = 4096;
  private final Optimum[] opts = new Optimum['က'];
  private int optCur = 0;
  private int optEnd = 0;
  private Matches matches;
  private final int[] repLens = new int[4];
  private final State nextState = new State();
  
  LZMAEncoderNormal(RangeEncoder paramRangeEncoder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    super(paramRangeEncoder, LZEncoder.getInstance(paramInt4, Math.max(paramInt5, EXTRA_SIZE_BEFORE), EXTRA_SIZE_AFTER, paramInt6, 273, paramInt7, paramInt8), paramInt1, paramInt2, paramInt3, paramInt4, paramInt6);
    for (int i = 0; i < 4096; i++) {
      opts[i] = new Optimum();
    }
  }
  
  public void reset()
  {
    optCur = 0;
    optEnd = 0;
    super.reset();
  }
  
  private int convertOpts()
  {
    optEnd = optCur;
    int i = opts[optCur].optPrev;
    do
    {
      Optimum localOptimum = opts[optCur];
      if (prev1IsLiteral)
      {
        opts[i].optPrev = optCur;
        opts[i].backPrev = -1;
        optCur = (i--);
        if (hasPrev2)
        {
          opts[i].optPrev = (i + 1);
          opts[i].backPrev = backPrev2;
          optCur = i;
          i = optPrev2;
        }
      }
      int j = opts[i].optPrev;
      opts[i].optPrev = optCur;
      optCur = i;
      i = j;
    } while (optCur > 0);
    optCur = opts[0].optPrev;
    back = opts[optCur].backPrev;
    return optCur;
  }
  
  int getNextSymbol()
  {
    if (optCur < optEnd)
    {
      i = opts[optCur].optPrev - optCur;
      optCur = opts[optCur].optPrev;
      back = opts[optCur].backPrev;
      return i;
    }
    assert (optCur == optEnd);
    optCur = 0;
    optEnd = 0;
    back = -1;
    if (readAhead == -1) {
      matches = getMatches();
    }
    int i = Math.min(lz.getAvail(), 273);
    if (i < 2) {
      return 1;
    }
    int j = 0;
    for (int k = 0; k < 4; k++)
    {
      repLens[k] = lz.getMatchLen(reps[k], i);
      if (repLens[k] < 2) {
        repLens[k] = 0;
      } else if (repLens[k] > repLens[j]) {
        j = k;
      }
    }
    if (repLens[j] >= niceLen)
    {
      back = j;
      skip(repLens[j] - 1);
      return repLens[j];
    }
    k = 0;
    int m = 0;
    if (matches.count > 0)
    {
      k = matches.len[(matches.count - 1)];
      m = matches.dist[(matches.count - 1)];
      if (k >= niceLen)
      {
        back = (m + 4);
        skip(k - 1);
        return k;
      }
    }
    int n = lz.getByte(0);
    int i1 = lz.getByte(reps[0] + 1);
    if ((k < 2) && (n != i1) && (repLens[j] < 2)) {
      return 1;
    }
    int i2 = lz.getPos();
    int i3 = i2 & posMask;
    int i4 = lz.getByte(1);
    int i5 = literalEncoder.getPrice(n, i1, i4, i2, state);
    opts[1].set1(i5, 0, -1);
    i4 = getAnyMatchPrice(state, i3);
    i5 = getAnyRepPrice(i4, state);
    if (i1 == n)
    {
      i6 = getShortRepPrice(i5, state, i3);
      if (i6 < opts[1].price) {
        opts[1].set1(i6, 0, 0);
      }
    }
    optEnd = Math.max(k, repLens[j]);
    if (optEnd < 2)
    {
      assert (optEnd == 0) : optEnd;
      back = opts[1].backPrev;
      return 1;
    }
    updatePrices();
    opts[0].state.set(state);
    System.arraycopy(reps, 0, opts[0].reps, 0, 4);
    for (int i6 = optEnd; i6 >= 2; i6--) {
      opts[i6].reset();
    }
    int i7;
    int i8;
    int i9;
    for (i6 = 0; i6 < 4; i6++)
    {
      i7 = repLens[i6];
      if (i7 >= 2)
      {
        i8 = getLongRepPrice(i5, i6, state, i3);
        do
        {
          i9 = i8 + repLenEncoder.getPrice(i7, i3);
          if (i9 < opts[i7].price) {
            opts[i7].set1(i9, 0, i6);
          }
          i7--;
        } while (i7 >= 2);
      }
    }
    i6 = Math.max(repLens[0] + 1, 2);
    if (i6 <= k)
    {
      i7 = getNormalMatchPrice(i4, state);
      for (i8 = 0; i6 > matches.len[i8]; i8++) {}
      for (;;)
      {
        i9 = matches.dist[i8];
        int i10 = getMatchAndLenPrice(i7, i9, i6, i3);
        if (i10 < opts[i6].price) {
          opts[i6].set1(i10, 0, i9 + 4);
        }
        if (i6 == matches.len[i8])
        {
          i8++;
          if (i8 == matches.count) {
            break;
          }
        }
        i6++;
      }
    }
    i = Math.min(lz.getAvail(), 4095);
    while (++optCur < optEnd)
    {
      matches = getMatches();
      if ((matches.count > 0) && (matches.len[(matches.count - 1)] >= niceLen)) {
        break;
      }
      i--;
      i2++;
      i3 = i2 & posMask;
      updateOptStateAndReps();
      i4 = opts[optCur].price + getAnyMatchPrice(opts[optCur].state, i3);
      i5 = getAnyRepPrice(i4, opts[optCur].state);
      calc1BytePrices(i2, i3, i, i5);
      if (i >= 2)
      {
        i6 = calcLongRepPrices(i2, i3, i, i5);
        if (matches.count > 0) {
          calcNormalMatchPrices(i2, i3, i, i4, i6);
        }
      }
    }
    return convertOpts();
  }
  
  private void updateOptStateAndReps()
  {
    int i = opts[optCur].optPrev;
    assert (i < optCur);
    if (opts[optCur].prev1IsLiteral)
    {
      i--;
      if (opts[optCur].hasPrev2)
      {
        opts[optCur].state.set(opts[opts[optCur].optPrev2].state);
        if (opts[optCur].backPrev2 < 4) {
          opts[optCur].state.updateLongRep();
        } else {
          opts[optCur].state.updateMatch();
        }
      }
      else
      {
        opts[optCur].state.set(opts[i].state);
      }
      opts[optCur].state.updateLiteral();
    }
    else
    {
      opts[optCur].state.set(opts[i].state);
    }
    if (i == optCur - 1)
    {
      assert ((opts[optCur].backPrev == 0) || (opts[optCur].backPrev == -1));
      if (opts[optCur].backPrev == 0) {
        opts[optCur].state.updateShortRep();
      } else {
        opts[optCur].state.updateLiteral();
      }
      System.arraycopy(opts[i].reps, 0, opts[optCur].reps, 0, 4);
    }
    else
    {
      int j;
      if ((opts[optCur].prev1IsLiteral) && (opts[optCur].hasPrev2))
      {
        i = opts[optCur].optPrev2;
        j = opts[optCur].backPrev2;
        opts[optCur].state.updateLongRep();
      }
      else
      {
        j = opts[optCur].backPrev;
        if (j < 4) {
          opts[optCur].state.updateLongRep();
        } else {
          opts[optCur].state.updateMatch();
        }
      }
      if (j < 4)
      {
        opts[optCur].reps[0] = opts[i].reps[j];
        for (int k = 1; k <= j; k++) {
          opts[optCur].reps[k] = opts[i].reps[(k - 1)];
        }
        while (k < 4)
        {
          opts[optCur].reps[k] = opts[i].reps[k];
          k++;
        }
      }
      else
      {
        opts[optCur].reps[0] = (j - 4);
        System.arraycopy(opts[i].reps, 0, opts[optCur].reps, 1, 3);
      }
    }
  }
  
  private void calc1BytePrices(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    int j = lz.getByte(0);
    int k = lz.getByte(opts[optCur].reps[0] + 1);
    int m = opts[optCur].price + literalEncoder.getPrice(j, k, lz.getByte(1), paramInt1, opts[optCur].state);
    if (m < opts[(optCur + 1)].price)
    {
      opts[(optCur + 1)].set1(m, optCur, -1);
      i = 1;
    }
    int n;
    if ((k == j) && ((opts[(optCur + 1)].optPrev == optCur) || (opts[(optCur + 1)].backPrev != 0)))
    {
      n = getShortRepPrice(paramInt4, opts[optCur].state, paramInt2);
      if (n <= opts[(optCur + 1)].price)
      {
        opts[(optCur + 1)].set1(n, optCur, 0);
        i = 1;
      }
    }
    if ((i == 0) && (k != j) && (paramInt3 > 2))
    {
      n = Math.min(niceLen, paramInt3 - 1);
      int i1 = lz.getMatchLen(1, opts[optCur].reps[0], n);
      if (i1 >= 2)
      {
        nextState.set(opts[optCur].state);
        nextState.updateLiteral();
        int i2 = paramInt1 + 1 & posMask;
        int i3 = m + getLongRepAndLenPrice(0, i1, nextState, i2);
        int i4 = optCur + 1 + i1;
        while (optEnd < i4) {
          opts[(++optEnd)].reset();
        }
        if (i3 < opts[i4].price) {
          opts[i4].set2(i3, optCur, 0);
        }
      }
    }
  }
  
  private int calcLongRepPrices(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 2;
    int j = Math.min(paramInt3, niceLen);
    for (int k = 0; k < 4; k++)
    {
      int m = lz.getMatchLen(opts[optCur].reps[k], j);
      if (m >= 2)
      {
        while (optEnd < optCur + m) {
          opts[(++optEnd)].reset();
        }
        int n = getLongRepPrice(paramInt4, k, opts[optCur].state, paramInt2);
        for (int i1 = m; i1 >= 2; i1--)
        {
          i2 = n + repLenEncoder.getPrice(i1, paramInt2);
          if (i2 < opts[(optCur + i1)].price) {
            opts[(optCur + i1)].set1(i2, optCur, k);
          }
        }
        if (k == 0) {
          i = m + 1;
        }
        i1 = Math.min(niceLen, paramInt3 - m - 1);
        int i2 = lz.getMatchLen(m + 1, opts[optCur].reps[k], i1);
        if (i2 >= 2)
        {
          int i3 = n + repLenEncoder.getPrice(m, paramInt2);
          nextState.set(opts[optCur].state);
          nextState.updateLongRep();
          int i4 = lz.getByte(m, 0);
          int i5 = lz.getByte(0);
          int i6 = lz.getByte(m, 1);
          i3 += literalEncoder.getPrice(i4, i5, i6, paramInt1 + m, nextState);
          nextState.updateLiteral();
          int i7 = paramInt1 + m + 1 & posMask;
          i3 += getLongRepAndLenPrice(0, i2, nextState, i7);
          int i8 = optCur + m + 1 + i2;
          while (optEnd < i8) {
            opts[(++optEnd)].reset();
          }
          if (i3 < opts[i8].price) {
            opts[i8].set3(i3, optCur, k, m, 0);
          }
        }
      }
    }
    return i;
  }
  
  private void calcNormalMatchPrices(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (matches.len[(matches.count - 1)] > paramInt3)
    {
      for (matches.count = 0; matches.len[matches.count] < paramInt3; matches.count += 1) {}
      matches.len[(matches.count++)] = paramInt3;
    }
    if (matches.len[(matches.count - 1)] < paramInt5) {
      return;
    }
    while (optEnd < optCur + matches.len[(matches.count - 1)]) {
      opts[(++optEnd)].reset();
    }
    int i = getNormalMatchPrice(paramInt4, opts[optCur].state);
    for (int j = 0; paramInt5 > matches.len[j]; j++) {}
    for (int k = paramInt5;; k++)
    {
      int m = matches.dist[j];
      int n = getMatchAndLenPrice(i, m, k, paramInt2);
      if (n < opts[(optCur + k)].price) {
        opts[(optCur + k)].set1(n, optCur, m + 4);
      }
      if (k == matches.len[j])
      {
        int i1 = Math.min(niceLen, paramInt3 - k - 1);
        int i2 = lz.getMatchLen(k + 1, m, i1);
        if (i2 >= 2)
        {
          nextState.set(opts[optCur].state);
          nextState.updateMatch();
          int i3 = lz.getByte(k, 0);
          int i4 = lz.getByte(0);
          int i5 = lz.getByte(k, 1);
          int i6 = n + literalEncoder.getPrice(i3, i4, i5, paramInt1 + k, nextState);
          nextState.updateLiteral();
          int i7 = paramInt1 + k + 1 & posMask;
          i6 += getLongRepAndLenPrice(0, i2, nextState, i7);
          int i8 = optCur + k + 1 + i2;
          while (optEnd < i8) {
            opts[(++optEnd)].reset();
          }
          if (i6 < opts[i8].price) {
            opts[i8].set3(i6, optCur, m + 4, k, 0);
          }
        }
        j++;
        if (j == matches.count) {
          break;
        }
      }
    }
  }
}
