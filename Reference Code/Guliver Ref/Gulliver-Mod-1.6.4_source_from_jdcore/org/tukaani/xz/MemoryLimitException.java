package org.tukaani.xz;

public class MemoryLimitException
  extends XZIOException
{
  private final int memoryNeeded;
  private final int memoryLimit;
  
  public MemoryLimitException(int paramInt1, int paramInt2)
  {
    super("" + paramInt1 + " KiB of memory would be needed; limit was " + paramInt2 + " KiB");
    memoryNeeded = paramInt1;
    memoryLimit = paramInt2;
  }
}
