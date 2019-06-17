package org.tukaani.xz.common;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import org.tukaani.xz.CorruptedInputException;
import org.tukaani.xz.UnsupportedOptionsException;
import org.tukaani.xz.XZ;
import org.tukaani.xz.XZFormatException;

public class DecoderUtil
  extends Util
{
  public static boolean isCRC32Valid(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(paramArrayOfByte, paramInt1, paramInt2);
    long l = localCRC32.getValue();
    for (int i = 0; i < 4; i++) {
      if ((byte)(int)(l >>> i * 8) != paramArrayOfByte[(paramInt3 + i)]) {
        return false;
      }
    }
    return true;
  }
  
  public static StreamFlags decodeStreamHeader(byte[] paramArrayOfByte)
    throws IOException
  {
    for (int i = 0; i < XZ.HEADER_MAGIC.length; i++) {
      if (paramArrayOfByte[i] != XZ.HEADER_MAGIC[i]) {
        throw new XZFormatException();
      }
    }
    if (!isCRC32Valid(paramArrayOfByte, XZ.HEADER_MAGIC.length, 2, XZ.HEADER_MAGIC.length + 2)) {
      throw new CorruptedInputException("XZ Stream Header is corrupt");
    }
    try
    {
      return decodeStreamFlags(paramArrayOfByte, XZ.HEADER_MAGIC.length);
    }
    catch (UnsupportedOptionsException localUnsupportedOptionsException)
    {
      throw new UnsupportedOptionsException("Unsupported options in XZ Stream Header");
    }
  }
  
  public static StreamFlags decodeStreamFooter(byte[] paramArrayOfByte)
    throws IOException
  {
    if ((paramArrayOfByte[10] != XZ.FOOTER_MAGIC[0]) || (paramArrayOfByte[11] != XZ.FOOTER_MAGIC[1])) {
      throw new CorruptedInputException("XZ Stream Footer is corrupt");
    }
    if (!isCRC32Valid(paramArrayOfByte, 4, 6, 0)) {
      throw new CorruptedInputException("XZ Stream Footer is corrupt");
    }
    StreamFlags localStreamFlags;
    try
    {
      localStreamFlags = decodeStreamFlags(paramArrayOfByte, 8);
    }
    catch (UnsupportedOptionsException localUnsupportedOptionsException)
    {
      throw new UnsupportedOptionsException("Unsupported options in XZ Stream Footer");
    }
    backwardSize = 0L;
    for (int i = 0; i < 4; i++) {
      backwardSize |= (paramArrayOfByte[(i + 4)] & 0xFF) << i * 8;
    }
    backwardSize = ((backwardSize + 1L) * 4L);
    return localStreamFlags;
  }
  
  private static StreamFlags decodeStreamFlags(byte[] paramArrayOfByte, int paramInt)
    throws UnsupportedOptionsException
  {
    if ((paramArrayOfByte[paramInt] != 0) || ((paramArrayOfByte[(paramInt + 1)] & 0xFF) >= 16)) {
      throw new UnsupportedOptionsException();
    }
    StreamFlags localStreamFlags = new StreamFlags();
    checkType = paramArrayOfByte[(paramInt + 1)];
    return localStreamFlags;
  }
  
  public static boolean areStreamFlagsEqual(StreamFlags paramStreamFlags1, StreamFlags paramStreamFlags2)
  {
    return checkType == checkType;
  }
  
  public static long decodeVLI(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i == -1) {
      throw new EOFException();
    }
    long l = i & 0x7F;
    int j = 0;
    while ((i & 0x80) != 0)
    {
      j++;
      if (j >= 9) {
        throw new CorruptedInputException();
      }
      i = paramInputStream.read();
      if (i == -1) {
        throw new EOFException();
      }
      if (i == 0) {
        throw new CorruptedInputException();
      }
      l |= (i & 0x7F) << j * 7;
    }
    return l;
  }
}
