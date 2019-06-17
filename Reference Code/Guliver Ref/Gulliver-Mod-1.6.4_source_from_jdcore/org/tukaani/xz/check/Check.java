package org.tukaani.xz.check;

import java.security.NoSuchAlgorithmException;
import org.tukaani.xz.UnsupportedOptionsException;

public abstract class Check
{
  int size;
  String name;
  
  public Check() {}
  
  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract byte[] finish();
  
  public void update(byte[] paramArrayOfByte)
  {
    update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int getSize()
  {
    return size;
  }
  
  public String getName()
  {
    return name;
  }
  
  public static Check getInstance(int paramInt)
    throws UnsupportedOptionsException
  {
    switch (paramInt)
    {
    case 0: 
      return new None();
    case 1: 
      return new CRC32();
    case 4: 
      return new CRC64();
    case 10: 
      try
      {
        return new SHA256();
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
    }
    throw new UnsupportedOptionsException("Unsupported Check ID " + paramInt);
  }
}
