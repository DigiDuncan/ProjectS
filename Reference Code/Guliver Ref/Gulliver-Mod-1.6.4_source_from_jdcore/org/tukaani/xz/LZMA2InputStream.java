package org.tukaani.xz;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.tukaani.xz.lz.LZDecoder;
import org.tukaani.xz.lzma.LZMADecoder;
import org.tukaani.xz.rangecoder.RangeDecoder;

public class LZMA2InputStream
  extends InputStream
{
  private DataInputStream in;
  private final LZDecoder lz;
  private final RangeDecoder rc = new RangeDecoder(65536);
  private LZMADecoder lzma;
  private int uncompressedSize = 0;
  private boolean isLZMAChunk;
  private boolean needDictReset = true;
  private boolean needProps = true;
  private boolean endReached = false;
  private IOException exception = null;
  
  public static int getMemoryUsage(int paramInt)
  {
    return 104 + getDictSize(paramInt) / 1024;
  }
  
  private static int getDictSize(int paramInt)
  {
    if ((paramInt < 4096) || (paramInt > 2147483632)) {
      throw new IllegalArgumentException("Unsupported dictionary size " + paramInt);
    }
    return paramInt + 15 & 0xFFFFFFF0;
  }
  
  public LZMA2InputStream(InputStream paramInputStream, int paramInt)
  {
    this(paramInputStream, paramInt, null);
  }
  
  public LZMA2InputStream(InputStream paramInputStream, int paramInt, byte[] paramArrayOfByte)
  {
    if (paramInputStream == null) {
      throw new NullPointerException();
    }
    in = new DataInputStream(paramInputStream);
    lz = new LZDecoder(getDictSize(paramInt), paramArrayOfByte);
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
      needDictReset = false;
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
    try
    {
      int i = 0;
      while (paramInt2 > 0)
      {
        if (uncompressedSize == 0)
        {
          decodeChunkHeader();
          if (endReached) {
            return i == 0 ? -1 : i;
          }
        }
        int j = Math.min(uncompressedSize, paramInt2);
        if (!isLZMAChunk)
        {
          lz.copyUncompressed(in, j);
        }
        else
        {
          lz.setLimit(j);
          lzma.decode();
        }
        int k = lz.flush(paramArrayOfByte, paramInt1);
        paramInt1 += k;
        paramInt2 -= k;
        i += k;
        uncompressedSize -= k;
        if ((uncompressedSize == 0) && ((!rc.isFinished()) || (lz.hasPending()))) {
          throw new CorruptedInputException();
        }
      }
      return i;
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
  }
  
  private void decodeChunkHeader()
    throws IOException
  {
    int i = in.readUnsignedByte();
    if (i == 0)
    {
      endReached = true;
      return;
    }
    if ((i >= 224) || (i == 1))
    {
      needProps = true;
      needDictReset = false;
      lz.reset();
    }
    else if (needDictReset)
    {
      throw new CorruptedInputException();
    }
    if (i >= 128)
    {
      isLZMAChunk = true;
      uncompressedSize = ((i & 0x1F) << 16);
      uncompressedSize += in.readUnsignedShort() + 1;
      int j = in.readUnsignedShort() + 1;
      if (i >= 192)
      {
        needProps = false;
        decodeProps();
      }
      else
      {
        if (needProps) {
          throw new CorruptedInputException();
        }
        if (i >= 160) {
          lzma.reset();
        }
      }
      rc.prepareInputBuffer(in, j);
    }
    else
    {
      if (i > 2) {
        throw new CorruptedInputException();
      }
      isLZMAChunk = false;
      uncompressedSize = (in.readUnsignedShort() + 1);
    }
  }
  
  private void decodeProps()
    throws IOException
  {
    int i = in.readUnsignedByte();
    if (i > 224) {
      throw new CorruptedInputException();
    }
    int j = i / 45;
    i -= j * 9 * 5;
    int k = i / 9;
    int m = i - k * 9;
    if (m + k > 4) {
      throw new CorruptedInputException();
    }
    lzma = new LZMADecoder(lz, rc, m, k, j);
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
    return uncompressedSize;
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
