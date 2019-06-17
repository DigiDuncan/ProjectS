package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;

































public final class HashCodes
{
  public static HashCode fromInt(int hash)
  {
    return new IntHashCode(hash);
  }
  
  private static final class IntHashCode extends HashCode implements Serializable {
    final int hash;
    
    IntHashCode(int hash) {
      this.hash = hash;
    }
    



    public byte[] asBytes()
    {
      return new byte[] { (byte)hash, (byte)(hash >> 8), (byte)(hash >> 16), (byte)(hash >> 24) };
    }
    



    public int asInt()
    {
      return hash;
    }
  }
  














  public static HashCode fromLong(long hash)
  {
    return new LongHashCode(hash);
  }
  
  private static final class LongHashCode extends HashCode implements Serializable {
    final long hash;
    
    LongHashCode(long hash) {
      this.hash = hash;
    }
    



    public byte[] asBytes()
    {
      return new byte[] { (byte)(int)hash, (byte)(int)(hash >> 8), (byte)(int)(hash >> 16), (byte)(int)(hash >> 24), (byte)(int)(hash >> 32), (byte)(int)(hash >> 40), (byte)(int)(hash >> 48), (byte)(int)(hash >> 56) };
    }
    







    public int asInt()
    {
      return (int)hash;
    }
  }
  























  static HashCode fromBytesNoCopy(byte[] bytes)
  {
    return new BytesHashCode(bytes);
  }
  
  private static final class BytesHashCode extends HashCode implements Serializable {
    final byte[] bytes;
    
    BytesHashCode(byte[] bytes) {
      this.bytes = ((byte[])Preconditions.checkNotNull(bytes));
    }
    



    public byte[] asBytes()
    {
      return (byte[])bytes.clone();
    }
    
    public int asInt() {
      Preconditions.checkState(bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", new Object[] { Integer.valueOf(bytes.length) });
      
      return bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
    }
    





















    public int hashCode()
    {
      if (bytes.length >= 4) {
        return asInt();
      }
      int val = bytes[0] & 0xFF;
      for (int i = 1; i < bytes.length; i++) {
        val |= (bytes[i] & 0xFF) << i * 8;
      }
      return val;
    }
  }
}
