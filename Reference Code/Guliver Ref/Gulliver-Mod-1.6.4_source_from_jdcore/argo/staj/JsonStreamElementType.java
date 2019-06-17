package argo.staj;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Stack;
















public enum JsonStreamElementType
{
  START_ARRAY, 
  










  END_ARRAY, 
  




  START_OBJECT, 
  




  END_OBJECT, 
  




  START_FIELD, 
  








  END_FIELD, 
  




  STRING, 
  




  TRUE, 
  




  FALSE, 
  




  NULL, 
  




  NUMBER, 
  




  START_DOCUMENT, 
  














  END_DOCUMENT;
  

  private JsonStreamElementType() {}
  
  abstract JsonStreamElement parseNext(PositionTrackingPushbackReader paramPositionTrackingPushbackReader, Stack<JsonStreamElementType> paramStack);
  
  static JsonStreamElement parseFirstElement(PositionTrackingPushbackReader pushbackReader)
    throws InvalidSyntaxRuntimeException
  {
    char nextChar = (char)pushbackReader.read();
    if ((nextChar == '{') || (nextChar == '[')) {
      pushbackReader.unread(nextChar);
      return JsonStreamElement.startDocument();
    }
    throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected either [ or { but got [" + nextChar + "].", pushbackReader);
  }
  








  private static JsonStreamElement parseFieldOrObjectEnd(PositionTrackingPushbackReader pushbackReader, Stack<JsonStreamElementType> stack)
  {
    char nextChar = (char)readNextNonWhitespaceChar(pushbackReader);
    if (nextChar != '}') {
      pushbackReader.unread(nextChar);
      return aFieldToken(pushbackReader, stack);
    }
    stack.pop();
    return JsonStreamElement.endObject();
  }
  
  private static JsonStreamElement parseFromTheEndOfARootNode(PositionTrackingPushbackReader pushbackReader, Stack<JsonStreamElementType> stack) {
    int nextChar = readNextNonWhitespaceChar(pushbackReader);
    if (stack.isEmpty()) {
      if (nextChar != -1) {
        throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Got unexpected trailing character [" + (char)nextChar + "].", pushbackReader);
      }
      return JsonStreamElement.endDocument();
    }
    pushbackReader.unread((char)nextChar);
    return parseFromEndOfNode(pushbackReader, stack);
  }
  
  private static JsonStreamElement parseFromEndOfNode(PositionTrackingPushbackReader pushbackReader, Stack<JsonStreamElementType> stack)
  {
    int nextChar = readNextNonWhitespaceChar(pushbackReader);
    JsonStreamElementType peek = (JsonStreamElementType)stack.peek();
    if (peek.equals(START_OBJECT)) {
      switch (nextChar) {
      case 44: 
        return aJsonValue(pushbackReader, stack);
      case 125: 
        stack.pop();
        return JsonStreamElement.endObject();
      }
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected either , or } but got [" + nextChar + "].", pushbackReader);
    }
    if (peek.equals(START_ARRAY)) {
      switch (nextChar) {
      case 44: 
        return aJsonValue(pushbackReader, stack);
      case 93: 
        stack.pop();
        return JsonStreamElement.endArray();
      }
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected either , or ] but got [" + nextChar + "].", pushbackReader);
    }
    
    switch (nextChar) {
    case 44: 
      stack.pop();
      return JsonStreamElement.endField();
    case 125: 
      stack.pop();
      pushbackReader.unread((char)nextChar);
      return JsonStreamElement.endField();
    }
    throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected either , or ] but got [" + nextChar + "].", pushbackReader);
  }
  


  private static int readNextNonWhitespaceChar(PositionTrackingPushbackReader in)
  {
    boolean gotNonWhitespace = false;
    int nextChar;
    do { nextChar = in.read();
      switch (nextChar) {
      case 9: 
      case 10: 
      case 13: 
      case 32: 
        break;
      default: 
        gotNonWhitespace = true;
      }
    } while (!gotNonWhitespace);
    return nextChar;
  }
  
  private static JsonStreamElement aJsonValue(PositionTrackingPushbackReader pushbackReader, Stack<JsonStreamElementType> stack) {
    char nextChar = (char)readNextNonWhitespaceChar(pushbackReader);
    switch (nextChar) {
    case '"': 
      pushbackReader.unread(nextChar);
      return JsonStreamElement.string(stringToken(pushbackReader));
    case 't': 
      char[] remainingTrueTokenCharacters = new char[3];
      int trueTokenCharactersRead = pushbackReader.read(remainingTrueTokenCharacters);
      if ((trueTokenCharactersRead != 3) || (remainingTrueTokenCharacters[0] != 'r') || (remainingTrueTokenCharacters[1] != 'u') || (remainingTrueTokenCharacters[2] != 'e')) {
        pushbackReader.uncount(remainingTrueTokenCharacters);
        throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected 't' to be followed by [[r, u, e]], but got [" + Arrays.toString(remainingTrueTokenCharacters) + "].", pushbackReader);
      }
      return JsonStreamElement.trueValue();
    
    case 'f': 
      char[] remainingFalseTokenCharacters = new char[4];
      int falseTokenCharactersRead = pushbackReader.read(remainingFalseTokenCharacters);
      if ((falseTokenCharactersRead != 4) || (remainingFalseTokenCharacters[0] != 'a') || (remainingFalseTokenCharacters[1] != 'l') || (remainingFalseTokenCharacters[2] != 's') || (remainingFalseTokenCharacters[3] != 'e')) {
        pushbackReader.uncount(remainingFalseTokenCharacters);
        throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected 'f' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(remainingFalseTokenCharacters) + "].", pushbackReader);
      }
      return JsonStreamElement.falseValue();
    
    case 'n': 
      char[] remainingNullTokenCharacters = new char[3];
      int nullTokenCharactersRead = pushbackReader.read(remainingNullTokenCharacters);
      if ((nullTokenCharactersRead != 3) || (remainingNullTokenCharacters[0] != 'u') || (remainingNullTokenCharacters[1] != 'l') || (remainingNullTokenCharacters[2] != 'l')) {
        pushbackReader.uncount(remainingNullTokenCharacters);
        throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected 'n' to be followed by [[u, l, l]], but got [" + Arrays.toString(remainingNullTokenCharacters) + "].", pushbackReader);
      }
      return JsonStreamElement.nullValue();
    
    case '-': 
    case '0': 
    case '1': 
    case '2': 
    case '3': 
    case '4': 
    case '5': 
    case '6': 
    case '7': 
    case '8': 
    case '9': 
      pushbackReader.unread(nextChar);
      return JsonStreamElement.number(numberToken(pushbackReader));
    case '{': 
      stack.push(START_OBJECT);
      return JsonStreamElement.startObject();
    case '[': 
      stack.push(START_ARRAY);
      return JsonStreamElement.startArray();
    }
    throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Invalid character at start of value [" + nextChar + "].", pushbackReader);
  }
  
  private static JsonStreamElement aFieldToken(PositionTrackingPushbackReader pushbackReader, Stack<JsonStreamElementType> stack)
  {
    char nextChar = (char)readNextNonWhitespaceChar(pushbackReader);
    if ('"' != nextChar) {
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected object identifier to begin with [\"] but got [" + nextChar + "].", pushbackReader);
    }
    pushbackReader.unread(nextChar);
    stack.push(START_FIELD);
    return JsonStreamElement.startField(stringToken(pushbackReader));
  }
  
  private static String stringToken(PositionTrackingPushbackReader in) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)in.read();
    if ('"' != firstChar) {
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected [\"] but got [" + firstChar + "].", in);
    }
    ThingWithPosition openDoubleQuotesPosition = in.snapshotOfPosition();
    boolean stringClosed = false;
    while (!stringClosed) {
      char nextChar = (char)in.read();
      switch (nextChar) {
      case '￿': 
        throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Got opening [\"] without matching closing [\"]", openDoubleQuotesPosition);
      case '"': 
        stringClosed = true;
        break;
      case '\\': 
        char escapedChar = escapedStringChar(in);
        result.append(escapedChar);
        break;
      default: 
        result.append(nextChar);
      }
    }
    return result.toString();
  }
  
  private static char escapedStringChar(PositionTrackingPushbackReader in)
  {
    char firstChar = (char)in.read();
    char result; switch (firstChar) {
    case '"': 
      result = '"';
      break;
    case '\\': 
      result = '\\';
      break;
    case '/': 
      result = '/';
      break;
    case 'b': 
      result = '\b';
      break;
    case 'f': 
      result = '\f';
      break;
    case 'n': 
      result = '\n';
      break;
    case 'r': 
      result = '\r';
      break;
    case 't': 
      result = '\t';
      break;
    case 'u': 
      result = (char)hexadecimalNumber(in);
      break;
    default: 
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Unrecognised escape character [" + firstChar + "].", in);
    }
    return result;
  }
  
  private static int hexadecimalNumber(PositionTrackingPushbackReader in) {
    char[] resultCharArray = new char[4];
    int readSize = in.read(resultCharArray);
    if (readSize != 4) {
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected a 4 digit hexadecimal number but got only [" + readSize + "], namely [" + String.valueOf(resultCharArray, 0, readSize) + "].", in);
    }
    int result;
    try {
      result = Integer.parseInt(String.valueOf(resultCharArray), 16);
    } catch (NumberFormatException e) {
      in.uncount(resultCharArray);
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Unable to parse [" + String.valueOf(resultCharArray) + "] as a hexadecimal number.", e, in);
    }
    return result;
  }
  
  private static String numberToken(PositionTrackingPushbackReader in) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)in.read();
    if ('-' == firstChar) {
      result.append('-');
    } else {
      in.unread(firstChar);
    }
    result.append(nonNegativeNumberToken(in));
    return result.toString();
  }
  
  private static String nonNegativeNumberToken(PositionTrackingPushbackReader in) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)in.read();
    if ('0' == firstChar) {
      result.append('0');
      result.append(possibleFractionalComponent(in));
      result.append(possibleExponent(in));
    } else {
      in.unread(firstChar);
      result.append(nonZeroDigitToken(in));
      result.append(digitString(in));
      result.append(possibleFractionalComponent(in));
      result.append(possibleExponent(in));
    }
    return result.toString();
  }
  
  private static char nonZeroDigitToken(PositionTrackingPushbackReader in)
  {
    char nextChar = (char)in.read();
    char result; switch (nextChar) {
    case '1': 
    case '2': 
    case '3': 
    case '4': 
    case '5': 
    case '6': 
    case '7': 
    case '8': 
    case '9': 
      result = nextChar;
      break;
    default: 
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected a digit 1 - 9 but got [" + nextChar + "].", in);
    }
    return result;
  }
  
  private static char digitToken(PositionTrackingPushbackReader in)
  {
    char nextChar = (char)in.read();
    char result; switch (nextChar) {
    case '0': 
    case '1': 
    case '2': 
    case '3': 
    case '4': 
    case '5': 
    case '6': 
    case '7': 
    case '8': 
    case '9': 
      result = nextChar;
      break;
    default: 
      throw InvalidSyntaxRuntimeException.invalidSyntaxRuntimeException("Expected a digit 1 - 9 but got [" + nextChar + "].", in);
    }
    return result;
  }
  
  private static String digitString(PositionTrackingPushbackReader in) {
    StringBuilder result = new StringBuilder();
    boolean gotANonDigit = false;
    while (!gotANonDigit) {
      char nextChar = (char)in.read();
      switch (nextChar) {
      case '0': 
      case '1': 
      case '2': 
      case '3': 
      case '4': 
      case '5': 
      case '6': 
      case '7': 
      case '8': 
      case '9': 
        result.append(nextChar);
        break;
      default: 
        gotANonDigit = true;
        in.unread(nextChar);
      }
    }
    return result.toString();
  }
  
  private static String possibleFractionalComponent(PositionTrackingPushbackReader pushbackReader) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)pushbackReader.read();
    if (firstChar == '.') {
      result.append('.');
      result.append(digitToken(pushbackReader));
      result.append(digitString(pushbackReader));
    } else {
      pushbackReader.unread(firstChar);
    }
    return result.toString();
  }
  
  private static String possibleExponent(PositionTrackingPushbackReader pushbackReader) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)pushbackReader.read();
    switch (firstChar) {
    case '.': 
    case 'E': 
      result.append('E');
      result.append(possibleSign(pushbackReader));
      result.append(digitToken(pushbackReader));
      result.append(digitString(pushbackReader));
      break;
    case 'e': 
      result.append('e');
      result.append(possibleSign(pushbackReader));
      result.append(digitToken(pushbackReader));
      result.append(digitString(pushbackReader));
      break;
    default: 
      pushbackReader.unread(firstChar);
    }
    
    return result.toString();
  }
  
  private static String possibleSign(PositionTrackingPushbackReader pushbackReader) {
    StringBuilder result = new StringBuilder();
    char firstChar = (char)pushbackReader.read();
    if ((firstChar == '+') || (firstChar == '-')) {
      result.append(firstChar);
    } else {
      pushbackReader.unread(firstChar);
    }
    return result.toString();
  }
}
