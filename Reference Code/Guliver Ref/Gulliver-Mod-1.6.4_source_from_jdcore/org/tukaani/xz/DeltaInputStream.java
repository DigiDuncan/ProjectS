package org.tukaani.xz;

import java.io.IOException;
import java.io.InputStream;
import org.tukaani.xz.delta.DeltaDecoder;

public class DeltaInputStream
  extends InputStream
{
  private InputStream in;
  private final DeltaDecoder delta;
  private IOException exception = null;
  
  public DeltaInputStream(InputStream paramInputStream, int paramInt)
  {
    if (paramInputStream == null) {
      throw new NullPointerException();
    }
    in = paramInputStream;
    delta = new DeltaDecoder(paramInt);
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
    if (paramInt2 == 0) {
      return 0;
    }
    if (in == null) {
      throw new XZIOException("Stream closed");
    }
    if (exception != null) {
      throw exception;
    }
    int i;
    try
    {
      i = in.read(paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
    if (i == -1) {
      return -1;
    }
    delta.decode(paramArrayOfByte, paramInt1, i);
    return i;
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
    return in.available();
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
