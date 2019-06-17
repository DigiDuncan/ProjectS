package argo.staj;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;












final class PositionTrackingPushbackReader
  implements ThingWithPosition
{
  private final PushbackReader pushbackReader;
  private int characterCount = 0;
  private int lineCount = 1;
  private boolean lastCharacterWasCarriageReturn = false;
  
  PositionTrackingPushbackReader(Reader in) {
    pushbackReader = new PushbackReader(in);
  }
  
  void unread(char c) throws JsonStreamException {
    characterCount -= 1;
    if (characterCount < 0) characterCount = 0;
    try {
      pushbackReader.unread(c);
    } catch (IOException e) {
      throw new JsonStreamException("Failed to read from Reader", e);
    }
  }
  
  void uncount(char[] resultCharArray) {
    characterCount -= resultCharArray.length;
    if (characterCount < 0) characterCount = 0;
  }
  
  int read() throws JsonStreamException {
    try {
      int result = pushbackReader.read();
      updateCharacterAndLineCounts(result);
      return result;
    } catch (IOException e) {
      throw new JsonStreamException("Failed to read from Reader", e);
    }
  }
  
  int read(char[] buffer) throws JsonStreamException {
    try {
      int result = pushbackReader.read(buffer);
      for (char character : buffer) {
        updateCharacterAndLineCounts(character);
      }
      return result;
    } catch (IOException e) {
      throw new JsonStreamException("Failed to read from Reader", e);
    }
  }
  
  private void updateCharacterAndLineCounts(int result) {
    if (13 == result) {
      characterCount = 0;
      lineCount += 1;
      lastCharacterWasCarriageReturn = true;
    } else {
      if ((10 == result) && (!lastCharacterWasCarriageReturn)) {
        characterCount = 0;
        lineCount += 1;
      } else {
        characterCount += 1;
      }
      lastCharacterWasCarriageReturn = false;
    }
  }
  
  public int getColumn() {
    return characterCount;
  }
  
  public int getRow() {
    return lineCount;
  }
  
  public ThingWithPosition snapshotOfPosition() {
    new ThingWithPosition() {
      private final int localCharacterCount = characterCount;
      private final int localLineCount = lineCount;
      
      public int getColumn() {
        return localCharacterCount;
      }
      
      public int getRow() {
        return localLineCount;
      }
    };
  }
}
