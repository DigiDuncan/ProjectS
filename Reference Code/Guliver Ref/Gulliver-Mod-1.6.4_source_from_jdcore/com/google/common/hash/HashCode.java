package com.google.common.hash;

import java.security.MessageDigest;
import javax.annotation.Nullable;












































































public abstract class HashCode
{
  HashCode() {}
  
  public abstract int asInt();
  
  public abstract byte[] asBytes();
  
  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof HashCode)) {
      HashCode that = (HashCode)object;
      

      return MessageDigest.isEqual(asBytes(), that.asBytes());
    }
    return false;
  }
  








  public int hashCode()
  {
    return asInt();
  }
  








  public String toString()
  {
    byte[] bytes = asBytes();
    StringBuilder sb = new StringBuilder(2 * bytes.length);
    for (byte b : bytes) {
      sb.append(hexDigits[(b >> 4 & 0xF)]).append(hexDigits[(b & 0xF)]);
    }
    return sb.toString();
  }
  
  private static final char[] hexDigits = "0123456789abcdef".toCharArray();
}
