package joptsimple;

import java.util.Collections;






























class UnrecognizedOptionException
  extends OptionException
{
  UnrecognizedOptionException(String option)
  {
    super(Collections.singletonList(option));
  }
  
  public String getMessage()
  {
    return singleOptionMessage() + " is not a recognized option";
  }
}
