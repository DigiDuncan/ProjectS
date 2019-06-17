package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;



































public class OptionSet
{
  private final List<OptionSpec<?>> detectedSpecs;
  private final Map<String, AbstractOptionSpec<?>> detectedOptions;
  private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
  private final Map<String, List<?>> defaultValues;
  
  OptionSet(Map<String, List<?>> defaults)
  {
    detectedSpecs = new ArrayList();
    detectedOptions = new HashMap();
    optionsToArguments = new IdentityHashMap();
    defaultValues = new HashMap(defaults);
  }
  
































  public boolean has(OptionSpec<?> option)
  {
    return optionsToArguments.containsKey(option);
  }
  








































































































































  public List<OptionSpec<?>> specs()
  {
    List<OptionSpec<?>> specs = detectedSpecs;
    specs.remove(detectedOptions.get("[arguments]"));
    
    return Collections.unmodifiableList(specs);
  }
  






  void add(AbstractOptionSpec<?> spec)
  {
    addWithArgument(spec, null);
  }
  
  void addWithArgument(AbstractOptionSpec<?> spec, String argument) {
    detectedSpecs.add(spec);
    
    for (String each : spec.options()) {
      detectedOptions.put(each, spec);
    }
    List<String> optionArguments = (List)optionsToArguments.get(spec);
    
    if (optionArguments == null) {
      optionArguments = new ArrayList();
      optionsToArguments.put(spec, optionArguments);
    }
    
    if (argument != null) {
      optionArguments.add(argument);
    }
  }
  
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if ((that == null) || (!getClass().equals(that.getClass()))) {
      return false;
    }
    OptionSet other = (OptionSet)that;
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap(optionsToArguments);
    
    Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = new HashMap(optionsToArguments);
    
    return (detectedOptions.equals(detectedOptions)) && (thisOptionsToArguments.equals(otherOptionsToArguments));
  }
  

  public int hashCode()
  {
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap(optionsToArguments);
    
    return detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
  }
}
