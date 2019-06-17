package joptsimple;

import java.util.Collections;






























class IllegalOptionSpecificationException
  extends OptionException
{
  IllegalOptionSpecificationException(String option)
  {
    super(Collections.singletonList(option));
  }
  
  public String getMessage()
  {
    return singleOptionMessage() + " is not a legal option character";
  }
}
