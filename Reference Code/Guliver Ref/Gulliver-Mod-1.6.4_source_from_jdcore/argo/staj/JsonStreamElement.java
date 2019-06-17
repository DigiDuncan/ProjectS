package argo.staj;














public abstract class JsonStreamElement
{
  private static final JsonStreamElement START_DOCUMENT = nonTextJsonStreamElement(JsonStreamElementType.START_DOCUMENT);
  private static final JsonStreamElement END_DOCUMENT = nonTextJsonStreamElement(JsonStreamElementType.END_DOCUMENT);
  private static final JsonStreamElement START_ARRAY = nonTextJsonStreamElement(JsonStreamElementType.START_ARRAY);
  private static final JsonStreamElement END_ARRAY = nonTextJsonStreamElement(JsonStreamElementType.END_ARRAY);
  private static final JsonStreamElement START_OBJECT = nonTextJsonStreamElement(JsonStreamElementType.START_OBJECT);
  private static final JsonStreamElement END_OBJECT = nonTextJsonStreamElement(JsonStreamElementType.END_OBJECT);
  private static final JsonStreamElement END_FIELD = nonTextJsonStreamElement(JsonStreamElementType.END_FIELD);
  private static final JsonStreamElement TRUE = nonTextJsonStreamElement(JsonStreamElementType.TRUE);
  private static final JsonStreamElement FALSE = nonTextJsonStreamElement(JsonStreamElementType.FALSE);
  private static final JsonStreamElement NULL = nonTextJsonStreamElement(JsonStreamElementType.NULL);
  private final JsonStreamElementType jsonStreamElementType;
  
  private static JsonStreamElement nonTextJsonStreamElement(final JsonStreamElementType jsonStreamElementType) { new JsonStreamElement(jsonStreamElementType, jsonStreamElementType)
    {


      public String text()
      {


        throw new IllegalStateException(jsonStreamElementType().name() + " does not have text associated with it");
      }
      
      public String toString()
      {
        return "JsonStreamElement jsonStreamElementType: " + jsonStreamElementType + "";
      }
    }; }
  
  private static JsonStreamElement textJsonStreamElement(final JsonStreamElementType jsonStreamElementType, final String text)
  {
    new JsonStreamElement(jsonStreamElementType, text)
    {


      public String text()
      {


        return text;
      }
      
      public String toString()
      {
        return "JsonStreamElement jsonStreamElementType: " + jsonStreamElementType + ", text: " + text;
      }
    };
  }
  
  static JsonStreamElement startDocument() {
    return START_DOCUMENT;
  }
  
  static JsonStreamElement endDocument() {
    return END_DOCUMENT;
  }
  
  static JsonStreamElement startArray() {
    return START_ARRAY;
  }
  
  static JsonStreamElement endArray() {
    return END_ARRAY;
  }
  
  static JsonStreamElement startObject() {
    return START_OBJECT;
  }
  
  static JsonStreamElement endObject() {
    return END_OBJECT;
  }
  
  static JsonStreamElement startField(String text) {
    return textJsonStreamElement(JsonStreamElementType.START_FIELD, text);
  }
  
  static JsonStreamElement endField() {
    return END_FIELD;
  }
  
  static JsonStreamElement string(String text) {
    return textJsonStreamElement(JsonStreamElementType.STRING, text);
  }
  
  static JsonStreamElement number(String text) {
    return textJsonStreamElement(JsonStreamElementType.NUMBER, text);
  }
  
  static JsonStreamElement trueValue() {
    return TRUE;
  }
  
  static JsonStreamElement falseValue() {
    return FALSE;
  }
  
  static JsonStreamElement nullValue() {
    return NULL;
  }
  

  private JsonStreamElement(JsonStreamElementType jsonStreamElementType)
  {
    this.jsonStreamElementType = jsonStreamElementType;
  }
  




  public final JsonStreamElementType jsonStreamElementType()
  {
    return jsonStreamElementType;
  }
  
  public abstract String text();
}
