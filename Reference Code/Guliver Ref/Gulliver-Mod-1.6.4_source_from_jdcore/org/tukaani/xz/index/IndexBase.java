package org.tukaani.xz.index;

import org.tukaani.xz.XZIOException;
import org.tukaani.xz.common.Util;

abstract class IndexBase
{
  private final XZIOException invalidIndexException;
  long blocksSum = 0L;
  long uncompressedSum = 0L;
  long indexListSize = 0L;
  long recordCount = 0L;
  
  IndexBase(XZIOException paramXZIOException)
  {
    invalidIndexException = paramXZIOException;
  }
  
  private long getUnpaddedIndexSize()
  {
    return 1 + Util.getVLISize(recordCount) + indexListSize + 4L;
  }
  
  public long getIndexSize()
  {
    return getUnpaddedIndexSize() + 3L & 0xFFFFFFFFFFFFFFFC;
  }
  
  public long getStreamSize()
  {
    return 12L + blocksSum + getIndexSize() + 12L;
  }
  
  int getIndexPaddingSize()
  {
    return (int)(4L - getUnpaddedIndexSize() & 0x3);
  }
  
  void add(long paramLong1, long paramLong2)
    throws XZIOException
  {
    blocksSum += (paramLong1 + 3L & 0xFFFFFFFFFFFFFFFC);
    uncompressedSum += paramLong2;
    indexListSize += Util.getVLISize(paramLong1) + Util.getVLISize(paramLong2);
    recordCount += 1L;
    if ((blocksSum < 0L) || (uncompressedSum < 0L) || (getIndexSize() > 17179869184L) || (getStreamSize() < 0L)) {
      throw invalidIndexException;
    }
  }
}
