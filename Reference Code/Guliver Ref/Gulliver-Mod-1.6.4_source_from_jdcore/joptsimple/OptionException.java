package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



































public abstract class OptionException
  extends RuntimeException
{
  private final List<String> options = new ArrayList();
  
  protected OptionException(Collection<String> options) {
    this.options.addAll(options);
  }
  














  protected final String singleOptionMessage()
  {
    return singleOptionMessage((String)options.get(0));
  }
  
  protected final String singleOptionMessage(String option) {
    return "'" + option + "'";
  }
  
  protected final String multipleOptionMessage() {
    StringBuilder buffer = new StringBuilder("[");
    
    for (Iterator<String> iter = options.iterator(); iter.hasNext();) {
      buffer.append(singleOptionMessage((String)iter.next()));
      if (iter.hasNext()) {
        buffer.append(", ");
      }
    }
    buffer.append(']');
    
    return buffer.toString();
  }
  
  static OptionException unrecognizedOption(String option) {
    return new UnrecognizedOptionException(option);
  }
}
