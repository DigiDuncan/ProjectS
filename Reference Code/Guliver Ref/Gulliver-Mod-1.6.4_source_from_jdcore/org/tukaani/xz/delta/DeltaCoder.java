package org.tukaani.xz.delta;

abstract class DeltaCoder
{
  final int distance;
  final byte[] history = new byte['Ā'];
  int pos = 0;
  
  DeltaCoder(int paramInt)
  {
    if ((paramInt < 1) || (paramInt > 256)) {
      throw new IllegalArgumentException();
    }
    distance = paramInt;
  }
}
