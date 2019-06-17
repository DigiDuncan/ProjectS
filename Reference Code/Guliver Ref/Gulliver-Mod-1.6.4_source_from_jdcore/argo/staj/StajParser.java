package argo.staj;

import java.io.Reader;
import java.util.Iterator;
import java.util.Stack;














public final class StajParser
  implements Iterator<JsonStreamElement>
{
  private final PositionTrackingPushbackReader pushbackReader;
  private final Stack<JsonStreamElementType> stack = new Stack();
  

  private JsonStreamElement current;
  
  private JsonStreamElement next;
  

  public StajParser(Reader in)
  {
    pushbackReader = new PositionTrackingPushbackReader(in);
  }
  















  public boolean hasNext()
  {
    if ((current != null) && (current.jsonStreamElementType().equals(JsonStreamElementType.END_DOCUMENT)))
      return false;
    if (next == null) {
      next = getNextElement();
    }
    return true;
  }
  








  public JsonStreamElement next()
  {
    if (next != null) {
      current = next;
      next = null;
    } else {
      current = getNextElement();
    }
    return current;
  }
  
  private JsonStreamElement getNextElement() {
    if (current != null) {
      return current.jsonStreamElementType().parseNext(pushbackReader, stack);
    }
    return JsonStreamElementType.parseFirstElement(pushbackReader);
  }
  



  public void remove()
  {
    throw new UnsupportedOperationException("StajParser cannot remove elements from JSON it has parsed.");
  }
}
