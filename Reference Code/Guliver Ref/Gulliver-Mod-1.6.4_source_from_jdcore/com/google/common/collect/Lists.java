package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;




























































public final class Lists
{
  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }
  






































  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements)
  {
    Preconditions.checkNotNull(elements);
    
    return (elements instanceof Collection) ? new ArrayList(Collections2.cast(elements)) : newArrayList(elements.iterator());
  }
  












  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements)
  {
    Preconditions.checkNotNull(elements);
    ArrayList<E> list = newArrayList();
    while (elements.hasNext()) {
      list.add(elements.next());
    }
    return list;
  }
  






































































































































































































































































































































































  public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function)
  {
    return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList(fromList, function) : new TransformingSequentialList(fromList, function);
  }
  


  private static class TransformingSequentialList<F, T>
    extends AbstractSequentialList<T>
    implements Serializable
  {
    final List<F> fromList;
    

    final Function<? super F, ? extends T> function;
    

    TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function)
    {
      this.fromList = ((List)Preconditions.checkNotNull(fromList));
      this.function = ((Function)Preconditions.checkNotNull(function));
    }
    



    public void clear()
    {
      fromList.clear();
    }
    
    public int size() { return fromList.size(); }
    
    public ListIterator<T> listIterator(int index) {
      new TransformedListIterator(fromList.listIterator(index))
      {
        T transform(F from) {
          return function.apply(from);
        }
      };
    }
  }
  



  private static class TransformingRandomAccessList<F, T>
    extends AbstractList<T>
    implements Serializable, RandomAccess
  {
    final List<F> fromList;
    


    final Function<? super F, ? extends T> function;
    


    TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function)
    {
      this.fromList = ((List)Preconditions.checkNotNull(fromList));
      this.function = ((Function)Preconditions.checkNotNull(function));
    }
    
    public void clear() { fromList.clear(); }
    
    public T get(int index) {
      return function.apply(fromList.get(index));
    }
    
    public boolean isEmpty() { return fromList.isEmpty(); }
    
    public T remove(int index) {
      return function.apply(fromList.remove(index));
    }
    
    public int size() { return fromList.size(); }
  }
  


































































































































































































































































































































































































































  static int hashCodeImpl(List<?> list)
  {
    int hashCode = 1;
    for (Object o : list) {
      hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode());
      
      hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
    }
    
    return hashCode;
  }
  


  static boolean equalsImpl(List<?> list, @Nullable Object object)
  {
    if (object == Preconditions.checkNotNull(list)) {
      return true;
    }
    if (!(object instanceof List)) {
      return false;
    }
    
    List<?> o = (List)object;
    
    return (list.size() == o.size()) && (Iterators.elementsEqual(list.iterator(), o.iterator()));
  }
  

















  static int indexOfImpl(List<?> list, @Nullable Object element)
  {
    ListIterator<?> listIterator = list.listIterator();
    while (listIterator.hasNext()) {
      if (Objects.equal(element, listIterator.next())) {
        return listIterator.previousIndex();
      }
    }
    return -1;
  }
  


  static int lastIndexOfImpl(List<?> list, @Nullable Object element)
  {
    ListIterator<?> listIterator = list.listIterator(list.size());
    while (listIterator.hasPrevious()) {
      if (Objects.equal(element, listIterator.previous())) {
        return listIterator.nextIndex();
      }
    }
    return -1;
  }
}
