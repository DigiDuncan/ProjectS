package org.tukaani.xz;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.tukaani.xz.check.Check;
import org.tukaani.xz.common.DecoderUtil;
import org.tukaani.xz.common.StreamFlags;
import org.tukaani.xz.index.IndexHash;

public class SingleXZInputStream
  extends InputStream
{
  private InputStream in;
  private int memoryLimit;
  private StreamFlags streamHeaderFlags;
  private Check check;
  private BlockInputStream blockDecoder = null;
  private final IndexHash indexHash = new IndexHash();
  private boolean endReached = false;
  private IOException exception = null;
  
  public SingleXZInputStream(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    initialize(paramInputStream, paramInt);
  }
  
  SingleXZInputStream(InputStream paramInputStream, int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    initialize(paramInputStream, paramInt, paramArrayOfByte);
  }
  
  private void initialize(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[12];
    new DataInputStream(paramInputStream).readFully(arrayOfByte);
    initialize(paramInputStream, paramInt, arrayOfByte);
  }
  
  private void initialize(InputStream paramInputStream, int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    in = paramInputStream;
    memoryLimit = paramInt;
    streamHeaderFlags = DecoderUtil.decodeStreamHeader(paramArrayOfByte);
    check = Check.getInstance(streamHeaderFlags.checkType);
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
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
      throw new IndexOutOfBoundsException();
    }
    if (paramInt2 == 0) {
      return 0;
    }
    if (in == null) {
      throw new XZIOException("Stream closed");
    }
    if (exception != null) {
      throw exception;
    }
    if (endReached) {
      return -1;
    }
    int i = 0;
    try
    {
      while (paramInt2 > 0)
      {
        if (blockDecoder == null) {
          try
          {
            blockDecoder = new BlockInputStream(in, check, memoryLimit, -1L, -1L);
          }
          catch (IndexIndicatorException localIndexIndicatorException)
          {
            indexHash.validate(in);
            validateStreamFooter();
            endReached = true;
            return i > 0 ? i : -1;
          }
        }
        int j = blockDecoder.read(paramArrayOfByte, paramInt1, paramInt2);
        if (j > 0)
        {
          i += j;
          paramInt1 += j;
          paramInt2 -= j;
        }
        else if (j == -1)
        {
          indexHash.add(blockDecoder.getUnpaddedSize(), blockDecoder.getUncompressedSize());
          blockDecoder = null;
        }
      }
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      if (i == 0) {
        throw localIOException;
      }
    }
    return i;
  }
  
  private void validateStreamFooter()
    throws IOException
  {
    byte[] arrayOfByte = new byte[12];
    new DataInputStream(in).readFully(arrayOfByte);
    StreamFlags localStreamFlags = DecoderUtil.decodeStreamFooter(arrayOfByte);
    if ((!DecoderUtil.areStreamFlagsEqual(streamHeaderFlags, localStreamFlags)) || (indexHash.getIndexSize() != backwardSize)) {
      throw new CorruptedInputException("XZ Stream Footer does not match Stream Header");
    }
  }
  
  public int available()
    throws IOException
  {
    if (in == null) {
      throw new XZIOException("Stream closed");
    }
    if (exception != null) {
      throw exception;
    }
    return blockDecoder == null ? 0 : blockDecoder.available();
  }
  
  public void close()
    throws IOException
  {
    if (in != null) {
      try
      {
        in.close();
      }
      finally
      {
        in = null;
      }
    }
  }
}
