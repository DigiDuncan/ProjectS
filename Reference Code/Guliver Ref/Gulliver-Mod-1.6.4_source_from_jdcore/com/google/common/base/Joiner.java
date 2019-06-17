package com.google.common.base;

import java.io.IOException;
import java.util.Iterator;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;


























































public class Joiner
{
  private final String separator;
  
  public static Joiner on(String separator)
  {
    return new Joiner(separator);
  }
  








  private Joiner(String separator)
  {
    this.separator = ((String)Preconditions.checkNotNull(separator));
  }
  
  private Joiner(Joiner prototype) {
    separator = separator;
  }
  




























  public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts)
    throws IOException
  {
    Preconditions.checkNotNull(appendable);
    if (parts.hasNext()) {
      appendable.append(toString(parts.next()));
      while (parts.hasNext()) {
        appendable.append(separator);
        appendable.append(toString(parts.next()));
      }
    }
    return appendable;
  }
  





































  public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts)
  {
    return appendTo(builder, parts.iterator());
  }
  





  public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts)
  {
    try
    {
      appendTo(builder, parts);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
    return builder;
  }
  





































  public final String join(Iterable<?> parts)
  {
    return join(parts.iterator());
  }
  





  public final String join(Iterator<?> parts)
  {
    return appendTo(new StringBuilder(), parts).toString();
  }
  



















  @CheckReturnValue
  public Joiner useForNull(final String nullText)
  {
    Preconditions.checkNotNull(nullText);
    new Joiner(this, nullText) {
      CharSequence toString(@Nullable Object part) {
        return part == null ? nullText : Joiner.this.toString(part);
      }
      
      public Joiner useForNull(String nullText) {
        Preconditions.checkNotNull(nullText);
        throw new UnsupportedOperationException("already specified useForNull");
      }
    };
  }
  















































  @CheckReturnValue
  public MapJoiner withKeyValueSeparator(String keyValueSeparator)
  {
    return new MapJoiner(this, keyValueSeparator, null);
  }
  






  public static final class MapJoiner
  {
    private final Joiner joiner;
    




    private final String keyValueSeparator;
    





    private MapJoiner(Joiner joiner, String keyValueSeparator)
    {
      this.joiner = joiner;
      this.keyValueSeparator = ((String)Preconditions.checkNotNull(keyValueSeparator));
    }
  }
  

















































































































































































  CharSequence toString(Object part)
  {
    Preconditions.checkNotNull(part);
    return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
  }
}
