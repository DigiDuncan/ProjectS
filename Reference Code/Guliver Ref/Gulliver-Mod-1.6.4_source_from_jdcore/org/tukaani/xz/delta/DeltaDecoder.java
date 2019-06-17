package org.tukaani.xz.delta;

public class DeltaDecoder
  extends DeltaCoder
{
  public DeltaDecoder(int paramInt)
  {
    super(paramInt);
  }
  
  public void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2;
    for (int j = paramInt1; j < i; j++)
    {
      int tmp18_16 = j;
      byte[] tmp18_15 = paramArrayOfByte;
      tmp18_15[tmp18_16] = ((byte)(tmp18_15[tmp18_16] + history[(distance + pos & 0xFF)]));
      history[(pos-- & 0xFF)] = paramArrayOfByte[j];
    }
  }
}
