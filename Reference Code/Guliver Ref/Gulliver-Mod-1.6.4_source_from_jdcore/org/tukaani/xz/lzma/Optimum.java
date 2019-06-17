package org.tukaani.xz.lzma;

final class Optimum
{
  final State state = new State();
  final int[] reps = new int[4];
  int price;
  int optPrev;
  int backPrev;
  boolean prev1IsLiteral;
  boolean hasPrev2;
  int optPrev2;
  int backPrev2;
  
  Optimum() {}
  
  void reset()
  {
    price = 1073741824;
  }
  
  void set1(int paramInt1, int paramInt2, int paramInt3)
  {
    price = paramInt1;
    optPrev = paramInt2;
    backPrev = paramInt3;
    prev1IsLiteral = false;
  }
  
  void set2(int paramInt1, int paramInt2, int paramInt3)
  {
    price = paramInt1;
    optPrev = (paramInt2 + 1);
    backPrev = paramInt3;
    prev1IsLiteral = true;
    hasPrev2 = false;
  }
  
  void set3(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    price = paramInt1;
    optPrev = (paramInt2 + paramInt4 + 1);
    backPrev = paramInt5;
    prev1IsLiteral = true;
    hasPrev2 = true;
    optPrev2 = paramInt2;
    backPrev2 = paramInt3;
  }
}
