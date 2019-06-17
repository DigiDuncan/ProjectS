package org.tukaani.xz;

import java.io.IOException;
import java.io.OutputStream;

class CountingOutputStream
  extends FinishableOutputStream
{
  private final OutputStream out;
  private long size = 0L;
  
  public CountingOutputStream(OutputStream paramOutputStream)
  {
    out = paramOutputStream;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    out.write(paramInt);
    if (size >= 0L) {
      size += 1L;
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    out.write(paramArrayOfByte, paramInt1, paramInt2);
    if (size >= 0L) {
      size += paramInt2;
    }
  }
  
  public void flush()
    throws IOException
  {
    out.flush();
  }
  
  public void close()
    throws IOException
  {
    out.close();
  }
  
  public long getSize()
  {
    return size;
  }
}
