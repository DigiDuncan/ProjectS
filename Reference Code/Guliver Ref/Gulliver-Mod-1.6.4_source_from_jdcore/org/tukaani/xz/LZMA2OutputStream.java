package org.tukaani.xz;

import java.io.DataOutputStream;
import java.io.IOException;
import org.tukaani.xz.lz.LZEncoder;
import org.tukaani.xz.lzma.LZMAEncoder;
import org.tukaani.xz.rangecoder.RangeEncoder;

class LZMA2OutputStream
  extends FinishableOutputStream
{
  private FinishableOutputStream out;
  private final DataOutputStream outData;
  private final LZEncoder lz;
  private final RangeEncoder rc;
  private final LZMAEncoder lzma;
  private final int props;
  private boolean dictResetNeeded = true;
  private boolean stateResetNeeded = true;
  private boolean propsNeeded = true;
  private int pendingSize = 0;
  private boolean finished = false;
  private IOException exception = null;
  
  private static int getExtraSizeBefore(int paramInt)
  {
    return 65536 > paramInt ? 65536 - paramInt : 0;
  }
  
  LZMA2OutputStream(FinishableOutputStream paramFinishableOutputStream, LZMA2Options paramLZMA2Options)
  {
    if (paramFinishableOutputStream == null) {
      throw new NullPointerException();
    }
    out = paramFinishableOutputStream;
    outData = new DataOutputStream(paramFinishableOutputStream);
    rc = new RangeEncoder(65536);
    int i = paramLZMA2Options.getDictSize();
    int j = getExtraSizeBefore(i);
    lzma = LZMAEncoder.getInstance(rc, paramLZMA2Options.getLc(), paramLZMA2Options.getLp(), paramLZMA2Options.getPb(), paramLZMA2Options.getMode(), i, j, paramLZMA2Options.getNiceLen(), paramLZMA2Options.getMatchFinder(), paramLZMA2Options.getDepthLimit());
    lz = lzma.getLZEncoder();
    byte[] arrayOfByte = paramLZMA2Options.getPresetDict();
    if ((arrayOfByte != null) && (arrayOfByte.length > 0))
    {
      lz.setPresetDict(i, arrayOfByte);
      dictResetNeeded = false;
    }
    props = ((paramLZMA2Options.getPb() * 5 + paramLZMA2Options.getLp()) * 9 + paramLZMA2Options.getLc());
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
        int i = lz.fillWindow(paramArrayOfByte, paramInt1, paramInt2);
        paramInt1 += i;
        paramInt2 -= i;
        pendingSize += i;
        if (lzma.encodeForLZMA2()) {
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
    int i = rc.finish();
    int j = lzma.getUncompressedSize();
    assert (i > 0) : i;
    assert (j > 0) : j;
    if (i + 2 < j)
    {
      writeLZMA(j, i);
    }
    else
    {
      lzma.reset();
      j = lzma.getUncompressedSize();
      assert (j > 0) : j;
      writeUncompressed(j);
    }
    pendingSize -= j;
    lzma.resetUncompressedSize();
    rc.reset();
  }
  
  private void writeLZMA(int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if (propsNeeded)
    {
      if (dictResetNeeded) {
        i = 224;
      } else {
        i = 192;
      }
    }
    else if (stateResetNeeded) {
      i = 160;
    } else {
      i = 128;
    }
    i |= paramInt1 - 1 >>> 16;
    outData.writeByte(i);
    outData.writeShort(paramInt1 - 1);
    outData.writeShort(paramInt2 - 1);
    if (propsNeeded) {
      outData.writeByte(props);
    }
    rc.write(out);
    propsNeeded = false;
    stateResetNeeded = false;
    dictResetNeeded = false;
  }
  
  private void writeUncompressed(int paramInt)
    throws IOException
  {
    while (paramInt > 0)
    {
      int i = Math.min(paramInt, 65536);
      outData.writeByte(dictResetNeeded ? 1 : 2);
      outData.writeShort(i - 1);
      lz.copyUncompressed(out, paramInt, i);
      paramInt -= i;
      dictResetNeeded = false;
    }
    stateResetNeeded = true;
  }
  
  private void writeEndMarker()
    throws IOException
  {
    assert (!finished);
    if (exception != null) {
      throw exception;
    }
    lz.setFinishing();
    try
    {
      while (pendingSize > 0)
      {
        lzma.encodeForLZMA2();
        writeChunk();
      }
      out.write(0);
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
    finished = true;
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
      lz.setFlushing();
      while (pendingSize > 0)
      {
        lzma.encodeForLZMA2();
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
