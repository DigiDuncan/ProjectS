package argo.jdom;







public final class JsonField
{
  private final JsonStringNode name;
  





  private final JsonNode value;
  





  public JsonField(JsonStringNode name, JsonNode value)
  {
    if (name == null) {
      throw new NullPointerException("Attempt to construct a JsonField with a null name.");
    }
    this.name = name;
    if (value == null) {
      throw new NullPointerException("Attempt to construct a JsonField with a null value.");
    }
    this.value = value;
  }
  


  public JsonStringNode getName()
  {
    return name;
  }
  


  public JsonNode getValue()
  {
    return value;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    JsonField jsonField = (JsonField)o;
    
    return (name.equals(name)) && (value.equals(value));
  }
  

  public int hashCode()
  {
    int result = name.hashCode();
    result = 31 * result + value.hashCode();
    return result;
  }
  
  public String toString()
  {
    return "JsonField{name=" + name + ", value=" + value + '}';
  }
}
