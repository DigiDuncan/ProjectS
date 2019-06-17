package joptsimple.util;












public final class KeyValuePair
{
  public final String key;
  










  public final String value;
  











  private KeyValuePair(String key, String value)
  {
    this.key = key;
    this.value = value;
  }
  






  public static KeyValuePair valueOf(String asString)
  {
    int equalsIndex = asString.indexOf('=');
    if (equalsIndex == -1) {
      return new KeyValuePair(asString, "");
    }
    String aKey = asString.substring(0, equalsIndex);
    String aValue = equalsIndex == asString.length() - 1 ? "" : asString.substring(equalsIndex + 1);
    
    return new KeyValuePair(aKey, aValue);
  }
  
  public boolean equals(Object that)
  {
    if (!(that instanceof KeyValuePair)) {
      return false;
    }
    KeyValuePair other = (KeyValuePair)that;
    return (key.equals(key)) && (value.equals(value));
  }
  
  public int hashCode()
  {
    return key.hashCode() ^ value.hashCode();
  }
  
  public String toString()
  {
    return key + '=' + value;
  }
}
