package joptsimple.internal;

import java.util.Map;
import java.util.TreeMap;





















































public class AbbreviationMap<V>
{
  private String key;
  private V value;
  private final Map<Character, AbbreviationMap<V>> children = new TreeMap();
  

  private int keysBeyond;
  


  public AbbreviationMap() {}
  

  public boolean contains(String aKey)
  {
    return get(aKey) != null;
  }
  








  public V get(String aKey)
  {
    char[] chars = charsOf(aKey);
    
    AbbreviationMap<V> child = this;
    for (char each : chars) {
      child = (AbbreviationMap)children.get(Character.valueOf(each));
      if (child == null) {
        return null;
      }
    }
    return value;
  }
  








  public void put(String aKey, V newValue)
  {
    if (newValue == null)
      throw new NullPointerException();
    if (aKey.length() == 0) {
      throw new IllegalArgumentException();
    }
    char[] chars = charsOf(aKey);
    add(chars, newValue, 0, chars.length);
  }
  








  public void putAll(Iterable<String> keys, V newValue)
  {
    for (String each : keys)
      put(each, newValue);
  }
  
  private boolean add(char[] chars, V newValue, int offset, int length) {
    if (offset == length) {
      value = newValue;
      boolean wasAlreadyAKey = key != null;
      key = new String(chars);
      return !wasAlreadyAKey;
    }
    
    char nextChar = chars[offset];
    AbbreviationMap<V> child = (AbbreviationMap)children.get(Character.valueOf(nextChar));
    if (child == null) {
      child = new AbbreviationMap();
      children.put(Character.valueOf(nextChar), child);
    }
    
    boolean newKeyAdded = child.add(chars, newValue, offset + 1, length);
    
    if (newKeyAdded) {
      keysBeyond += 1;
    }
    if (key == null) {
      value = (keysBeyond > 1 ? null : newValue);
    }
    return newKeyAdded;
  }
  
























































  public Map<String, V> toJavaUtilMap()
  {
    Map<String, V> mappings = new TreeMap();
    addToMappings(mappings);
    return mappings;
  }
  
  private void addToMappings(Map<String, V> mappings) {
    if (key != null) {
      mappings.put(key, value);
    }
    for (AbbreviationMap<V> each : children.values())
      each.addToMappings(mappings);
  }
  
  private static char[] charsOf(String aKey) {
    char[] chars = new char[aKey.length()];
    aKey.getChars(0, aKey.length(), chars, 0);
    return chars;
  }
}
