package org.tukaani.xz;

abstract class DeltaCoder
  implements FilterCoder
{
  DeltaCoder() {}
  
  public boolean changesSize()
  {
    return false;
  }
  
  public boolean nonLastOK()
  {
    return true;
  }
  
  public boolean lastOK()
  {
    return false;
  }
}
