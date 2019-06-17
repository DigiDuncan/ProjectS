package org.tukaani.xz.check;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256
  extends Check
{
  private final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
  
  public SHA256()
    throws NoSuchAlgorithmException
  {
    size = 32;
    name = "SHA-256";
  }
  
  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    sha256.update(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public byte[] finish()
  {
    byte[] arrayOfByte = sha256.digest();
    sha256.reset();
    return arrayOfByte;
  }
}
