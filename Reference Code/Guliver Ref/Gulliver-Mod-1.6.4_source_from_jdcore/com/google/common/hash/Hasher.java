package com.google.common.hash;

public abstract interface Hasher
{
  public abstract Hasher putBytes(byte[] paramArrayOfByte);
  
  public abstract HashCode hash();
}
