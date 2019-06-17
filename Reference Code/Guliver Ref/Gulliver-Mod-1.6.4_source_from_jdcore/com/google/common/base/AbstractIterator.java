package com.google.common.base;

import java.util.Iterator;
import java.util.NoSuchElementException;
























abstract class AbstractIterator<T>
  implements Iterator<T>
{
  private State state = State.NOT_READY;
  private T next;
  protected AbstractIterator() {}
  
  private static enum State {
    READY,  NOT_READY,  DONE,  FAILED;
    
    private State() {}
  }
  
  protected abstract T computeNext();
  
  protected final T endOfData() {
    state = State.DONE;
    return null;
  }
  
  public final boolean hasNext()
  {
    Preconditions.checkState(state != State.FAILED);
    switch (1.$SwitchMap$com$google$common$base$AbstractIterator$State[state.ordinal()]) {
    case 1: 
      return false;
    case 2: 
      return true;
    }
    
    return tryToComputeNext();
  }
  
  private boolean tryToComputeNext() {
    state = State.FAILED;
    next = computeNext();
    if (state != State.DONE) {
      state = State.READY;
      return true;
    }
    return false;
  }
  
  public final T next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    state = State.NOT_READY;
    return next;
  }
  
  public final void remove() {
    throw new UnsupportedOperationException();
  }
}
