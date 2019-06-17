package com.google.common.collect;







































final class Hashing
{
  static int smear(int hashCode)
  {
    return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
  }
  
  static int MAX_TABLE_SIZE = 1073741824;
}
