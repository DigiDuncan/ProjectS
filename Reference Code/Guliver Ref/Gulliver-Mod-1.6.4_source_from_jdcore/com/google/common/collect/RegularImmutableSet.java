package com.google.common.collect;








final class RegularImmutableSet<E>
  extends ImmutableSet.ArrayImmutableSet<E>
{
  final transient Object[] table;
  






  private final transient int mask;
  






  private final transient int hashCode;
  






  RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask)
  {
    super(elements);
    this.table = table;
    this.mask = mask;
    this.hashCode = hashCode;
  }
  
  public boolean contains(Object target) {
    if (target == null) {
      return false;
    }
    for (int i = Hashing.smear(target.hashCode());; i++) {
      Object candidate = table[(i & mask)];
      if (candidate == null) {
        return false;
      }
      if (candidate.equals(target)) {
        return true;
      }
    }
  }
  
  public int hashCode() {
    return hashCode;
  }
  
  boolean isHashCodeFast() {
    return true;
  }
}
