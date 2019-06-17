package org.tukaani.xz.lzma;

final class State
{
  private int state;
  
  State() {}
  
  void reset()
  {
    state = 0;
  }
  
  int get()
  {
    return state;
  }
  
  void set(State paramState)
  {
    state = state;
  }
  
  void updateLiteral()
  {
    if (state <= 3) {
      state = 0;
    } else if (state <= 9) {
      state -= 3;
    } else {
      state -= 6;
    }
  }
  
  void updateMatch()
  {
    state = (state < 7 ? 7 : 10);
  }
  
  void updateLongRep()
  {
    state = (state < 7 ? 8 : 11);
  }
  
  void updateShortRep()
  {
    state = (state < 7 ? 9 : 11);
  }
  
  boolean isLiteral()
  {
    return state < 7;
  }
}
