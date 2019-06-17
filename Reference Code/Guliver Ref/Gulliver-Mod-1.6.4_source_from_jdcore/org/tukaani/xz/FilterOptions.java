package org.tukaani.xz;

public abstract class FilterOptions
  implements Cloneable
{
  abstract FilterEncoder getFilterEncoder();
  
  FilterOptions() {}
}
