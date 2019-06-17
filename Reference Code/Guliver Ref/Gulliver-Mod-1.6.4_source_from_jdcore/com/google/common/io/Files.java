package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;



























































public final class Files
{
  public static BufferedReader newReader(File file, Charset charset)
    throws FileNotFoundException
  {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(charset);
    return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
  }
  









  public static BufferedWriter newWriter(File file, Charset charset)
    throws FileNotFoundException
  {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(charset);
    return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
  }
  





  public static ByteSource asByteSource(File file)
  {
    return new FileByteSource(file, null);
  }
  
  private static final class FileByteSource extends ByteSource
  {
    private final File file;
    
    private FileByteSource(File file) {
      this.file = ((File)Preconditions.checkNotNull(file));
    }
    
    public FileInputStream openStream() throws IOException
    {
      return new FileInputStream(file);
    }
    







    public byte[] read()
      throws IOException
    {
      long size = file.length();
      

      if (size == 0L) {
        return super.read();
      }
      


      if (size > 2147483647L)
      {
        throw new OutOfMemoryError("file is too large to fit in a byte array: " + size + " bytes");
      }
      


      byte[] bytes = new byte[(int)size];
      
      Closer closer = Closer.create();
      try {
        InputStream in = (InputStream)closer.register(openStream());
        int off = 0;
        int read = 0;
        


        while ((off < size) && ((read = in.read(bytes, off, (int)size - off)) != -1)) {
          off += read;
        }
        
        byte[] result = bytes;
        ByteArrayOutputStream out;
        if (off < size)
        {
          result = Arrays.copyOf(bytes, off);
        } else if (read != -1)
        {

          out = new ByteArrayOutputStream();
          ByteStreams.copy(in, out);
          byte[] moreBytes = out.toByteArray();
          result = new byte[bytes.length + moreBytes.length];
          System.arraycopy(bytes, 0, result, 0, bytes.length);
          System.arraycopy(moreBytes, 0, result, bytes.length, moreBytes.length);
        }
        

        return result;
      } catch (Throwable e) {
        throw closer.rethrow(e);
      } finally {
        closer.close();
      }
    }
    
    public String toString()
    {
      return "Files.asByteSource(" + file + ")";
    }
  }
  








  public static ByteSink asByteSink(File file, FileWriteMode... modes)
  {
    return new FileByteSink(file, modes, null);
  }
  
  private static final class FileByteSink extends ByteSink
  {
    private final File file;
    private final ImmutableSet<FileWriteMode> modes;
    
    private FileByteSink(File file, FileWriteMode... modes) {
      this.file = ((File)Preconditions.checkNotNull(file));
      this.modes = ImmutableSet.copyOf(modes);
    }
    
    public FileOutputStream openStream() throws IOException
    {
      return new FileOutputStream(file, modes.contains(FileWriteMode.APPEND));
    }
    
    public String toString()
    {
      return "Files.asByteSink(" + file + ", " + modes + ")";
    }
  }
  












































  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file)
  {
    return newOutputStreamSupplier(file, false);
  }
  









  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file, boolean append)
  {
    return ByteStreams.asOutputSupplier(asByteSink(file, modes(append)));
  }
  
  private static FileWriteMode[] modes(boolean append) {
    return append ? new FileWriteMode[] { FileWriteMode.APPEND } : new FileWriteMode[0];
  }
  





















































  public static byte[] toByteArray(File file)
    throws IOException
  {
    return asByteSource(file).read();
  }
  





















  public static void copy(InputSupplier<? extends InputStream> from, File to)
    throws IOException
  {
    ByteStreams.asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
  }
  





  public static void write(byte[] from, File to)
    throws IOException
  {
    asByteSink(to, new FileWriteMode[0]).write(from);
  }
  



































  public static void copy(File from, File to)
    throws IOException
  {
    Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
    
    asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
  }
}
