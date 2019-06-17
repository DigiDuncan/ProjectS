package joptsimple.internal;

import java.util.LinkedHashSet;
import java.util.Set;
































public class Rows
{
  private final int overallWidth;
  private final int columnSeparatorWidth;
  private final Set<Row> rows = new LinkedHashSet();
  private int widthOfWidestOption;
  private int widthOfWidestDescription;
  
  public Rows(int overallWidth, int columnSeparatorWidth) {
    this.overallWidth = overallWidth;
    this.columnSeparatorWidth = columnSeparatorWidth;
  }
  
  public void add(String option, String description) {
    add(new Row(option, description));
  }
  
  private void add(Row row) {
    rows.add(row);
    widthOfWidestOption = Math.max(widthOfWidestOption, option.length());
    widthOfWidestDescription = Math.max(widthOfWidestDescription, description.length());
  }
  
  private void reset() {
    rows.clear();
    widthOfWidestOption = 0;
    widthOfWidestDescription = 0;
  }
  
  public void fitToWidth() {
    Columns columns = new Columns(optionWidth(), descriptionWidth());
    
    Set<Row> fitted = new LinkedHashSet();
    for (Row each : rows) {
      fitted.addAll(columns.fit(each));
    }
    reset();
    
    for (Row each : fitted)
      add(each);
  }
  
  public String render() {
    StringBuilder buffer = new StringBuilder();
    
    for (Row each : rows) {
      pad(buffer, option, optionWidth()).append(Strings.repeat(' ', columnSeparatorWidth));
      pad(buffer, description, descriptionWidth()).append(Strings.LINE_SEPARATOR);
    }
    
    return buffer.toString();
  }
  
  private int optionWidth() {
    return Math.min((overallWidth - columnSeparatorWidth) / 2, widthOfWidestOption);
  }
  
  private int descriptionWidth() {
    return Math.min((overallWidth - columnSeparatorWidth) / 2, widthOfWidestDescription);
  }
  
  private StringBuilder pad(StringBuilder buffer, String s, int length) {
    buffer.append(s).append(Strings.repeat(' ', length - s.length()));
    return buffer;
  }
}
