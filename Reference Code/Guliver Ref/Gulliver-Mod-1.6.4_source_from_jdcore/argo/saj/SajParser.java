package argo.saj;

import argo.staj.InvalidSyntaxRuntimeException;
import argo.staj.JsonStreamElement;
import argo.staj.StajParser;
import java.io.IOException;
import java.io.Reader;








































public final class SajParser
{
  public SajParser() {}
  
  public void parse(Reader in, JsonListener jsonListener)
    throws IOException, InvalidSyntaxException
  {
    parse(jsonListener, new StajParser(in));
  }
  
  void parse(JsonListener jsonListener, StajParser stajParser) throws InvalidSyntaxException {
    try {
      while (stajParser.hasNext()) {
        JsonStreamElement jsonStreamElement = stajParser.next();
        switch (1.$SwitchMap$argo$staj$JsonStreamElementType[jsonStreamElement.jsonStreamElementType().ordinal()]) {
        case 1: 
          jsonListener.startDocument();
          break;
        case 2: 
          jsonListener.endDocument();
          break;
        case 3: 
          jsonListener.startArray();
          break;
        case 4: 
          jsonListener.endArray();
          break;
        case 5: 
          jsonListener.startObject();
          break;
        case 6: 
          jsonListener.endObject();
          break;
        case 7: 
          jsonListener.startField(jsonStreamElement.text());
          break;
        case 8: 
          jsonListener.endField();
          break;
        case 9: 
          jsonListener.nullValue();
          break;
        case 10: 
          jsonListener.trueValue();
          break;
        case 11: 
          jsonListener.falseValue();
          break;
        case 12: 
          jsonListener.stringValue(jsonStreamElement.text());
          break;
        case 13: 
          jsonListener.numberValue(jsonStreamElement.text());
          break;
        default: 
          throw new IllegalStateException("Got a JsonStreamElement of unexpected type: " + jsonStreamElement);
        }
      }
    } catch (InvalidSyntaxRuntimeException e) {
      throw e.asInvalidSyntaxException();
    }
  }
}
