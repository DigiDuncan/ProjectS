package com.google.common.hash;

import com.google.common.base.Supplier;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;






































public final class Hashing
{
  private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
  

  private static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = murmur3_32(GOOD_FAST_HASH_SEED);
  

  private static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = murmur3_128(GOOD_FAST_HASH_SEED);
  











































  public static HashFunction murmur3_32(int seed)
  {
    return new Murmur3_32HashFunction(seed);
  }
  












  private static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
  







  public static HashFunction murmur3_128(int seed)
  {
    return new Murmur3_128HashFunction(seed);
  }
  












  private static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
  








  private static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
  



  public static HashFunction sha1()
  {
    return SHA_1;
  }
  
  private static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
  









  private static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
  









  private static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
  














  private static final HashFunction CRC_32 = checksumHashFunction(ChecksumType.CRC_32, "Hashing.crc32()");
  














  private static final HashFunction ADLER_32 = checksumHashFunction(ChecksumType.ADLER_32, "Hashing.adler32()");
  
  private static HashFunction checksumHashFunction(ChecksumType type, String toString)
  {
    return new ChecksumHashFunction(type, bits, toString);
  }
  
  static abstract enum ChecksumType implements Supplier<Checksum> {
    CRC_32(32), 
    




    ADLER_32(32);
    


    private final int bits;
    


    private ChecksumType(int bits)
    {
      this.bits = bits;
    }
    
    public abstract Checksum get();
  }
}
