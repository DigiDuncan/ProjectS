package com.google.common.io;

import java.io.IOException;

public abstract interface OutputSupplier<T>
{
  public abstract T getOutput()
    throws IOException;
}
