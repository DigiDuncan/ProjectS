package com.google.common.hash;

public abstract interface HashFunction
{
  public abstract Hasher newHasher();
  
  public abstract HashCode hashBytes(byte[] paramArrayOfByte);
}
