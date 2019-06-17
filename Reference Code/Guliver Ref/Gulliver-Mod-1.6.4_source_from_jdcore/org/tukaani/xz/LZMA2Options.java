package org.tukaani.xz;

public class LZMA2Options
  extends FilterOptions
{
  private static final int[] presetToDictSize = { 262144, 1048576, 2097152, 4194304, 4194304, 8388608, 8388608, 16777216, 33554432, 67108864 };
  private static final int[] presetToDepthLimit = { 4, 8, 24, 48 };
  private int dictSize;
  private byte[] presetDict = null;
  private int lc;
  private int lp;
  private int pb;
  private int mode;
  private int niceLen;
  private int mf;
  private int depthLimit;
  
  public LZMA2Options()
  {
    try
    {
      setPreset(6);
    }
    catch (UnsupportedOptionsException localUnsupportedOptionsException)
    {
      if (!$assertionsDisabled) {
        throw new AssertionError();
      }
      throw new RuntimeException();
    }
  }
  
  public void setPreset(int paramInt)
    throws UnsupportedOptionsException
  {
    if ((paramInt < 0) || (paramInt > 9)) {
      throw new UnsupportedOptionsException("Unsupported preset: " + paramInt);
    }
    lc = 3;
    lp = 0;
    pb = 2;
    dictSize = presetToDictSize[paramInt];
    if (paramInt <= 3)
    {
      mode = 1;
      mf = 4;
      niceLen = (paramInt <= 1 ? 128 : 273);
      depthLimit = presetToDepthLimit[paramInt];
    }
    else
    {
      mode = 2;
      mf = 20;
      niceLen = (paramInt == 5 ? 32 : paramInt == 4 ? 16 : 64);
      depthLimit = 0;
    }
  }
  
  public int getDictSize()
  {
    return dictSize;
  }
  
  public byte[] getPresetDict()
  {
    return presetDict;
  }
  
  public int getLc()
  {
    return lc;
  }
  
  public int getLp()
  {
    return lp;
  }
  
  public int getPb()
  {
    return pb;
  }
  
  public int getMode()
  {
    return mode;
  }
  
  public int getNiceLen()
  {
    return niceLen;
  }
  
  public int getMatchFinder()
  {
    return mf;
  }
  
  public int getDepthLimit()
  {
    return depthLimit;
  }
  
  public FinishableOutputStream getOutputStream(FinishableOutputStream paramFinishableOutputStream)
  {
    if (mode == 0) {
      return new UncompressedLZMA2OutputStream(paramFinishableOutputStream);
    }
    return new LZMA2OutputStream(paramFinishableOutputStream, this);
  }
  
  FilterEncoder getFilterEncoder()
  {
    return new LZMA2Encoder(this);
  }
  
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      if (!$assertionsDisabled) {
        throw new AssertionError();
      }
      throw new RuntimeException();
    }
  }
}
