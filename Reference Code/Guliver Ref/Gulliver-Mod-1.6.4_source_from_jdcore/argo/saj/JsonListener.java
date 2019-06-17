package argo.saj;

public abstract interface JsonListener
{
  public abstract void startDocument();
  
  public abstract void endDocument();
  
  public abstract void startArray();
  
  public abstract void endArray();
  
  public abstract void startObject();
  
  public abstract void endObject();
  
  public abstract void startField(String paramString);
  
  public abstract void endField();
  
  public abstract void stringValue(String paramString);
  
  public abstract void numberValue(String paramString);
  
  public abstract void trueValue();
  
  public abstract void falseValue();
  
  public abstract void nullValue();
}
