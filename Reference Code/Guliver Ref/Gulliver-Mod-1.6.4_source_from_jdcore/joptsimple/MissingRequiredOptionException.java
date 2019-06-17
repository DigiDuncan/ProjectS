package joptsimple;

import java.util.Collection;






























class MissingRequiredOptionException
  extends OptionException
{
  protected MissingRequiredOptionException(Collection<String> options)
  {
    super(options);
  }
  
  public String getMessage()
  {
    return "Missing required option(s) " + multipleOptionMessage();
  }
}
