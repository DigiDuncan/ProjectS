package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Queue;



























public final class LineReader
{
  private final Readable readable;
  private final Reader reader;
  private final char[] buf = new char['က'];
  private final CharBuffer cbuf = CharBuffer.wrap(buf);
  
  private final Queue<String> lines = new LinkedList();
  private final LineBuffer lineBuf = new LineBuffer() {
    protected void handleLine(String line, String end) {
      lines.add(line);
    }
  };
  



  public LineReader(Readable readable)
  {
    Preconditions.checkNotNull(readable);
    this.readable = readable;
    reader = ((readable instanceof Reader) ? (Reader)readable : null);
  }
  









  public String readLine()
    throws IOException
  {
    while (lines.peek() == null) {
      cbuf.clear();
      

      int read = reader != null ? reader.read(buf, 0, buf.length) : readable.read(cbuf);
      

      if (read == -1) {
        lineBuf.finish();
        break;
      }
      lineBuf.add(buf, 0, read);
    }
    return (String)lines.poll();
  }
}
