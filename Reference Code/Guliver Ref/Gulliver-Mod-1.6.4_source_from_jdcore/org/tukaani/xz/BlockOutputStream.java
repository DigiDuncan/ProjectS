package org.tukaani.xz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.tukaani.xz.check.Check;
import org.tukaani.xz.common.EncoderUtil;

class BlockOutputStream
  extends FinishableOutputStream
{
  private final OutputStream out;
  private final CountingOutputStream outCounted;
  private FinishableOutputStream filterChain;
  private final Check check;
  private final int headerSize;
  private final long compressedSizeLimit;
  private long uncompressedSize = 0L;
  
  public BlockOutputStream(OutputStream paramOutputStream, FilterEncoder[] paramArrayOfFilterEncoder, Check paramCheck)
    throws IOException
  {
    out = paramOutputStream;
    check = paramCheck;
    outCounted = new CountingOutputStream(paramOutputStream);
    filterChain = outCounted;
    for (int i = paramArrayOfFilterEncoder.length - 1; i >= 0; i--) {
      filterChain = paramArrayOfFilterEncoder[i].getOutputStream(filterChain);
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(0);
    localByteArrayOutputStream.write(paramArrayOfFilterEncoder.length - 1);
    for (int j = 0; j < paramArrayOfFilterEncoder.length; j++)
    {
      EncoderUtil.encodeVLI(localByteArrayOutputStream, paramArrayOfFilterEncoder[j].getFilterID());
      byte[] arrayOfByte2 = paramArrayOfFilterEncoder[j].getFilterProps();
      EncoderUtil.encodeVLI(localByteArrayOutputStream, arrayOfByte2.length);
      localByteArrayOutputStream.write(arrayOfByte2);
    }
    while ((localByteArrayOutputStream.size() & 0x3) != 0) {
      localByteArrayOutputStream.write(0);
    }
    byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    headerSize = (arrayOfByte1.length + 4);
    if (headerSize > 1024) {
      throw new UnsupportedOptionsException();
    }
    arrayOfByte1[0] = ((byte)(arrayOfByte1.length / 4));
    paramOutputStream.write(arrayOfByte1);
    EncoderUtil.writeCRC32(paramOutputStream, arrayOfByte1);
    compressedSizeLimit = (9223372036854775804L - headerSize - paramCheck.getSize());
  }
  
  public void write(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = ((byte)paramInt);
    write(arrayOfByte, 0, 1);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    filterChain.write(paramArrayOfByte, paramInt1, paramInt2);
    check.update(paramArrayOfByte, paramInt1, paramInt2);
    uncompressedSize += paramInt2;
    validate();
  }
  
  public void flush()
    throws IOException
  {
    filterChain.flush();
    validate();
  }
  
  public void finish()
    throws IOException
  {
    filterChain.finish();
    validate();
    for (long l = outCounted.getSize(); (l & 0x3) != 0L; l += 1L) {
      out.write(0);
    }
    out.write(check.finish());
  }
  
  private void validate()
    throws IOException
  {
    long l = outCounted.getSize();
    if ((l < 0L) || (l > compressedSizeLimit) || (uncompressedSize < 0L)) {
      throw new XZIOException("XZ Stream has grown too big");
    }
  }
  
  public long getUnpaddedSize()
  {
    return headerSize + outCounted.getSize() + check.getSize();
  }
  
  public long getUncompressedSize()
  {
    return uncompressedSize;
  }
}
