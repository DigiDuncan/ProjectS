package joptsimple;

public abstract interface ValueConverter<V>
{
  public abstract Class<V> valueType();
  
  public abstract String valuePattern();
}
