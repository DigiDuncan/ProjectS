package org.tukaani.xz;

abstract interface FilterCoder
{
  public abstract boolean changesSize();
  
  public abstract boolean nonLastOK();
  
  public abstract boolean lastOK();
}
