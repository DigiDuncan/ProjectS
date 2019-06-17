package org.tukaani.xz.check;

public class CRC64
  extends Check
{
  private static final long[] crcTable = new long['Ā'];
  private long crc = -1L;
  
  public CRC64()
  {
    size = 8;
    name = "CRC64";
  }
  
  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2;
    while (paramInt1 < i) {
      crc = (crcTable[((paramArrayOfByte[(paramInt1++)] ^ (int)crc) & 0xFF)] ^ crc >>> 8);
    }
  }
  
  public byte[] finish()
  {
    long l = crc ^ 0xFFFFFFFFFFFFFFFF;
    crc = -1L;
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < arrayOfByte.length; i++) {
      arrayOfByte[i] = ((byte)(int)(l >> i * 8));
    }
    return arrayOfByte;
  }
  
  static
  {
    for (int i = 0; i < crcTable.length; i++)
    {
      long l = i;
      for (int j = 0; j < 8; j++) {
        if ((l & 1L) == 1L) {
          l = l >>> 1 ^ 0xC96C5795D7870F42;
        } else {
          l >>>= 1;
        }
      }
      crcTable[i] = l;
    }
  }
}
