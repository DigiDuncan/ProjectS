package org.tukaani.xz.lzma;

import java.io.IOException;
import org.tukaani.xz.CorruptedInputException;
import org.tukaani.xz.lz.LZDecoder;
import org.tukaani.xz.rangecoder.RangeDecoder;

public final class LZMADecoder
  extends LZMACoder
{
  private final LZDecoder lz;
  private final RangeDecoder rc;
  private final LiteralDecoder literalDecoder;
  private final LengthDecoder matchLenDecoder = new LengthDecoder(null);
  private final LengthDecoder repLenDecoder = new LengthDecoder(null);
  
  public LZMADecoder(LZDecoder paramLZDecoder, RangeDecoder paramRangeDecoder, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramInt3);
    lz = paramLZDecoder;
    rc = paramRangeDecoder;
    literalDecoder = new LiteralDecoder(paramInt1, paramInt2);
    reset();
  }
  
  public void reset()
  {
    super.reset();
    literalDecoder.reset();
    matchLenDecoder.reset();
    repLenDecoder.reset();
  }
  
  public void decode()
    throws IOException
  {
    lz.repeatPending();
    while (lz.hasSpace())
    {
      int i = lz.getPos() & posMask;
      if (rc.decodeBit(isMatch[state.get()], i) == 0)
      {
        literalDecoder.decode();
      }
      else
      {
        int j = rc.decodeBit(isRep, state.get()) == 0 ? decodeMatch(i) : decodeRepMatch(i);
        lz.repeat(reps[0], j);
      }
    }
    rc.normalize();
    if (!rc.isInBufferOK()) {
      throw new CorruptedInputException();
    }
  }
  
  private int decodeMatch(int paramInt)
    throws IOException
  {
    state.updateMatch();
    reps[3] = reps[2];
    reps[2] = reps[1];
    reps[1] = reps[0];
    int i = matchLenDecoder.decode(paramInt);
    int j = rc.decodeBitTree(distSlots[getDistState(i)]);
    if (j < 4)
    {
      reps[0] = j;
    }
    else
    {
      int k = (j >> 1) - 1;
      reps[0] = ((0x2 | j & 0x1) << k);
      if (j < 14)
      {
        reps[0] |= rc.decodeReverseBitTree(distSpecial[(j - 4)]);
      }
      else
      {
        reps[0] |= rc.decodeDirectBits(k - 4) << 4;
        reps[0] |= rc.decodeReverseBitTree(distAlign);
      }
    }
    return i;
  }
  
  private int decodeRepMatch(int paramInt)
    throws IOException
  {
    if (rc.decodeBit(isRep0, state.get()) == 0)
    {
      if (rc.decodeBit(isRep0Long[state.get()], paramInt) == 0)
      {
        state.updateShortRep();
        return 1;
      }
    }
    else
    {
      int i;
      if (rc.decodeBit(isRep1, state.get()) == 0)
      {
        i = reps[1];
      }
      else
      {
        if (rc.decodeBit(isRep2, state.get()) == 0)
        {
          i = reps[2];
        }
        else
        {
          i = reps[3];
          reps[3] = reps[2];
        }
        reps[2] = reps[1];
      }
      reps[1] = reps[0];
      reps[0] = i;
    }
    state.updateLongRep();
    return repLenDecoder.decode(paramInt);
  }
  
  private class LengthDecoder
    extends LZMACoder.LengthCoder
  {
    private LengthDecoder()
    {
      super();
    }
    
    int decode(int paramInt)
      throws IOException
    {
      if (rc.decodeBit(choice, 0) == 0) {
        return rc.decodeBitTree(low[paramInt]) + 2;
      }
      if (rc.decodeBit(choice, 1) == 0) {
        return rc.decodeBitTree(mid[paramInt]) + 2 + 8;
      }
      return rc.decodeBitTree(high) + 2 + 8 + 8;
    }
    
    LengthDecoder(LZMADecoder.1 param1)
    {
      this();
    }
  }
  
  private class LiteralDecoder
    extends LZMACoder.LiteralCoder
  {
    LiteralSubdecoder[] subdecoders;
    
    LiteralDecoder(int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
      subdecoders = new LiteralSubdecoder[1 << paramInt1 + paramInt2];
      for (int i = 0; i < subdecoders.length; i++) {
        subdecoders[i] = new LiteralSubdecoder(null);
      }
    }
    
    void reset()
    {
      for (int i = 0; i < subdecoders.length; i++) {
        subdecoders[i].reset();
      }
    }
    
    void decode()
      throws IOException
    {
      int i = getSubcoderIndex(lz.getByte(0), lz.getPos());
      subdecoders[i].decode();
    }
    
    private class LiteralSubdecoder
      extends LZMACoder.LiteralCoder.LiteralSubcoder
    {
      private LiteralSubdecoder()
      {
        super();
      }
      
      void decode()
        throws IOException
      {
        int i = 1;
        if (state.isLiteral())
        {
          do
          {
            i = i << 1 | rc.decodeBit(probs, i);
          } while (i < 256);
        }
        else
        {
          int j = lz.getByte(reps[0]);
          int k = 256;
          do
          {
            j <<= 1;
            int m = j & k;
            int n = rc.decodeBit(probs, k + m + i);
            i = i << 1 | n;
            k &= (0 - n ^ m ^ 0xFFFFFFFF);
          } while (i < 256);
        }
        lz.putByte((byte)i);
        state.updateLiteral();
      }
      
      LiteralSubdecoder(LZMADecoder.1 param1)
      {
        this();
      }
    }
  }
}
