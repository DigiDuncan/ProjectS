package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;






































































public abstract class ImmutableSet<E>
  extends ImmutableCollection<E>
  implements Set<E>
{
  public static <E> ImmutableSet<E> of()
  {
    return EmptyImmutableSet.INSTANCE;
  }
  





  public static <E> ImmutableSet<E> of(E element)
  {
    return new SingletonImmutableSet(element);
  }
  
















































































  private static <E> ImmutableSet<E> construct(int n, Object... elements)
  {
    switch (n) {
    case 0: 
      return of();
    
    case 1: 
      E elem = elements[0];
      return of(elem);
    }
    
    
    int tableSize = chooseTableSize(n);
    Object[] table = new Object[tableSize];
    int mask = tableSize - 1;
    int hashCode = 0;
    int uniques = 0;
    for (int i = 0; i < n; i++) {
      Object element = ObjectArrays.checkElementNotNull(elements[i], i);
      int hash = element.hashCode();
      for (int j = Hashing.smear(hash);; j++) {
        int index = j & mask;
        Object value = table[index];
        if (value == null)
        {
          elements[(uniques++)] = element;
          table[index] = element;
          hashCode += hash;
        } else {
          if (value.equals(element))
            break;
        }
      }
    }
    Arrays.fill(elements, uniques, n, null);
    if (uniques == 1)
    {

      E element = elements[0];
      return new SingletonImmutableSet(element, hashCode); }
    if (tableSize != chooseTableSize(uniques))
    {

      return construct(uniques, elements);
    }
    Object[] uniqueElements = uniques < elements.length ? ObjectArrays.arraysCopyOf(elements, uniques) : elements;
    

    return new RegularImmutableSet(uniqueElements, hashCode, table, mask);
  }
  








  private static final int CUTOFF = (int)Math.floor(7.516192768E8D);
  









  static int chooseTableSize(int setSize)
  {
    if (setSize < CUTOFF)
    {
      int tableSize = Integer.highestOneBit(setSize - 1) << 1;
      while (tableSize * 0.7D < setSize) {
        tableSize <<= 1;
      }
      return tableSize;
    }
    

    Preconditions.checkArgument(setSize < 1073741824, "collection too large");
    return 1073741824;
  }
  









  public static <E> ImmutableSet<E> copyOf(E[] elements)
  {
    switch (elements.length) {
    case 0: 
      return of();
    case 1: 
      return of(elements[0]);
    }
    return construct(elements.length, (Object[])elements.clone());
  }
  

























































  ImmutableSet() {}
  
























































  boolean isHashCodeFast()
  {
    return false;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this) {
      return true;
    }
    if (((object instanceof ImmutableSet)) && (isHashCodeFast()) && (((ImmutableSet)object).isHashCodeFast()) && (hashCode() != object.hashCode()))
    {


      return false;
    }
    return Sets.equalsImpl(this, object);
  }
  
  public int hashCode() {
    return Sets.hashCodeImpl(this);
  }
  
  public abstract UnmodifiableIterator<E> iterator();
  
  static abstract class ArrayImmutableSet<E>
    extends ImmutableSet<E>
  {
    final transient Object[] elements;
    
    ArrayImmutableSet(Object[] elements)
    {
      this.elements = elements;
    }
    
    public int size()
    {
      return elements.length;
    }
    
    public boolean isEmpty() {
      return false;
    }
    
    public UnmodifiableIterator<E> iterator() {
      return asList().iterator();
    }
    
    public Object[] toArray() {
      return asList().toArray();
    }
    
    public <T> T[] toArray(T[] array) {
      return asList().toArray(array);
    }
    
    public boolean containsAll(Collection<?> targets) {
      if (targets == this) {
        return true;
      }
      if (!(targets instanceof ArrayImmutableSet)) {
        return super.containsAll(targets);
      }
      if (targets.size() > size()) {
        return false;
      }
      for (Object target : elements) {
        if (!contains(target)) {
          return false;
        }
      }
      return true;
    }
    



    ImmutableList<E> createAsList()
    {
      return new RegularImmutableAsList(this, elements);
    }
  }
}
