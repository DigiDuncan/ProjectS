package joptsimple.internal;










class Row
{
  final String option;
  








  final String description;
  








  Row(String option, String description)
  {
    this.option = option;
    this.description = description;
  }
  
  public boolean equals(Object that)
  {
    if (that == this)
      return true;
    if ((that == null) || (!getClass().equals(that.getClass()))) {
      return false;
    }
    Row other = (Row)that;
    return (option.equals(option)) && (description.equals(description));
  }
  
  public int hashCode()
  {
    return option.hashCode() ^ description.hashCode();
  }
}
