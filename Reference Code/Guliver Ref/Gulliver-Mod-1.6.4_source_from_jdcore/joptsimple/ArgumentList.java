package joptsimple;











class ArgumentList
{
  private final String[] arguments;
  









  private int currentIndex;
  










  ArgumentList(String... arguments)
  {
    this.arguments = ((String[])arguments.clone());
  }
  
  boolean hasMore() {
    return currentIndex < arguments.length;
  }
  
  String next() {
    return arguments[(currentIndex++)];
  }
}
