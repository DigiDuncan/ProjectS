package argo.jdom;

import argo.saj.InvalidSyntaxException;
import argo.saj.JsonListener;
import argo.saj.SajParser;
import java.io.IOException;
import java.io.Reader;










































public final class JdomParser
{
  public JdomParser() {}
  
  public JsonRootNode parse(final Reader reader)
    throws IOException, InvalidSyntaxException
  {
    parse(new JsonListenerBasedParser() {
      public void parse(JsonListener jsonListener) throws IOException, InvalidSyntaxException {
        new SajParser().parse(reader, jsonListener);
      }
    });
  }
  
  JsonRootNode parse(JsonListenerBasedParser jsonListenerBasedParser) throws IOException, InvalidSyntaxException {
    JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
    jsonListenerBasedParser.parse(jsonListenerToJdomAdapter);
    return jsonListenerToJdomAdapter.getDocument();
  }
  
  static abstract interface JsonListenerBasedParser
  {
    public abstract void parse(JsonListener paramJsonListener)
      throws IOException, InvalidSyntaxException;
  }
}
