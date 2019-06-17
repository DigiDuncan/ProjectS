package org.tukaani.xz;

import java.io.InputStream;

class DeltaDecoder
  extends DeltaCoder
  implements FilterDecoder
{
  private final int distance;
  
  DeltaDecoder(byte[] paramArrayOfByte)
    throws UnsupportedOptionsException
  {
    if (paramArrayOfByte.length != 1) {
      throw new UnsupportedOptionsException("Unsupported Delta filter properties");
    }
    distance = ((paramArrayOfByte[0] & 0xFF) + 1);
  }
  
  public int getMemoryUsage()
  {
    return 1;
  }
  
  public InputStream getInputStream(InputStream paramInputStream)
  {
    return new DeltaInputStream(paramInputStream, distance);
  }
}
