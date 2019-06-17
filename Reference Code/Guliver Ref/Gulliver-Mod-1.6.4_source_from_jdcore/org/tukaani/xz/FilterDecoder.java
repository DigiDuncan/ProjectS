package org.tukaani.xz;

import java.io.InputStream;

abstract interface FilterDecoder
  extends FilterCoder
{
  public abstract int getMemoryUsage();
  
  public abstract InputStream getInputStream(InputStream paramInputStream);
}
