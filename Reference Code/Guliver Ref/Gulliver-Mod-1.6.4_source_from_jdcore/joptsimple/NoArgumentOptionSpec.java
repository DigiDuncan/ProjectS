package joptsimple;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

































class NoArgumentOptionSpec
  extends AbstractOptionSpec<Void>
{
  NoArgumentOptionSpec(Collection<String> options, String description)
  {
    super(options, description);
  }
  


  void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
  {
    detectedOptions.add(this);
  }
  
  public boolean acceptsArguments() {
    return false;
  }
  
  public boolean requiresArgument() {
    return false;
  }
  
  public boolean isRequired() {
    return false;
  }
  
  public String argumentDescription() {
    return "";
  }
  
  public String argumentTypeIndicator() {
    return "";
  }
  




  public List<Void> defaultValues()
  {
    return Collections.emptyList();
  }
}
