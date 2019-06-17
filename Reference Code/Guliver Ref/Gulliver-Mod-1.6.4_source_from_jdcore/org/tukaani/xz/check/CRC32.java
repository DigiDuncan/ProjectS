package org.tukaani.xz.check;

public class CRC32
  extends Check
{
  private final java.util.zip.CRC32 state = new java.util.zip.CRC32();
  
  public CRC32()
  {
    size = 4;
    name = "CRC32";
  }
  
  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    state.update(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public byte[] finish()
  {
    long l = state.getValue();
    byte[] arrayOfByte = { (byte)(int)l, (byte)(int)(l >>> 8), (byte)(int)(l >>> 16), (byte)(int)(l >>> 24) };
    state.reset();
    return arrayOfByte;
  }
}
