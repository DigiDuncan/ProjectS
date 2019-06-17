package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
























abstract class TransformedIterator<F, T>
  implements Iterator<T>
{
  final Iterator<? extends F> backingIterator;
  
  TransformedIterator(Iterator<? extends F> backingIterator)
  {
    this.backingIterator = ((Iterator)Preconditions.checkNotNull(backingIterator));
  }
  
  abstract T transform(F paramF);
  
  public final boolean hasNext()
  {
    return backingIterator.hasNext();
  }
  
  public final T next()
  {
    return transform(backingIterator.next());
  }
  
  public final void remove()
  {
    backingIterator.remove();
  }
}
