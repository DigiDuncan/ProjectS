package org.tukaani.xz;

abstract class BCJCoder
  implements FilterCoder
{
  BCJCoder() {}
  
  public static boolean isBCJFilterID(long paramLong)
  {
    return (paramLong >= 4L) && (paramLong <= 9L);
  }
  
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
