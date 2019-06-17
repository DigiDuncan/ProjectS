package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;















































public abstract class ByteSource
{
  public ByteSource() {}
  
  public CharSource asCharSource(Charset charset)
  {
    return new AsCharSource(charset, null);
  }
  





























































































  private static final byte[] countBuffer = new byte['က'];
  
















  public abstract InputStream openStream()
    throws IOException;
  















  public long copyTo(ByteSink sink)
    throws IOException
  {
    Preconditions.checkNotNull(sink);
    
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      OutputStream out = (OutputStream)closer.register(sink.openStream());
      return ByteStreams.copy(in, out);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  



  public byte[] read()
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return ByteStreams.toByteArray(in);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  























  private final class AsCharSource
    extends CharSource
  {
    private final Charset charset;
    























    private AsCharSource(Charset charset)
    {
      this.charset = ((Charset)Preconditions.checkNotNull(charset));
    }
    
    public Reader openStream() throws IOException
    {
      return new InputStreamReader(openStream(), charset);
    }
    
    public String toString()
    {
      return ByteSource.this.toString() + ".asCharSource(" + charset + ")";
    }
  }
}
