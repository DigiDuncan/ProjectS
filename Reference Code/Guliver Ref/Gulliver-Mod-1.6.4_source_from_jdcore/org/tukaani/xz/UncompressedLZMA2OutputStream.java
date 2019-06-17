package org.tukaani.xz;

import java.io.DataOutputStream;
import java.io.IOException;

class UncompressedLZMA2OutputStream
  extends FinishableOutputStream
{
  private FinishableOutputStream out;
  private final DataOutputStream outData;
  private final byte[] uncompBuf = new byte[65536];
  private int uncompPos = 0;
  private boolean dictResetNeeded = true;
  private boolean finished = false;
  private IOException exception = null;
  
  UncompressedLZMA2OutputStream(FinishableOutputStream paramFinishableOutputStream)
  {
    if (paramFinishableOutputStream == null) {
      throw new NullPointerException();
    }
    out = paramFinishableOutputStream;
    outData = new DataOutputStream(paramFinishableOutputStream);
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
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
      throw new IndexOutOfBoundsException();
    }
    if (exception != null) {
      throw exception;
    }
    if (finished) {
      throw new XZIOException("Stream finished or closed");
    }
    try
    {
      while (paramInt2 > 0)
      {
        int i = Math.min(uncompBuf.length - uncompPos, paramInt2);
        System.arraycopy(paramArrayOfByte, paramInt1, uncompBuf, uncompPos, i);
        paramInt2 -= i;
        uncompPos += i;
        if (uncompPos == uncompBuf.length) {
          writeChunk();
        }
      }
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
  }
  
  private void writeChunk()
    throws IOException
  {
    outData.writeByte(dictResetNeeded ? 1 : 2);
    outData.writeShort(uncompPos - 1);
    outData.write(uncompBuf, 0, uncompPos);
    uncompPos = 0;
    dictResetNeeded = false;
  }
  
  private void writeEndMarker()
    throws IOException
  {
    if (exception != null) {
      throw exception;
    }
    if (finished) {
      throw new XZIOException("Stream finished or closed");
    }
    try
    {
      if (uncompPos > 0) {
        writeChunk();
      }
      out.write(0);
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
  }
  
  public void flush()
    throws IOException
  {
    if (exception != null) {
      throw exception;
    }
    if (finished) {
      throw new XZIOException("Stream finished or closed");
    }
    try
    {
      if (uncompPos > 0) {
        writeChunk();
      }
      out.flush();
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
  }
  
  public void finish()
    throws IOException
  {
    if (!finished)
    {
      writeEndMarker();
      try
      {
        out.finish();
      }
      catch (IOException localIOException)
      {
        exception = localIOException;
        throw localIOException;
      }
      finished = true;
    }
  }
  
  public void close()
    throws IOException
  {
    if (out != null)
    {
      if (!finished) {
        try
        {
          writeEndMarker();
        }
        catch (IOException localIOException1) {}
      }
      try
      {
        out.close();
      }
      catch (IOException localIOException2)
      {
        if (exception == null) {
          exception = localIOException2;
        }
      }
      out = null;
    }
    if (exception != null) {
      throw exception;
    }
  }
}
