package org.tukaani.xz;

import java.io.IOException;
import java.io.OutputStream;

public abstract class FinishableOutputStream
  extends OutputStream
{
  public FinishableOutputStream() {}
  
  public void finish()
    throws IOException
  {}
}
