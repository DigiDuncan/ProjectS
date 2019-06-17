package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;







































































public abstract class ByteSink
{
  public ByteSink() {}
  
  public abstract OutputStream openStream()
    throws IOException;
  
  public void write(byte[] bytes)
    throws IOException
  {
    Preconditions.checkNotNull(bytes);
    
    Closer closer = Closer.create();
    try {
      OutputStream out = (OutputStream)closer.register(openStream());
      out.write(bytes);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  





  public long writeFrom(InputStream input)
    throws IOException
  {
    Preconditions.checkNotNull(input);
    
    Closer closer = Closer.create();
    try {
      OutputStream out = (OutputStream)closer.register(openStream());
      return ByteStreams.copy(input, out);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
}
