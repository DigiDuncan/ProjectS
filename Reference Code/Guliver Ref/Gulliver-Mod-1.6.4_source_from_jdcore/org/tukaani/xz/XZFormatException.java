package org.tukaani.xz;

public class XZFormatException
  extends XZIOException
{
  public XZFormatException()
  {
    super("Input is not in the XZ format");
  }
}
