package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;






































final class Murmur3_32HashFunction
  extends AbstractStreamingHashFunction
  implements Serializable
{
  private final int seed;
  
  Murmur3_32HashFunction(int seed)
  {
    this.seed = seed;
  }
  



  public Hasher newHasher()
  {
    return new Murmur3_32Hasher(seed);
  }
  
  public String toString()
  {
    return "Hashing.murmur3_32(" + seed + ")";
  }
  








































  private static int mixK1(int k1)
  {
    k1 *= -862048943;
    k1 = Integer.rotateLeft(k1, 15);
    k1 *= 461845907;
    return k1;
  }
  
  private static int mixH1(int h1, int k1) {
    h1 ^= k1;
    h1 = Integer.rotateLeft(h1, 13);
    h1 = h1 * 5 + -430675100;
    return h1;
  }
  
  private static HashCode fmix(int h1, int length)
  {
    h1 ^= length;
    h1 ^= h1 >>> 16;
    h1 *= -2048144789;
    h1 ^= h1 >>> 13;
    h1 *= -1028477387;
    h1 ^= h1 >>> 16;
    return HashCodes.fromInt(h1);
  }
  
  private static final class Murmur3_32Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher
  {
    private int h1;
    private int length;
    
    Murmur3_32Hasher(int seed) {
      super();
      h1 = seed;
      length = 0;
    }
    
    protected void process(ByteBuffer bb) {
      int k1 = Murmur3_32HashFunction.mixK1(bb.getInt());
      h1 = Murmur3_32HashFunction.mixH1(h1, k1);
      length += 4;
    }
    
    protected void processRemaining(ByteBuffer bb) {
      length += bb.remaining();
      int k1 = 0;
      for (int i = 0; bb.hasRemaining(); i += 8) {
        k1 ^= UnsignedBytes.toInt(bb.get()) << i;
      }
      h1 ^= Murmur3_32HashFunction.mixK1(k1);
    }
    
    public HashCode makeHash() {
      return Murmur3_32HashFunction.fmix(h1, length);
    }
  }
}
