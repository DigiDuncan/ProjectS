package org.tukaani.xz;

import java.io.InputStream;
import org.tukaani.xz.simple.ARM;
import org.tukaani.xz.simple.ARMThumb;
import org.tukaani.xz.simple.IA64;
import org.tukaani.xz.simple.PowerPC;
import org.tukaani.xz.simple.SPARC;
import org.tukaani.xz.simple.SimpleFilter;
import org.tukaani.xz.simple.X86;

class BCJDecoder
  extends BCJCoder
  implements FilterDecoder
{
  private final long filterID;
  private final int startOffset;
  
  BCJDecoder(long paramLong, byte[] paramArrayOfByte)
    throws UnsupportedOptionsException
  {
    assert (isBCJFilterID(paramLong));
    filterID = paramLong;
    if (paramArrayOfByte.length == 0)
    {
      startOffset = 0;
    }
    else if (paramArrayOfByte.length == 4)
    {
      int i = 0;
      for (int j = 0; j < 4; j++) {
        i |= (paramArrayOfByte[j] & 0xFF) << j * 8;
      }
      startOffset = i;
    }
    else
    {
      throw new UnsupportedOptionsException("Unsupported BCJ filter properties");
    }
  }
  
  public int getMemoryUsage()
  {
    return SimpleInputStream.getMemoryUsage();
  }
  
  public InputStream getInputStream(InputStream paramInputStream)
  {
    Object localObject = null;
    if (filterID == 4L) {
      localObject = new X86(false, startOffset);
    } else if (filterID == 5L) {
      localObject = new PowerPC(false, startOffset);
    } else if (filterID == 6L) {
      localObject = new IA64(false, startOffset);
    } else if (filterID == 7L) {
      localObject = new ARM(false, startOffset);
    } else if (filterID == 8L) {
      localObject = new ARMThumb(false, startOffset);
    } else if (filterID == 9L) {
      localObject = new SPARC(false, startOffset);
    } else if (!$assertionsDisabled) {
      throw new AssertionError();
    }
    return new SimpleInputStream(paramInputStream, (SimpleFilter)localObject);
  }
}
