package org.tukaani.xz;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class CountingInputStream
  extends FilterInputStream
{
  private long size = 0L;
  
  public CountingInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }
  
  public int read()
    throws IOException
  {
    int i = in.read();
    if ((i != -1) && (size >= 0L)) {
      size += 1L;
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = in.read(paramArrayOfByte, paramInt1, paramInt2);
    if ((i > 0) && (size >= 0L)) {
      size += i;
    }
    return i;
  }
  
  public long getSize()
  {
    return size;
  }
}
