package org.tukaani.xz;

import java.io.IOException;
import java.io.OutputStream;
import org.tukaani.xz.check.Check;
import org.tukaani.xz.common.EncoderUtil;
import org.tukaani.xz.common.StreamFlags;
import org.tukaani.xz.index.IndexEncoder;

public class XZOutputStream
  extends FinishableOutputStream
{
  private OutputStream out;
  private final StreamFlags streamFlags = new StreamFlags();
  private final Check check;
  private final IndexEncoder index = new IndexEncoder();
  private BlockOutputStream blockEncoder = null;
  private FilterEncoder[] filters;
  private boolean filtersSupportFlushing;
  private IOException exception = null;
  private boolean finished = false;
  
  public XZOutputStream(OutputStream paramOutputStream, FilterOptions paramFilterOptions)
    throws IOException
  {
    this(paramOutputStream, paramFilterOptions, 4);
  }
  
  public XZOutputStream(OutputStream paramOutputStream, FilterOptions paramFilterOptions, int paramInt)
    throws IOException
  {
    this(paramOutputStream, new FilterOptions[] { paramFilterOptions }, paramInt);
  }
  
  public XZOutputStream(OutputStream paramOutputStream, FilterOptions[] paramArrayOfFilterOptions, int paramInt)
    throws IOException
  {
    out = paramOutputStream;
    updateFilters(paramArrayOfFilterOptions);
    streamFlags.checkType = paramInt;
    check = Check.getInstance(paramInt);
    encodeStreamHeader();
  }
  
  public void updateFilters(FilterOptions[] paramArrayOfFilterOptions)
    throws XZIOException
  {
    if (blockEncoder != null) {
      throw new UnsupportedOptionsException("Changing filter options in the middle of a XZ Block not implemented");
    }
    if ((paramArrayOfFilterOptions.length < 1) || (paramArrayOfFilterOptions.length > 4)) {
      throw new UnsupportedOptionsException("XZ filter chain must be 1-4 filters");
    }
    filtersSupportFlushing = true;
    FilterEncoder[] arrayOfFilterEncoder = new FilterEncoder[paramArrayOfFilterOptions.length];
    for (int i = 0; i < paramArrayOfFilterOptions.length; i++)
    {
      arrayOfFilterEncoder[i] = paramArrayOfFilterOptions[i].getFilterEncoder();
      filtersSupportFlushing &= arrayOfFilterEncoder[i].supportsFlushing();
    }
    RawCoder.validate(arrayOfFilterEncoder);
    filters = arrayOfFilterEncoder;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = { (byte)paramInt };
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
      if (blockEncoder == null) {
        blockEncoder = new BlockOutputStream(out, filters, check);
      }
      blockEncoder.write(paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
      exception = localIOException;
      throw localIOException;
    }
  }
  
  public void endBlock()
    throws IOException
  {
    if (exception != null) {
      throw exception;
    }
    if (finished) {
      throw new XZIOException("Stream finished or closed");
    }
    if (blockEncoder != null) {
      try
      {
        blockEncoder.finish();
        index.add(blockEncoder.getUnpaddedSize(), blockEncoder.getUncompressedSize());
        blockEncoder = null;
      }
      catch (IOException localIOException)
      {
        exception = localIOException;
        throw localIOException;
      }
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
      if (blockEncoder != null)
      {
        if (filtersSupportFlushing)
        {
          blockEncoder.flush();
        }
        else
        {
          endBlock();
          out.flush();
        }
      }
      else {
        out.flush();
      }
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
      endBlock();
      try
      {
        index.encode(out);
        encodeStreamFooter();
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
      try
      {
        finish();
      }
      catch (IOException localIOException1) {}
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
  
  private void encodeStreamFlags(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = 0;
    paramArrayOfByte[(paramInt + 1)] = ((byte)streamFlags.checkType);
  }
  
  private void encodeStreamHeader()
    throws IOException
  {
    out.write(XZ.HEADER_MAGIC);
    byte[] arrayOfByte = new byte[2];
    encodeStreamFlags(arrayOfByte, 0);
    out.write(arrayOfByte);
    EncoderUtil.writeCRC32(out, arrayOfByte);
  }
  
  private void encodeStreamFooter()
    throws IOException
  {
    byte[] arrayOfByte = new byte[6];
    long l = index.getIndexSize() / 4L - 1L;
    for (int i = 0; i < 4; i++) {
      arrayOfByte[i] = ((byte)(int)(l >>> i * 8));
    }
    encodeStreamFlags(arrayOfByte, 4);
    EncoderUtil.writeCRC32(out, arrayOfByte);
    out.write(arrayOfByte);
    out.write(XZ.FOOTER_MAGIC);
  }
}
