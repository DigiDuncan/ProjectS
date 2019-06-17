package com.google.common.base;

import java.util.Arrays;




























































public abstract class CharMatcher
{
  public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher()
  {
    public boolean matches(char c) {
      switch (c) {
      case '\t': 
      case '\n': 
      case '\013': 
      case '\f': 
      case '\r': 
      case ' ': 
      case '': 
      case ' ': 
      case ' ': 
      case ' ': 
      case ' ': 
      case '　': 
        return true;
      case ' ': 
        return false;
      }
      return (c >= ' ') && (c <= ' ');
    }
    

    public String toString()
    {
      return "CharMatcher.BREAKING_WHITESPACE";
    }
  };
  



  public static final CharMatcher ASCII = inRange('\000', '', "CharMatcher.ASCII");
  private static final String NINES;
  
  private static class RangesMatcher extends CharMatcher {
    private final char[] rangeStarts;
    private final char[] rangeEnds;
    
    RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) { super();
      this.rangeStarts = rangeStarts;
      this.rangeEnds = rangeEnds;
      Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
      for (int i = 0; i < rangeStarts.length; i++) {
        Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
        if (i + 1 < rangeStarts.length) {
          Preconditions.checkArgument(rangeEnds[i] < rangeStarts[(i + 1)]);
        }
      }
    }
    
    public boolean matches(char c)
    {
      int index = Arrays.binarySearch(rangeStarts, c);
      if (index >= 0) {
        return true;
      }
      index = (index ^ 0xFFFFFFFF) - 1;
      return (index >= 0) && (c <= rangeEnds[index]);
    }
  }
  






  static
  {
    StringBuilder builder = new StringBuilder("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length());
    for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
      builder.append((char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + '\t'));
    }
    NINES = builder.toString();
  }
  




  public static final CharMatcher DIGIT = new RangesMatcher("CharMatcher.DIGIT", "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray(), NINES.toCharArray());
  





  public static final CharMatcher JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
    public boolean matches(char c) {
      return Character.isDigit(c);
    }
  };
  





  public static final CharMatcher JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
    public boolean matches(char c) {
      return Character.isLetter(c);
    }
  };
  




  public static final CharMatcher JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT")
  {
    public boolean matches(char c) {
      return Character.isLetterOrDigit(c);
    }
  };
  




  public static final CharMatcher JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE")
  {
    public boolean matches(char c) {
      return Character.isUpperCase(c);
    }
  };
  




  public static final CharMatcher JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE")
  {
    public boolean matches(char c) {
      return Character.isLowerCase(c);
    }
  };
  




  public static final CharMatcher JAVA_ISO_CONTROL = inRange('\000', '\037').or(inRange('', '')).withToString("CharMatcher.JAVA_ISO_CONTROL");
  








  public static final CharMatcher INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\000­؀۝܏ ᠎   ⁪　?﻿￹￺".toCharArray(), "  ­؄۝܏ ᠎‏ ⁤⁯　﻿￹￻".toCharArray());
  



  private static String showCharacter(char c)
  {
    String hex = "0123456789ABCDEF";
    char[] tmp = { '\\', 'u', '\000', '\000', '\000', '\000' };
    for (int i = 0; i < 4; i++) {
      tmp[(5 - i)] = hex.charAt(c & 0xF);
      c = (char)(c >> '\004');
    }
    return String.copyValueOf(tmp);
  }
  









  public static final CharMatcher SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
  



  public static final CharMatcher ANY = new FastMatcher("CharMatcher.ANY")
  {
    public boolean matches(char c) {
      return true;
    }
    



    public int indexIn(CharSequence sequence, int start)
    {
      int length = sequence.length();
      Preconditions.checkPositionIndex(start, length);
      return start == length ? -1 : start;
    }
    
















































    public CharMatcher or(CharMatcher other)
    {
      Preconditions.checkNotNull(other);
      return this;
    }
  };
  





  public static final CharMatcher NONE = new FastMatcher("CharMatcher.NONE")
  {
    public boolean matches(char c) {
      return false;
    }
    




    public int indexIn(CharSequence sequence, int start)
    {
      int length = sequence.length();
      Preconditions.checkPositionIndex(start, length);
      return -1;
    }
    






















































    public CharMatcher or(CharMatcher other)
    {
      return (CharMatcher)Preconditions.checkNotNull(other);
    }
  };
  



  final String description;
  



  public static CharMatcher is(final char match)
  {
    String description = "CharMatcher.is('" + showCharacter(match) + "')";
    new FastMatcher(description) {
      public boolean matches(char c) {
        return c == match;
      }
      







      public CharMatcher or(CharMatcher other)
      {
        return other.matches(match) ? other : super.or(other);
      }
    };
  }
  

















































































































  public static CharMatcher inRange(char startInclusive, char endInclusive)
  {
    Preconditions.checkArgument(endInclusive >= startInclusive);
    String description = "CharMatcher.inRange('" + showCharacter(startInclusive) + "', '" + showCharacter(endInclusive) + "')";
    

    return inRange(startInclusive, endInclusive, description);
  }
  
  static CharMatcher inRange(final char startInclusive, final char endInclusive, String description)
  {
    new FastMatcher(description) {
      public boolean matches(char c) {
        return (startInclusive <= c) && (c <= endInclusive);
      }
    };
  }
  

































  CharMatcher(String description)
  {
    this.description = description;
  }
  



  protected CharMatcher()
  {
    description = super.toString();
  }
  










































































































  public CharMatcher or(CharMatcher other)
  {
    return new Or(this, (CharMatcher)Preconditions.checkNotNull(other));
  }
  
  private static class Or extends CharMatcher {
    final CharMatcher first;
    final CharMatcher second;
    
    Or(CharMatcher a, CharMatcher b, String description) {
      super();
      first = ((CharMatcher)Preconditions.checkNotNull(a));
      second = ((CharMatcher)Preconditions.checkNotNull(b));
    }
    
    Or(CharMatcher a, CharMatcher b) {
      this(a, b, "CharMatcher.or(" + a + ", " + b + ")");
    }
    







    public boolean matches(char c)
    {
      return (first.matches(c)) || (second.matches(c));
    }
    
    CharMatcher withToString(String description)
    {
      return new Or(first, second, description);
    }
  }
  


















  CharMatcher withToString(String description)
  {
    throw new UnsupportedOperationException();
  }
  
































  static abstract class FastMatcher
    extends CharMatcher
  {
    FastMatcher(String description)
    {
      super();
    }
  }
  



















































































































































































  public int indexIn(CharSequence sequence, int start)
  {
    int length = sequence.length();
    Preconditions.checkPositionIndex(start, length);
    for (int i = start; i < length; i++) {
      if (matches(sequence.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
  











































































































































































































































































































































  public String toString()
  {
    return description;
  }
  









































  public static final CharMatcher WHITESPACE = new FastMatcher("CharMatcher.WHITESPACE")
  {
    public boolean matches(char c) {
      return "\001\000 \000\000\000\000\000\000\t\n\013\f\r\000\000  \000\000\000\000\000 \000\000\000\000\000\000\000\000 \000\000\000\000\000\000\000\000\000\000　\000\000\000\000\000\000\000\000\000\000           \000\000\000\000\000  \000\000᠎\000\000\000".charAt(c % 'O') == c;
    }
  };
  
  public abstract boolean matches(char paramChar);
}
