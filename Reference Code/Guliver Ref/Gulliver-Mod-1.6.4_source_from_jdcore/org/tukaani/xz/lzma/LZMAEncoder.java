package org.tukaani.xz.lzma;

import org.tukaani.xz.lz.LZEncoder;
import org.tukaani.xz.lz.Matches;
import org.tukaani.xz.rangecoder.RangeEncoder;

public abstract class LZMAEncoder
  extends LZMACoder
{
  private final RangeEncoder rc;
  final LZEncoder lz;
  final LiteralEncoder literalEncoder;
  final LengthEncoder matchLenEncoder;
  final LengthEncoder repLenEncoder;
  final int niceLen;
  private int distPriceCount = 0;
  private int alignPriceCount = 0;
  private final int distSlotPricesSize;
  private final int[][] distSlotPrices;
  private final int[][] fullDistPrices = new int[4][''];
  private final int[] alignPrices = new int[16];
  int back = 0;
  int readAhead = -1;
  private int uncompressedSize = 0;
  
  public static LZMAEncoder getInstance(RangeEncoder paramRangeEncoder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    switch (paramInt4)
    {
    case 1: 
      return new LZMAEncoderFast(paramRangeEncoder, paramInt1, paramInt2, paramInt3, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
    case 2: 
      return new LZMAEncoderNormal(paramRangeEncoder, paramInt1, paramInt2, paramInt3, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
    }
    throw new IllegalArgumentException();
  }
  
  public static int getDistSlot(int paramInt)
  {
    if (paramInt <= 4) {
      return paramInt;
    }
    int i = paramInt;
    int j = 31;
    if ((i & 0xFFFF0000) == 0)
    {
      i <<= 16;
      j = 15;
    }
    if ((i & 0xFF000000) == 0)
    {
      i <<= 8;
      j -= 8;
    }
    if ((i & 0xF0000000) == 0)
    {
      i <<= 4;
      j -= 4;
    }
    if ((i & 0xC0000000) == 0)
    {
      i <<= 2;
      j -= 2;
    }
    if ((i & 0x80000000) == 0) {
      j--;
    }
    return (j << 1) + (paramInt >>> j - 1 & 0x1);
  }
  
  abstract int getNextSymbol();
  
  LZMAEncoder(RangeEncoder paramRangeEncoder, LZEncoder paramLZEncoder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super(paramInt3);
    rc = paramRangeEncoder;
    lz = paramLZEncoder;
    niceLen = paramInt5;
    literalEncoder = new LiteralEncoder(paramInt1, paramInt2);
    matchLenEncoder = new LengthEncoder(paramInt3, paramInt5);
    repLenEncoder = new LengthEncoder(paramInt3, paramInt5);
    distSlotPricesSize = (getDistSlot(paramInt4 - 1) + 1);
    distSlotPrices = new int[4][distSlotPricesSize];
    reset();
  }
  
  public LZEncoder getLZEncoder()
  {
    return lz;
  }
  
  public void reset()
  {
    super.reset();
    literalEncoder.reset();
    matchLenEncoder.reset();
    repLenEncoder.reset();
    distPriceCount = 0;
    alignPriceCount = 0;
    uncompressedSize += readAhead + 1;
    readAhead = -1;
  }
  
  public int getUncompressedSize()
  {
    return uncompressedSize;
  }
  
  public void resetUncompressedSize()
  {
    uncompressedSize = 0;
  }
  
  public boolean encodeForLZMA2()
  {
    if ((!lz.isStarted()) && (!encodeInit())) {
      return false;
    }
    while ((uncompressedSize <= 2096879) && (rc.getPendingSize() <= 65510)) {
      if (!encodeSymbol()) {
        return false;
      }
    }
    return true;
  }
  
  private boolean encodeInit()
  {
    assert (readAhead == -1);
    if (!lz.hasEnoughData(0)) {
      return false;
    }
    skip(1);
    rc.encodeBit(isMatch[state.get()], 0, 0);
    literalEncoder.encodeInit();
    readAhead -= 1;
    assert (readAhead == -1);
    uncompressedSize += 1;
    assert (uncompressedSize == 1);
    return true;
  }
  
  private boolean encodeSymbol()
  {
    if (!lz.hasEnoughData(readAhead + 1)) {
      return false;
    }
    int i = getNextSymbol();
    assert (readAhead >= 0);
    int j = lz.getPos() - readAhead & posMask;
    if (back == -1)
    {
      assert (i == 1);
      rc.encodeBit(isMatch[state.get()], j, 0);
      literalEncoder.encode();
    }
    else
    {
      rc.encodeBit(isMatch[state.get()], j, 1);
      if (back < 4)
      {
        assert (lz.getMatchLen(-readAhead, reps[back], i) == i);
        rc.encodeBit(isRep, state.get(), 1);
        encodeRepMatch(back, i, j);
      }
      else
      {
        assert (lz.getMatchLen(-readAhead, back - 4, i) == i);
        rc.encodeBit(isRep, state.get(), 0);
        encodeMatch(back - 4, i, j);
      }
    }
    readAhead -= i;
    uncompressedSize += i;
    return true;
  }
  
  private void encodeMatch(int paramInt1, int paramInt2, int paramInt3)
  {
    state.updateMatch();
    matchLenEncoder.encode(paramInt2, paramInt3);
    int i = getDistSlot(paramInt1);
    rc.encodeBitTree(distSlots[getDistState(paramInt2)], i);
    if (i >= 4)
    {
      int j = (i >>> 1) - 1;
      int k = (0x2 | i & 0x1) << j;
      int m = paramInt1 - k;
      if (i < 14)
      {
        rc.encodeReverseBitTree(distSpecial[(i - 4)], m);
      }
      else
      {
        rc.encodeDirectBits(m >>> 4, j - 4);
        rc.encodeReverseBitTree(distAlign, m & 0xF);
        alignPriceCount -= 1;
      }
    }
    reps[3] = reps[2];
    reps[2] = reps[1];
    reps[1] = reps[0];
    reps[0] = paramInt1;
    distPriceCount -= 1;
  }
  
  private void encodeRepMatch(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 == 0)
    {
      rc.encodeBit(isRep0, state.get(), 0);
      rc.encodeBit(isRep0Long[state.get()], paramInt3, paramInt2 == 1 ? 0 : 1);
    }
    else
    {
      int i = reps[paramInt1];
      rc.encodeBit(isRep0, state.get(), 1);
      if (paramInt1 == 1)
      {
        rc.encodeBit(isRep1, state.get(), 0);
      }
      else
      {
        rc.encodeBit(isRep1, state.get(), 1);
        rc.encodeBit(isRep2, state.get(), paramInt1 - 2);
        if (paramInt1 == 3) {
          reps[3] = reps[2];
        }
        reps[2] = reps[1];
      }
      reps[1] = reps[0];
      reps[0] = i;
    }
    if (paramInt2 == 1)
    {
      state.updateShortRep();
    }
    else
    {
      repLenEncoder.encode(paramInt2, paramInt3);
      state.updateLongRep();
    }
  }
  
  Matches getMatches()
  {
    readAhead += 1;
    Matches localMatches = lz.getMatches();
    assert (lz.verifyMatches(localMatches));
    return localMatches;
  }
  
  void skip(int paramInt)
  {
    readAhead += paramInt;
    lz.skip(paramInt);
  }
  
  int getAnyMatchPrice(State paramState, int paramInt)
  {
    return RangeEncoder.getBitPrice(isMatch[paramState.get()][paramInt], 1);
  }
  
  int getNormalMatchPrice(int paramInt, State paramState)
  {
    return paramInt + RangeEncoder.getBitPrice(isRep[paramState.get()], 0);
  }
  
  int getAnyRepPrice(int paramInt, State paramState)
  {
    return paramInt + RangeEncoder.getBitPrice(isRep[paramState.get()], 1);
  }
  
  int getShortRepPrice(int paramInt1, State paramState, int paramInt2)
  {
    return paramInt1 + RangeEncoder.getBitPrice(isRep0[paramState.get()], 0) + RangeEncoder.getBitPrice(isRep0Long[paramState.get()][paramInt2], 0);
  }
  
  int getLongRepPrice(int paramInt1, int paramInt2, State paramState, int paramInt3)
  {
    int i = paramInt1;
    if (paramInt2 == 0)
    {
      i += RangeEncoder.getBitPrice(isRep0[paramState.get()], 0) + RangeEncoder.getBitPrice(isRep0Long[paramState.get()][paramInt3], 1);
    }
    else
    {
      i += RangeEncoder.getBitPrice(isRep0[paramState.get()], 1);
      if (paramInt2 == 1) {
        i += RangeEncoder.getBitPrice(isRep1[paramState.get()], 0);
      } else {
        i += RangeEncoder.getBitPrice(isRep1[paramState.get()], 1) + RangeEncoder.getBitPrice(isRep2[paramState.get()], paramInt2 - 2);
      }
    }
    return i;
  }
  
  int getLongRepAndLenPrice(int paramInt1, int paramInt2, State paramState, int paramInt3)
  {
    int i = getAnyMatchPrice(paramState, paramInt3);
    int j = getAnyRepPrice(i, paramState);
    int k = getLongRepPrice(j, paramInt1, paramState, paramInt3);
    return k + repLenEncoder.getPrice(paramInt2, paramInt3);
  }
  
  int getMatchAndLenPrice(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 + matchLenEncoder.getPrice(paramInt3, paramInt4);
    int j = getDistState(paramInt3);
    if (paramInt2 < 128)
    {
      i += fullDistPrices[j][paramInt2];
    }
    else
    {
      int k = getDistSlot(paramInt2);
      i += distSlotPrices[j][k] + alignPrices[(paramInt2 & 0xF)];
    }
    return i;
  }
  
  private void updateDistPrices()
  {
    distPriceCount = 128;
    int k;
    for (int i = 0; i < 4; i++)
    {
      for (j = 0; j < distSlotPricesSize; j++) {
        distSlotPrices[i][j] = RangeEncoder.getBitTreePrice(distSlots[i], j);
      }
      for (j = 14; j < distSlotPricesSize; j++)
      {
        k = (j >>> 1) - 1 - 4;
        distSlotPrices[i][j] += RangeEncoder.getDirectBitsPrice(k);
      }
      for (j = 0; j < 4; j++) {
        fullDistPrices[i][j] = distSlotPrices[i][j];
      }
    }
    i = 4;
    for (int j = 4; j < 14; j++)
    {
      k = (j >>> 1) - 1;
      int m = (0x2 | j & 0x1) << k;
      int n = distSpecial[(j - 4)].length;
      for (int i1 = 0; i1 < n; i1++)
      {
        int i2 = i - m;
        int i3 = RangeEncoder.getReverseBitTreePrice(distSpecial[(j - 4)], i2);
        for (int i4 = 0; i4 < 4; i4++) {
          fullDistPrices[i4][i] = (distSlotPrices[i4][j] + i3);
        }
        i++;
      }
    }
    assert (i == 128);
  }
  
  private void updateAlignPrices()
  {
    alignPriceCount = 16;
    for (int i = 0; i < 16; i++) {
      alignPrices[i] = RangeEncoder.getReverseBitTreePrice(distAlign, i);
    }
  }
  
  void updatePrices()
  {
    if (distPriceCount <= 0) {
      updateDistPrices();
    }
    if (alignPriceCount <= 0) {
      updateAlignPrices();
    }
    matchLenEncoder.updatePrices();
    repLenEncoder.updatePrices();
  }
  
  class LengthEncoder
    extends LZMACoder.LengthCoder
  {
    private final int[] counters;
    private final int[][] prices;
    
    LengthEncoder(int paramInt1, int paramInt2)
    {
      super();
      int i = 1 << paramInt1;
      counters = new int[i];
      int j = Math.max(paramInt2 - 2 + 1, 16);
      prices = new int[i][j];
    }
    
    void reset()
    {
      super.reset();
      for (int i = 0; i < counters.length; i++) {
        counters[i] = 0;
      }
    }
    
    void encode(int paramInt1, int paramInt2)
    {
      
      if (paramInt1 < 8)
      {
        rc.encodeBit(choice, 0, 0);
        rc.encodeBitTree(low[paramInt2], paramInt1);
      }
      else
      {
        rc.encodeBit(choice, 0, 1);
        paramInt1 -= 8;
        if (paramInt1 < 8)
        {
          rc.encodeBit(choice, 1, 0);
          rc.encodeBitTree(mid[paramInt2], paramInt1);
        }
        else
        {
          rc.encodeBit(choice, 1, 1);
          rc.encodeBitTree(high, paramInt1 - 8);
        }
      }
      counters[paramInt2] -= 1;
    }
    
    int getPrice(int paramInt1, int paramInt2)
    {
      return prices[paramInt2][(paramInt1 - 2)];
    }
    
    void updatePrices()
    {
      for (int i = 0; i < counters.length; i++) {
        if (counters[i] <= 0)
        {
          counters[i] = 32;
          updatePrices(i);
        }
      }
    }
    
    private void updatePrices(int paramInt)
    {
      int i = RangeEncoder.getBitPrice(choice[0], 0);
      for (int j = 0; j < 8; j++) {
        prices[paramInt][j] = (i + RangeEncoder.getBitTreePrice(low[paramInt], j));
      }
      i = RangeEncoder.getBitPrice(choice[0], 1);
      int k = RangeEncoder.getBitPrice(choice[1], 0);
      while (j < 16)
      {
        prices[paramInt][j] = (i + k + RangeEncoder.getBitTreePrice(mid[paramInt], j - 8));
        j++;
      }
      k = RangeEncoder.getBitPrice(choice[1], 1);
      while (j < prices[paramInt].length)
      {
        prices[paramInt][j] = (i + k + RangeEncoder.getBitTreePrice(high, j - 8 - 8));
        j++;
      }
    }
  }
  
  class LiteralEncoder
    extends LZMACoder.LiteralCoder
  {
    LiteralSubencoder[] subencoders;
    
    LiteralEncoder(int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
      subencoders = new LiteralSubencoder[1 << paramInt1 + paramInt2];
      for (int i = 0; i < subencoders.length; i++) {
        subencoders[i] = new LiteralSubencoder(null);
      }
    }
    
    void reset()
    {
      for (int i = 0; i < subencoders.length; i++) {
        subencoders[i].reset();
      }
    }
    
    void encodeInit()
    {
      assert (readAhead >= 0);
      subencoders[0].encode();
    }
    
    void encode()
    {
      assert (readAhead >= 0);
      int i = getSubcoderIndex(lz.getByte(1 + readAhead), lz.getPos() - readAhead);
      subencoders[i].encode();
    }
    
    int getPrice(int paramInt1, int paramInt2, int paramInt3, int paramInt4, State paramState)
    {
      int i = RangeEncoder.getBitPrice(isMatch[paramState.get()][(paramInt4 & posMask)], 0);
      int j = getSubcoderIndex(paramInt3, paramInt4);
      i += (paramState.isLiteral() ? subencoders[j].getNormalPrice(paramInt1) : subencoders[j].getMatchedPrice(paramInt1, paramInt2));
      return i;
    }
    
    private class LiteralSubencoder
      extends LZMACoder.LiteralCoder.LiteralSubcoder
    {
      private LiteralSubencoder()
      {
        super();
      }
      
      void encode()
      {
        int i = lz.getByte(readAhead) | 0x100;
        int j;
        int k;
        if (state.isLiteral())
        {
          do
          {
            j = i >>> 8;
            k = i >>> 7 & 0x1;
            rc.encodeBit(probs, j, k);
            i <<= 1;
          } while (i < 65536);
        }
        else
        {
          j = lz.getByte(reps[0] + 1 + readAhead);
          k = 256;
          do
          {
            j <<= 1;
            int n = j & k;
            int m = k + n + (i >>> 8);
            int i1 = i >>> 7 & 0x1;
            rc.encodeBit(probs, m, i1);
            i <<= 1;
            k &= (j ^ i ^ 0xFFFFFFFF);
          } while (i < 65536);
        }
        state.updateLiteral();
      }
      
      int getNormalPrice(int paramInt)
      {
        int i = 0;
        paramInt |= 0x100;
        do
        {
          int j = paramInt >>> 8;
          int k = paramInt >>> 7 & 0x1;
          i += RangeEncoder.getBitPrice(probs[j], k);
          paramInt <<= 1;
        } while (paramInt < 65536);
        return i;
      }
      
      int getMatchedPrice(int paramInt1, int paramInt2)
      {
        int i = 0;
        int j = 256;
        paramInt1 |= 0x100;
        do
        {
          paramInt2 <<= 1;
          int m = paramInt2 & j;
          int k = j + m + (paramInt1 >>> 8);
          int n = paramInt1 >>> 7 & 0x1;
          i += RangeEncoder.getBitPrice(probs[k], n);
          paramInt1 <<= 1;
          j &= (paramInt2 ^ paramInt1 ^ 0xFFFFFFFF);
        } while (paramInt1 < 65536);
        return i;
      }
      
      LiteralSubencoder(LZMAEncoder.1 param1)
      {
        this();
      }
    }
  }
}
