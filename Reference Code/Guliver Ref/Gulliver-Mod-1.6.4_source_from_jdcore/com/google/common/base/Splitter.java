package com.google.common.base;

import java.util.Iterator;
import javax.annotation.CheckReturnValue;






























































































public final class Splitter
{
  private final CharMatcher trimmer;
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final int limit;
  
  private Splitter(Strategy strategy)
  {
    this(strategy, false, CharMatcher.NONE, Integer.MAX_VALUE);
  }
  
  private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit)
  {
    this.strategy = strategy;
    this.omitEmptyStrings = omitEmptyStrings;
    this.trimmer = trimmer;
    this.limit = limit;
  }
  







  public static Splitter on(char separator)
  {
    return on(CharMatcher.is(separator));
  }
  









  public static Splitter on(CharMatcher separatorMatcher)
  {
    Preconditions.checkNotNull(separatorMatcher);
    
    new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
        new Splitter.SplittingIterator(splitter, toSplit) {
          int separatorStart(int start) {
            return val$separatorMatcher.indexIn(toSplit, start);
          }
          
          int separatorEnd(int separatorPosition) {
            return separatorPosition + 1;
          }
        };
      }
    });
  }
  























































































































































  @CheckReturnValue
  public Splitter omitEmptyStrings()
  {
    return new Splitter(strategy, true, trimmer, limit);
  }
  


































  @CheckReturnValue
  public Splitter trimResults()
  {
    return trimResults(CharMatcher.WHITESPACE);
  }
  











  @CheckReturnValue
  public Splitter trimResults(CharMatcher trimmer)
  {
    Preconditions.checkNotNull(trimmer);
    return new Splitter(strategy, omitEmptyStrings, trimmer, limit);
  }
  






  public Iterable<String> split(final CharSequence sequence)
  {
    Preconditions.checkNotNull(sequence);
    
    new Iterable() {
      public Iterator<String> iterator() {
        return Splitter.this.spliterator(sequence);
      }
      
      public String toString() { return ']'; }
    };
  }
  



  private Iterator<String> spliterator(CharSequence sequence)
  {
    return strategy.iterator(this, sequence);
  }
  





















  private static abstract class SplittingIterator
    extends AbstractIterator<String>
  {
    final CharSequence toSplit;
    




















    final CharMatcher trimmer;
    




















    final boolean omitEmptyStrings;
    




















    abstract int separatorStart(int paramInt);
    




















    int offset = 0;
    
    abstract int separatorEnd(int paramInt);
    
    protected SplittingIterator(Splitter splitter, CharSequence toSplit) { trimmer = trimmer;
      omitEmptyStrings = omitEmptyStrings;
      limit = limit;
      this.toSplit = toSplit;
    }
    


    int limit;
    

    protected String computeNext()
    {
      int nextStart = offset;
      while (offset != -1) {
        int start = nextStart;
        

        int separatorPosition = separatorStart(offset);
        int end; if (separatorPosition == -1) {
          int end = toSplit.length();
          offset = -1;
        } else {
          end = separatorPosition;
          offset = separatorEnd(separatorPosition);
        }
        if (offset == nextStart)
        {






          offset += 1;
          if (offset >= toSplit.length()) {
            offset = -1;
          }
        }
        else
        {
          while ((start < end) && (trimmer.matches(toSplit.charAt(start)))) {
            start++;
          }
          while ((end > start) && (trimmer.matches(toSplit.charAt(end - 1)))) {
            end--;
          }
          
          if ((omitEmptyStrings) && (start == end))
          {
            nextStart = offset;
          }
          else
          {
            if (limit == 1)
            {


              end = toSplit.length();
              offset = -1;
              
              while ((end > start) && (trimmer.matches(toSplit.charAt(end - 1)))) {
                end--;
              }
            }
            limit -= 1;
            

            return toSplit.subSequence(start, end).toString();
          } } }
      return (String)endOfData();
    }
  }
  
  private static abstract interface Strategy
  {
    public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
  }
}
