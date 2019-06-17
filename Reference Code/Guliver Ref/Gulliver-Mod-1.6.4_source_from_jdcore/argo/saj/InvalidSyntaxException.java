package argo.saj;





public final class InvalidSyntaxException
  extends Exception
{
  private final int column;
  



  private final int row;
  



  public InvalidSyntaxException(String s, int row, int column)
  {
    super("At line " + row + ", column " + column + ":  " + s);
    this.column = column;
    this.row = row;
  }
  
  public InvalidSyntaxException(String s, Throwable throwable, int row, int column) {
    super("At line " + row + ", column " + column + ":  " + s, throwable);
    this.column = column;
    this.row = row;
  }
}
