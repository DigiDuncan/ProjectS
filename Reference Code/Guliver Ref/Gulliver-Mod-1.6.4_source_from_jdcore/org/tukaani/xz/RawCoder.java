package org.tukaani.xz;

class RawCoder
{
  static void validate(FilterCoder[] paramArrayOfFilterCoder)
    throws UnsupportedOptionsException
  {
    for (int i = 0; i < paramArrayOfFilterCoder.length - 1; i++) {
      if (!paramArrayOfFilterCoder[i].nonLastOK()) {
        throw new UnsupportedOptionsException("Unsupported XZ filter chain");
      }
    }
    if (!paramArrayOfFilterCoder[(paramArrayOfFilterCoder.length - 1)].lastOK()) {
      throw new UnsupportedOptionsException("Unsupported XZ filter chain");
    }
    i = 0;
    for (int j = 0; j < paramArrayOfFilterCoder.length; j++) {
      if (paramArrayOfFilterCoder[j].changesSize()) {
        i++;
      }
    }
    if (i > 3) {
      throw new UnsupportedOptionsException("Unsupported XZ filter chain");
    }
  }
}
