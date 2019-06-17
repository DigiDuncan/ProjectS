package com.google.common.base;

import javax.annotation.Nullable;

public abstract interface Function<F, T>
{
  @Nullable
  public abstract T apply(@Nullable F paramF);
}
