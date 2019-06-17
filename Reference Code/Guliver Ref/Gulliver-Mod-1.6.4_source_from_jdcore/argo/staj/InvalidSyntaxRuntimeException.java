package argo.staj;

import argo.saj.InvalidSyntaxException;












public abstract class InvalidSyntaxRuntimeException
  extends RuntimeException
{
  private final int column;
  private final int row;
  
  private InvalidSyntaxRuntimeException(String s, ThingWithPosition thingWithPosition)
  {
    super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s);
    column = thingWithPosition.getColumn();
    row = thingWithPosition.getRow();
  }
  
  private InvalidSyntaxRuntimeException(String s, Throwable throwable, ThingWithPosition thingWithPosition) {
    super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s, throwable);
    column = thingWithPosition.getColumn();
    row = thingWithPosition.getRow();
  }
  
  static InvalidSyntaxRuntimeException invalidSyntaxRuntimeException(final String s, final ThingWithPosition thingWithPosition) {
    new InvalidSyntaxRuntimeException(s, thingWithPosition, s)
    {
      public InvalidSyntaxException asInvalidSyntaxException() {
        return new InvalidSyntaxException(s, thingWithPosition.getRow(), thingWithPosition.getColumn());
      }
    };
  }
  
  static InvalidSyntaxRuntimeException invalidSyntaxRuntimeException(final String s, final Throwable throwable, final ThingWithPosition thingWithPosition) {
    new InvalidSyntaxRuntimeException(s, throwable, thingWithPosition, s)
    {
      public InvalidSyntaxException asInvalidSyntaxException() {
        return new InvalidSyntaxException(s, throwable, thingWithPosition.getRow(), thingWithPosition.getColumn());
      }
    };
  }
  
  public abstract InvalidSyntaxException asInvalidSyntaxException();
}
