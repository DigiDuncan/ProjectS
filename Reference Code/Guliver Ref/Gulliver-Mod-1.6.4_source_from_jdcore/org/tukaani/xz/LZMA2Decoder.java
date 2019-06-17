package org.tukaani.xz;

import java.io.InputStream;

class LZMA2Decoder
  extends LZMA2Coder
  implements FilterDecoder
{
  private int dictSize;
  
  LZMA2Decoder(byte[] paramArrayOfByte)
    throws UnsupportedOptionsException
  {
    if ((paramArrayOfByte.length != 1) || ((paramArrayOfByte[0] & 0xFF) > 37)) {
      throw new UnsupportedOptionsException("Unsupported LZMA2 properties");
    }
    dictSize = (0x2 | paramArrayOfByte[0] & 0x1);
    dictSize <<= (paramArrayOfByte[0] >>> 1) + 11;
  }
  
  public int getMemoryUsage()
  {
    return LZMA2InputStream.getMemoryUsage(dictSize);
  }
  
  public InputStream getInputStream(InputStream paramInputStream)
  {
    return new LZMA2InputStream(paramInputStream, dictSize);
  }
}
