package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Collection;






































































































































































































































































































































public final class Collections2
{
  static boolean containsAllImpl(Collection<?> self, Collection<?> c)
  {
    Preconditions.checkNotNull(self);
    for (Object o : c) {
      if (!self.contains(o)) {
        return false;
      }
    }
    return true;
  }
  


  static String toStringImpl(Collection<?> collection)
  {
    StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
    
    STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function()
    {
      public Object apply(Object input) {
        return input == val$collection ? "(this Collection)" : input;
      }
    }));
    return ']';
  }
  


  static StringBuilder newStringBuilderForCollection(int size)
  {
    Preconditions.checkArgument(size >= 0, "size must be non-negative");
    return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
  }
  


  static <T> Collection<T> cast(Iterable<T> iterable)
  {
    return (Collection)iterable;
  }
  
  static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");
}
