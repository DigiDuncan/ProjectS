package joptsimple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;















































public class NonOptionArgumentSpec<V>
  extends AbstractOptionSpec<V>
{
  private ValueConverter<V> converter;
  private String argumentDescription = "";
  
  NonOptionArgumentSpec() {
    this("");
  }
  
  NonOptionArgumentSpec(String description) {
    super(Arrays.asList(new String[] { "[arguments]" }), description);
  }
  







































































  void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
  {
    detectedOptions.addWithArgument(this, detectedArgument);
  }
  
  public List<?> defaultValues() {
    return Collections.emptyList();
  }
  
  public boolean isRequired() {
    return false;
  }
  
  public boolean acceptsArguments() {
    return false;
  }
  
  public boolean requiresArgument() {
    return false;
  }
  
  public String argumentDescription() {
    return argumentDescription;
  }
  
  public String argumentTypeIndicator() {
    return argumentTypeIndicatorFrom(converter);
  }
  
  public boolean representsNonOptions() {
    return true;
  }
}
