package org.tukaani.xz;

import org.tukaani.xz.lzma.LZMAEncoder;

class LZMA2Encoder
  extends LZMA2Coder
  implements FilterEncoder
{
  private final LZMA2Options options;
  private final byte[] props = new byte[1];
  
  LZMA2Encoder(LZMA2Options paramLZMA2Options)
  {
    if (paramLZMA2Options.getPresetDict() != null) {
      throw new IllegalArgumentException("XZ doesn't support a preset dictionary for now");
    }
    if (paramLZMA2Options.getMode() == 0)
    {
      props[0] = 0;
    }
    else
    {
      int i = Math.max(paramLZMA2Options.getDictSize(), 4096);
      props[0] = ((byte)(LZMAEncoder.getDistSlot(i - 1) - 23));
    }
    options = ((LZMA2Options)paramLZMA2Options.clone());
  }
  
  public long getFilterID()
  {
    return 33L;
  }
  
  public byte[] getFilterProps()
  {
    return props;
  }
  
  public boolean supportsFlushing()
  {
    return true;
  }
  
  public FinishableOutputStream getOutputStream(FinishableOutputStream paramFinishableOutputStream)
  {
    return options.getOutputStream(paramFinishableOutputStream);
  }
}
