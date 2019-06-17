package org.tukaani.xz;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XZInputStream
  extends InputStream
{
  private final int memoryLimit;
  private InputStream in;
  private SingleXZInputStream xzIn;
  private boolean endReached = false;
  private IOException exception = null;
  
  public XZInputStream(InputStream paramInputStream)
    throws IOException
  {
    this(paramInputStream, -1);
  }
  
  public XZInputStream(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    in = paramInputStream;
    memoryLimit = paramInt;
    xzIn = new SingleXZInputStream(paramInputStream, paramInt);
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
        if (xzIn == null)
        {
          prepareNextStream();
          if (endReached) {
            return i == 0 ? -1 : i;
          }
        }
        int j = xzIn.read(paramArrayOfByte, paramInt1, paramInt2);
        if (j > 0)
        {
          i += j;
          paramInt1 += j;
          paramInt2 -= j;
        }
        else if (j == -1)
        {
          xzIn = null;
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
  
  private void prepareNextStream()
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(in);
    byte[] arrayOfByte = new byte[12];
    do
    {
      int i = localDataInputStream.read(arrayOfByte, 0, 1);
      if (i == -1)
      {
        endReached = true;
        return;
      }
      localDataInputStream.readFully(arrayOfByte, 1, 3);
    } while ((arrayOfByte[0] == 0) && (arrayOfByte[1] == 0) && (arrayOfByte[2] == 0) && (arrayOfByte[3] == 0));
    localDataInputStream.readFully(arrayOfByte, 4, 8);
    try
    {
      xzIn = new SingleXZInputStream(in, memoryLimit, arrayOfByte);
    }
    catch (XZFormatException localXZFormatException)
    {
      throw new CorruptedInputException("Garbage after a valid XZ Stream");
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
    return xzIn == null ? 0 : xzIn.available();
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
