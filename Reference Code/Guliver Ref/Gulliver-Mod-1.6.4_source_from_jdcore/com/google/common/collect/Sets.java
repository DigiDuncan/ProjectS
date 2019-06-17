package com.google.common.collect;

import java.util.Set;
import javax.annotation.Nullable;













































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































public final class Sets
{
  static int hashCodeImpl(Set<?> s)
  {
    int hashCode = 0;
    for (Object o : s) {
      hashCode += (o != null ? o.hashCode() : 0);
      
      hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
    }
    
    return hashCode;
  }
  


  static boolean equalsImpl(Set<?> s, @Nullable Object object)
  {
    if (s == object) {
      return true;
    }
    if ((object instanceof Set)) {
      Set<?> o = (Set)object;
      try
      {
        return (s.size() == o.size()) && (s.containsAll(o));
      } catch (NullPointerException ignored) {
        return false;
      } catch (ClassCastException ignored) {
        return false;
      }
    }
    return false;
  }
}
