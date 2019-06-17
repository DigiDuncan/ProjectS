package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;





























abstract class AbstractByteHasher
  extends AbstractHasher
{
  private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
  

  AbstractByteHasher() {}
  

  protected abstract void update(byte paramByte);
  

  protected void update(byte[] b)
  {
    update(b, 0, b.length);
  }
  


  protected void update(byte[] b, int off, int len)
  {
    for (int i = off; i < off + len; i++) {
      update(b[i]);
    }
  }
  






  public Hasher putBytes(byte[] bytes)
  {
    Preconditions.checkNotNull(bytes);
    update(bytes);
    return this;
  }
}
