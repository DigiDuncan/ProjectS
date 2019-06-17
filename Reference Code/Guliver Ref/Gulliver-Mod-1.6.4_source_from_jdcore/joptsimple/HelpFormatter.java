package joptsimple;

import java.util.Map;

public abstract interface HelpFormatter
{
  public abstract String format(Map<String, ? extends OptionDescriptor> paramMap);
}
