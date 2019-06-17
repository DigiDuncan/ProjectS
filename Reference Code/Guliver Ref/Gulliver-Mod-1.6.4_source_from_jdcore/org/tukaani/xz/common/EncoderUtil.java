package org.tukaani.xz.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

public class EncoderUtil
  extends Util
{
  public static void writeCRC32(OutputStream paramOutputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(paramArrayOfByte);
    long l = localCRC32.getValue();
    for (int i = 0; i < 4; i++) {
      paramOutputStream.write((byte)(int)(l >>> i * 8));
    }
  }
  
  public static void encodeVLI(OutputStream paramOutputStream, long paramLong)
    throws IOException
  {
    while (paramLong >= 128L)
    {
      paramOutputStream.write((byte)(int)(paramLong | 0x80));
      paramLong >>>= 7;
    }
    paramOutputStream.write((byte)(int)paramLong);
  }
}
