package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;















































































public final class Closer
  implements Closeable
{
  private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
  

  final Suppressor suppressor;
  

  public static Closer create()
  {
    return new Closer(SUPPRESSOR);
  }
  



  private final Deque<Closeable> stack = new ArrayDeque(4);
  private Throwable thrown;
  
  Closer(Suppressor suppressor) {
    this.suppressor = ((Suppressor)Preconditions.checkNotNull(suppressor));
  }
  






  public <C extends Closeable> C register(C closeable)
  {
    stack.push(closeable);
    return closeable;
  }
  











  public RuntimeException rethrow(Throwable e)
    throws IOException
  {
    thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    throw Throwables.propagate(e);
  }
  



















































  public void close()
    throws IOException
  {
    Throwable throwable = thrown;
    

    while (!stack.isEmpty()) {
      Closeable closeable = (Closeable)stack.pop();
      try {
        closeable.close();
      } catch (Throwable e) {
        if (throwable == null) {
          throwable = e;
        } else {
          suppressor.suppress(closeable, throwable, e);
        }
      }
    }
    
    if ((thrown == null) && (throwable != null)) {
      Throwables.propagateIfPossible(throwable, IOException.class);
      throw new AssertionError(throwable);
    }
  }
  





  static abstract interface Suppressor
  {
    public abstract void suppress(Closeable paramCloseable, Throwable paramThrowable1, Throwable paramThrowable2);
  }
  




  static final class LoggingSuppressor
    implements Closer.Suppressor
  {
    static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
    
    LoggingSuppressor() {}
    
    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
      Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
    }
  }
  

  static final class SuppressingSuppressor
    implements Closer.Suppressor
  {
    SuppressingSuppressor() {}
    

    static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
    
    static boolean isAvailable() {
      return addSuppressed != null; }
    

    static final Method addSuppressed = getAddSuppressed();
    
    private static Method getAddSuppressed() {
      try {
        return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
      } catch (Throwable e) {}
      return null;
    }
    


    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed)
    {
      if (thrown == suppressed) {
        return;
      }
      try {
        addSuppressed.invoke(thrown, new Object[] { suppressed });
      }
      catch (Throwable e) {
        Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
      }
    }
  }
}
