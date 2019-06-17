package com.google.common.collect;



































public abstract class FluentIterable<E>
  implements Iterable<E>
{
  private final Iterable<E> iterable;
  

































  protected FluentIterable()
  {
    iterable = this;
  }
  



































  public String toString()
  {
    return Iterables.toString(iterable);
  }
}
