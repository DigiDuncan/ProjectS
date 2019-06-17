package org.tukaani.xz.lz;

class CRC32Hash
{
  static final int[] crcTable = new int['Ā'];
  
  CRC32Hash() {}
  
  static
  {
    for (int i = 0; i < 256; i++)
    {
      int j = i;
      for (int k = 0; k < 8; k++) {
        if ((j & 0x1) != 0) {
          j = j >>> 1 ^ 0xEDB88320;
        } else {
          j >>>= 1;
        }
      }
      crcTable[i] = j;
    }
  }
}
