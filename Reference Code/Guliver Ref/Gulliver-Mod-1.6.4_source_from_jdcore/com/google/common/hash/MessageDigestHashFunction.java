package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;





















final class MessageDigestHashFunction
  extends AbstractStreamingHashFunction
  implements Serializable
{
  private final MessageDigest prototype;
  private final int bytes;
  private final boolean supportsClone;
  private final String toString;
  
  MessageDigestHashFunction(String algorithmName, String toString)
  {
    prototype = getMessageDigest(algorithmName);
    bytes = prototype.getDigestLength();
    this.toString = ((String)Preconditions.checkNotNull(toString));
    supportsClone = supportsClone();
  }
  








  private boolean supportsClone()
  {
    try
    {
      prototype.clone();
      return true;
    } catch (CloneNotSupportedException e) {}
    return false;
  }
  




  public String toString()
  {
    return toString;
  }
  
  private static MessageDigest getMessageDigest(String algorithmName) {
    try {
      return MessageDigest.getInstance(algorithmName);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }
  
  public Hasher newHasher() {
    if (supportsClone) {
      try {
        return new MessageDigestHasher((MessageDigest)prototype.clone(), bytes, null);
      }
      catch (CloneNotSupportedException e) {}
    }
    
    return new MessageDigestHasher(getMessageDigest(prototype.getAlgorithm()), bytes, null);
  }
  






  private static final class MessageDigestHasher
    extends AbstractByteHasher
  {
    private final MessageDigest digest;
    





    private final int bytes;
    





    private boolean done;
    





    private MessageDigestHasher(MessageDigest digest, int bytes)
    {
      this.digest = digest;
      this.bytes = bytes;
    }
    
    protected void update(byte b)
    {
      checkNotDone();
      digest.update(b);
    }
    
    protected void update(byte[] b)
    {
      checkNotDone();
      digest.update(b);
    }
    
    protected void update(byte[] b, int off, int len)
    {
      checkNotDone();
      digest.update(b, off, len);
    }
    
    private void checkNotDone() {
      Preconditions.checkState(!done, "Cannot use Hasher after calling #hash() on it");
    }
    
    public HashCode hash()
    {
      done = true;
      return bytes == digest.getDigestLength() ? HashCodes.fromBytesNoCopy(digest.digest()) : HashCodes.fromBytesNoCopy(Arrays.copyOf(digest.digest(), bytes));
    }
  }
}
