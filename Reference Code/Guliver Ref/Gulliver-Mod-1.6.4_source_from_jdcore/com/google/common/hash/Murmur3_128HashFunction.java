package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;































final class Murmur3_128HashFunction
  extends AbstractStreamingHashFunction
  implements Serializable
{
  private final int seed;
  
  Murmur3_128HashFunction(int seed)
  {
    this.seed = seed;
  }
  



  public Hasher newHasher()
  {
    return new Murmur3_128Hasher(seed);
  }
  
  public String toString()
  {
    return "Hashing.murmur3_128(" + seed + ")";
  }
  
  private static final class Murmur3_128Hasher
    extends AbstractStreamingHashFunction.AbstractStreamingHasher
  {
    private long h1;
    private long h2;
    private int length;
    
    Murmur3_128Hasher(int seed)
    {
      super();
      h1 = seed;
      h2 = seed;
      length = 0;
    }
    
    protected void process(ByteBuffer bb) {
      long k1 = bb.getLong();
      long k2 = bb.getLong();
      bmix64(k1, k2);
      length += 16;
    }
    
    private void bmix64(long k1, long k2) {
      h1 ^= mixK1(k1);
      
      h1 = Long.rotateLeft(h1, 27);
      h1 += h2;
      h1 = (h1 * 5L + 1390208809L);
      
      h2 ^= mixK2(k2);
      
      h2 = Long.rotateLeft(h2, 31);
      h2 += h1;
      h2 = (h2 * 5L + 944331445L);
    }
    
    protected void processRemaining(ByteBuffer bb) {
      long k1 = 0L;
      long k2 = 0L;
      length += bb.remaining();
      switch (bb.remaining()) {
      case 15: 
        k2 ^= UnsignedBytes.toInt(bb.get(14)) << 48;
      case 14: 
        k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40;
      case 13: 
        k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32;
      case 12: 
        k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24;
      case 11: 
        k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
      case 10: 
        k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
      case 9: 
        k2 ^= UnsignedBytes.toInt(bb.get(8));
      case 8: 
        k1 ^= bb.getLong();
        break;
      case 7: 
        k1 ^= UnsignedBytes.toInt(bb.get(6)) << 48;
      case 6: 
        k1 ^= UnsignedBytes.toInt(bb.get(5)) << 40;
      case 5: 
        k1 ^= UnsignedBytes.toInt(bb.get(4)) << 32;
      case 4: 
        k1 ^= UnsignedBytes.toInt(bb.get(3)) << 24;
      case 3: 
        k1 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
      case 2: 
        k1 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
      case 1: 
        k1 ^= UnsignedBytes.toInt(bb.get(0));
        break;
      default: 
        throw new AssertionError("Should never get here.");
      }
      h1 ^= mixK1(k1);
      h2 ^= mixK2(k2);
    }
    
    public HashCode makeHash() {
      h1 ^= length;
      h2 ^= length;
      
      h1 += h2;
      h2 += h1;
      
      h1 = fmix64(h1);
      h2 = fmix64(h2);
      
      h1 += h2;
      h2 += h1;
      
      return HashCodes.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(h1).putLong(h2).array());
    }
    




    private static long fmix64(long k)
    {
      k ^= k >>> 33;
      k *= -49064778989728563L;
      k ^= k >>> 33;
      k *= -4265267296055464877L;
      k ^= k >>> 33;
      return k;
    }
    
    private static long mixK1(long k1) {
      k1 *= -8663945395140668459L;
      k1 = Long.rotateLeft(k1, 31);
      k1 *= 5545529020109919103L;
      return k1;
    }
    
    private static long mixK2(long k2) {
      k2 *= 5545529020109919103L;
      k2 = Long.rotateLeft(k2, 33);
      k2 *= -8663945395140668459L;
      return k2;
    }
  }
}
