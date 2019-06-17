package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


































abstract class AbstractOptionSpec<V>
  implements OptionDescriptor, OptionSpec<V>
{
  private final List<String> options = new ArrayList();
  
  private final String description;
  
  private boolean forHelp;
  

  protected AbstractOptionSpec(Collection<String> options, String description)
  {
    arrangeOptions(options);
    
    this.description = description;
  }
  
  public final Collection<String> options() {
    return Collections.unmodifiableList(options);
  }
  







  public String description()
  {
    return description;
  }
  




  public final boolean isForHelp()
  {
    return forHelp;
  }
  
  public boolean representsNonOptions() {
    return false;
  }
  













  protected String argumentTypeIndicatorFrom(ValueConverter<V> converter)
  {
    if (converter == null) {
      return null;
    }
    String pattern = converter.valuePattern();
    return pattern == null ? converter.valueType().getName() : pattern;
  }
  
  abstract void handleOption(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet, String paramString);
  
  private void arrangeOptions(Collection<String> unarranged)
  {
    if (unarranged.size() == 1) {
      options.addAll(unarranged);
      return;
    }
    
    List<String> shortOptions = new ArrayList();
    List<String> longOptions = new ArrayList();
    
    for (String each : unarranged) {
      if (each.length() == 1) {
        shortOptions.add(each);
      } else {
        longOptions.add(each);
      }
    }
    Collections.sort(shortOptions);
    Collections.sort(longOptions);
    
    options.addAll(shortOptions);
    options.addAll(longOptions);
  }
  
  public boolean equals(Object that)
  {
    if (!(that instanceof AbstractOptionSpec)) {
      return false;
    }
    AbstractOptionSpec<?> other = (AbstractOptionSpec)that;
    return options.equals(options);
  }
  
  public int hashCode()
  {
    return options.hashCode();
  }
  
  public String toString()
  {
    return options.toString();
  }
}
