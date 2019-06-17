package org.tukaani.xz;

abstract interface FilterEncoder
  extends FilterCoder
{
  public abstract long getFilterID();
  
  public abstract byte[] getFilterProps();
  
  public abstract boolean supportsFlushing();
  
  public abstract FinishableOutputStream getOutputStream(FinishableOutputStream paramFinishableOutputStream);
}
