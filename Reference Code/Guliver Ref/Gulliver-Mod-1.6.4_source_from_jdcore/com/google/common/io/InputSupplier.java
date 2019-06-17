package com.google.common.io;

import java.io.IOException;

public abstract interface InputSupplier<T>
{
  public abstract T getInput()
    throws IOException;
}
