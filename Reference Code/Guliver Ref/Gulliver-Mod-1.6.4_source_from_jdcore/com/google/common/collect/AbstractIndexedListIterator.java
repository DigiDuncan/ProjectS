package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;






















































abstract class AbstractIndexedListIterator<E>
  extends UnmodifiableListIterator<E>
{
  private final int size;
  private int position;
  
  protected abstract E get(int paramInt);
  
  protected AbstractIndexedListIterator(int size, int position)
  {
    Preconditions.checkPositionIndex(position, size);
    this.size = size;
    this.position = position;
  }
  
  public final boolean hasNext()
  {
    return position < size;
  }
  
  public final E next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return get(position++);
  }
  
  public final int nextIndex()
  {
    return position;
  }
  
  public final boolean hasPrevious()
  {
    return position > 0;
  }
  
  public final E previous()
  {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    return get(--position);
  }
  
  public final int previousIndex()
  {
    return position - 1;
  }
}
