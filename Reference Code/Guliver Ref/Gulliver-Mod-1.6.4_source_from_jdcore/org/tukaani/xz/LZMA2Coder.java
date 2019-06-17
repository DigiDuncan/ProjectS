package org.tukaani.xz;

abstract class LZMA2Coder
  implements FilterCoder
{
  LZMA2Coder() {}
  
  public boolean changesSize()
  {
    return true;
  }
  
  public boolean nonLastOK()
  {
    return false;
  }
  
  public boolean lastOK()
  {
    return true;
  }
}
