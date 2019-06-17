package org.tukaani.xz;

public class CorruptedInputException
  extends XZIOException
{
  public CorruptedInputException()
  {
    super("Compressed data is corrupt");
  }
  
  public CorruptedInputException(String paramString)
  {
    super(paramString);
  }
}
