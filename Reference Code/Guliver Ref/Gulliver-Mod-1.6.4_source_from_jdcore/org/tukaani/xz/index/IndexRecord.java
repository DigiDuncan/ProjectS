package org.tukaani.xz.index;

class IndexRecord
{
  final long unpadded;
  final long uncompressed;
  
  IndexRecord(long paramLong1, long paramLong2)
  {
    unpadded = paramLong1;
    uncompressed = paramLong2;
  }
}
