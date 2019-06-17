package org.tukaani.xz;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.tukaani.xz.check.Check;
import org.tukaani.xz.common.DecoderUtil;

class BlockInputStream
  extends InputStream
{
  private final InputStream in;
  private final DataInputStream inData;
  private final CountingInputStream inCounted;
  private InputStream filterChain;
  private final Check check;
  private long uncompressedSizeInHeader = -1L;
  private long compressedSizeInHeader = -1L;
  private long compressedSizeLimit;
  private final int headerSize;
  private long uncompressedSize = 0L;
  private boolean endReached = false;
  
  public BlockInputStream(InputStream paramInputStream, Check paramCheck, int paramInt, long paramLong1, long paramLong2)
    throws IOException, IndexIndicatorException
  {
    in = paramInputStream;
    check = paramCheck;
    inData = new DataInputStream(paramInputStream);
    byte[] arrayOfByte = new byte['Ѐ'];
    inData.readFully(arrayOfByte, 0, 1);
    if (arrayOfByte[0] == 0) {
      throw new IndexIndicatorException();
    }
    headerSize = (4 * ((arrayOfByte[0] & 0xFF) + 1));
    inData.readFully(arrayOfByte, 1, headerSize - 1);
    if (!DecoderUtil.isCRC32Valid(arrayOfByte, 0, headerSize - 4, headerSize - 4)) {
      throw new CorruptedInputException("XZ Block Header is corrupt");
    }
    if ((arrayOfByte[1] & 0x3C) != 0) {
      throw new UnsupportedOptionsException("Unsupported options in XZ Block Header");
    }
    int i = (arrayOfByte[1] & 0x3) + 1;
    long[] arrayOfLong = new long[i];
    byte[][] arrayOfByte1 = new byte[i][];
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte, 2, headerSize - 6);
    long l;
    try
    {
      compressedSizeLimit = (9223372036854775804L - headerSize - paramCheck.getSize());
      if ((arrayOfByte[1] & 0x40) != 0)
      {
        compressedSizeInHeader = DecoderUtil.decodeVLI(localByteArrayInputStream);
        if ((compressedSizeInHeader == 0L) || (compressedSizeInHeader > compressedSizeLimit)) {
          throw new CorruptedInputException();
        }
        compressedSizeLimit = compressedSizeInHeader;
      }
      if ((arrayOfByte[1] & 0x80) != 0) {
        uncompressedSizeInHeader = DecoderUtil.decodeVLI(localByteArrayInputStream);
      }
      for (int j = 0; j < i; j++)
      {
        arrayOfLong[j] = DecoderUtil.decodeVLI(localByteArrayInputStream);
        l = DecoderUtil.decodeVLI(localByteArrayInputStream);
        if (l > localByteArrayInputStream.available()) {
          throw new CorruptedInputException();
        }
        arrayOfByte1[j] = new byte[(int)l];
        localByteArrayInputStream.read(arrayOfByte1[j]);
      }
    }
    catch (IOException localIOException)
    {
      throw new CorruptedInputException("XZ Block Header is corrupt");
    }
    for (int k = localByteArrayInputStream.available(); k > 0; k--) {
      if (localByteArrayInputStream.read() != 0) {
        throw new UnsupportedOptionsException("Unsupported options in XZ Block Header");
      }
    }
    if (paramLong1 != -1L)
    {
      k = headerSize + paramCheck.getSize();
      if (k >= paramLong1) {
        throw new CorruptedInputException("XZ Index does not match a Block Header");
      }
      l = paramLong1 - k;
      if ((l > compressedSizeLimit) || ((compressedSizeInHeader != -1L) && (compressedSizeInHeader != l))) {
        throw new CorruptedInputException("XZ Index does not match a Block Header");
      }
      if ((uncompressedSizeInHeader != -1L) && (uncompressedSizeInHeader != paramLong2)) {
        throw new CorruptedInputException("XZ Index does not match a Block Header");
      }
      compressedSizeLimit = l;
      compressedSizeInHeader = l;
      uncompressedSizeInHeader = paramLong2;
    }
    FilterDecoder[] arrayOfFilterDecoder = new FilterDecoder[arrayOfLong.length];
    for (int m = 0; m < arrayOfFilterDecoder.length; m++) {
      if (arrayOfLong[m] == 33L) {
        arrayOfFilterDecoder[m] = new LZMA2Decoder(arrayOfByte1[m]);
      } else if (arrayOfLong[m] == 3L) {
        arrayOfFilterDecoder[m] = new DeltaDecoder(arrayOfByte1[m]);
      } else if (BCJDecoder.isBCJFilterID(arrayOfLong[m])) {
        arrayOfFilterDecoder[m] = new BCJDecoder(arrayOfLong[m], arrayOfByte1[m]);
      } else {
        throw new UnsupportedOptionsException("Unknown Filter ID " + arrayOfLong[m]);
      }
    }
    RawCoder.validate(arrayOfFilterDecoder);
    if (paramInt >= 0)
    {
      m = 0;
      for (int n = 0; n < arrayOfFilterDecoder.length; n++) {
        m += arrayOfFilterDecoder[n].getMemoryUsage();
      }
      if (m > paramInt) {
        throw new MemoryLimitException(m, paramInt);
      }
    }
    inCounted = new CountingInputStream(paramInputStream);
    filterChain = inCounted;
    for (m = arrayOfFilterDecoder.length - 1; m >= 0; m--) {
      filterChain = arrayOfFilterDecoder[m].getInputStream(filterChain);
    }
  }
  
  public int read()
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    return read(arrayOfByte, 0, 1) == -1 ? -1 : arrayOfByte[0] & 0xFF;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (endReached) {
      return -1;
    }
    int i = filterChain.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i > 0)
    {
      check.update(paramArrayOfByte, paramInt1, i);
      uncompressedSize += i;
      long l = inCounted.getSize();
      if ((l < 0L) || (l > compressedSizeLimit) || (uncompressedSize < 0L) || ((uncompressedSizeInHeader != -1L) && (uncompressedSize > uncompressedSizeInHeader))) {
        throw new CorruptedInputException();
      }
      if ((i < paramInt2) || (uncompressedSize == uncompressedSizeInHeader))
      {
        if (filterChain.read() != -1) {
          throw new CorruptedInputException();
        }
        validate();
        endReached = true;
      }
    }
    else if (i == -1)
    {
      validate();
      endReached = true;
    }
    return i;
  }
  
  private void validate()
    throws IOException
  {
    long l = inCounted.getSize();
    if (((compressedSizeInHeader != -1L) && (compressedSizeInHeader != l)) || ((uncompressedSizeInHeader != -1L) && (uncompressedSizeInHeader != uncompressedSize))) {
      throw new CorruptedInputException();
    }
    while ((l++ & 0x3) != 0L) {
      if (inData.readUnsignedByte() != 0) {
        throw new CorruptedInputException();
      }
    }
    byte[] arrayOfByte = new byte[check.getSize()];
    inData.readFully(arrayOfByte);
    if (!Arrays.equals(check.finish(), arrayOfByte)) {
      throw new CorruptedInputException("Integrity check (" + check.getName() + ") does not match");
    }
  }
  
  public int available()
    throws IOException
  {
    return filterChain.available();
  }
  
  public long getUnpaddedSize()
  {
    return headerSize + inCounted.getSize() + check.getSize();
  }
  
  public long getUncompressedSize()
  {
    return uncompressedSize;
  }
}
