package org.tukaani.xz.lzma;

import org.tukaani.xz.rangecoder.RangeCoder;

abstract class LZMACoder
{
  final int posMask;
  final int[] reps = new int[4];
  final State state = new State();
  final short[][] isMatch = new short[12][16];
  final short[] isRep = new short[12];
  final short[] isRep0 = new short[12];
  final short[] isRep1 = new short[12];
  final short[] isRep2 = new short[12];
  final short[][] isRep0Long = new short[12][16];
  final short[][] distSlots = new short[4][64];
  final short[][] distSpecial = { new short[2], new short[2], new short[4], new short[4], new short[8], new short[8], new short[16], new short[16], new short[32], new short[32] };
  final short[] distAlign = new short[16];
  
  static final int getDistState(int paramInt)
  {
    return paramInt < 6 ? paramInt - 2 : 3;
  }
  
  LZMACoder(int paramInt)
  {
    posMask = ((1 << paramInt) - 1);
  }
  
  void reset()
  {
    reps[0] = 0;
    reps[1] = 0;
    reps[2] = 0;
    reps[3] = 0;
    state.reset();
    for (int i = 0; i < isMatch.length; i++) {
      RangeCoder.initProbs(isMatch[i]);
    }
    RangeCoder.initProbs(isRep);
    RangeCoder.initProbs(isRep0);
    RangeCoder.initProbs(isRep1);
    RangeCoder.initProbs(isRep2);
    for (i = 0; i < isRep0Long.length; i++) {
      RangeCoder.initProbs(isRep0Long[i]);
    }
    for (i = 0; i < distSlots.length; i++) {
      RangeCoder.initProbs(distSlots[i]);
    }
    for (i = 0; i < distSpecial.length; i++) {
      RangeCoder.initProbs(distSpecial[i]);
    }
    RangeCoder.initProbs(distAlign);
  }
  
  abstract class LengthCoder
  {
    final short[] choice = new short[2];
    final short[][] low = new short[16][8];
    final short[][] mid = new short[16][8];
    final short[] high = new short['Ā'];
    
    LengthCoder() {}
    
    void reset()
    {
      RangeCoder.initProbs(choice);
      for (int i = 0; i < low.length; i++) {
        RangeCoder.initProbs(low[i]);
      }
      for (i = 0; i < low.length; i++) {
        RangeCoder.initProbs(mid[i]);
      }
      RangeCoder.initProbs(high);
    }
  }
  
  abstract class LiteralCoder
  {
    private final int lc;
    private final int literalPosMask;
    
    LiteralCoder(int paramInt1, int paramInt2)
    {
      lc = paramInt1;
      literalPosMask = ((1 << paramInt2) - 1);
    }
    
    final int getSubcoderIndex(int paramInt1, int paramInt2)
    {
      int i = paramInt1 >> 8 - lc;
      int j = (paramInt2 & literalPosMask) << lc;
      return i + j;
    }
    
    abstract class LiteralSubcoder
    {
      final short[] probs = new short['̀'];
      
      LiteralSubcoder() {}
      
      void reset()
      {
        RangeCoder.initProbs(probs);
      }
    }
  }
}
