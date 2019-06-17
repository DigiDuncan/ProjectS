package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;























































































































































public final class CharStreams
{
  public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> in, Charset charset)
  {
    return asInputSupplier(ByteStreams.asByteSource(in).asCharSource(charset));
  }
  
















































































































































  public static <R extends Readable,  extends Closeable> List<String> readLines(InputSupplier<R> supplier)
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      R r = (Readable)closer.register((Closeable)supplier.getInput());
      return readLines(r);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  











  public static List<String> readLines(Readable r)
    throws IOException
  {
    List<String> result = new ArrayList();
    LineReader lineReader = new LineReader(r);
    String line;
    while ((line = lineReader.readLine()) != null) {
      result.add(line);
    }
    return result;
  }
  























































































































































  static <R extends Reader> InputSupplier<R> asInputSupplier(CharSource source)
  {
    Preconditions.checkNotNull(source);
    new InputSupplier()
    {
      public R getInput() throws IOException {
        return val$source.openStream();
      }
    };
  }
}
