package org.tukaani.xz.lz;

final class Hash234
  extends CRC32Hash
{
  private final int hash4Mask;
  private final int[] hash2Table = new int['Ѐ'];
  private final int[] hash3Table = new int[65536];
  private final int[] hash4Table;
  private int hash2Value = 0;
  private int hash3Value = 0;
  private int hash4Value = 0;
  
  static int getHash4Size(int paramInt)
  {
    int i = paramInt - 1;
    i |= i >>> 1;
    i |= i >>> 2;
    i |= i >>> 4;
    i |= i >>> 8;
    i >>>= 1;
    i |= 0xFFFF;
    if (i > 16777216) {
      i >>>= 1;
    }
    return i + 1;
  }
  
  Hash234(int paramInt)
  {
    hash4Table = new int[getHash4Size(paramInt)];
    hash4Mask = (hash4Table.length - 1);
  }
  
  void calcHashes(byte[] paramArrayOfByte, int paramInt)
  {
    int i = crcTable[(paramArrayOfByte[paramInt] & 0xFF)] ^ paramArrayOfByte[(paramInt + 1)] & 0xFF;
    hash2Value = (i & 0x3FF);
    i ^= (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8;
    hash3Value = (i & 0xFFFF);
    i ^= crcTable[(paramArrayOfByte[(paramInt + 3)] & 0xFF)] << 5;
    hash4Value = (i & hash4Mask);
  }
  
  int getHash2Pos()
  {
    return hash2Table[hash2Value];
  }
  
  int getHash3Pos()
  {
    return hash3Table[hash3Value];
  }
  
  int getHash4Pos()
  {
    return hash4Table[hash4Value];
  }
  
  void updateTables(int paramInt)
  {
    hash2Table[hash2Value] = paramInt;
    hash3Table[hash3Value] = paramInt;
    hash4Table[hash4Value] = paramInt;
  }
  
  void normalize(int paramInt)
  {
    LZEncoder.normalize(hash2Table, paramInt);
    LZEncoder.normalize(hash3Table, paramInt);
    LZEncoder.normalize(hash4Table, paramInt);
  }
}
