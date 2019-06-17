package com.google.common.io;

import java.io.IOException;
import java.io.Reader;

public abstract class CharSource
{
  public CharSource() {}
  
  public abstract Reader openStream()
    throws IOException;
}
