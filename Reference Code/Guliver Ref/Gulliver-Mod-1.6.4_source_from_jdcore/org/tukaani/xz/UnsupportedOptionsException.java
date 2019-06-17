package org.tukaani.xz;

public class UnsupportedOptionsException
  extends XZIOException
{
  public UnsupportedOptionsException() {}
  
  public UnsupportedOptionsException(String paramString)
  {
    super(paramString);
  }
}
