package argo.format;

import argo.jdom.JsonField;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
















public abstract interface JsonFormatter
{
  public static final FieldSorter DO_NOTHING_FIELD_SORTER = new FieldSorter() {
    public List<JsonField> sort(List<JsonField> unsorted) {
      return unsorted;
    }
  };
  
  public static final Comparator<JsonField> JSON_FIELD_COMPARATOR = new Comparator() {
    public int compare(JsonField jsonField, JsonField jsonField1) {
      return jsonField.getName().compareTo(jsonField1.getName());
    }
  };
  
  public static final FieldSorter ALPHABETIC_FIELD_SORTER = new FieldSorter() {
    public List<JsonField> sort(List<JsonField> unsorted) {
      List<JsonField> sorted = new ArrayList(unsorted);
      Collections.sort(sorted, JsonFormatter.JSON_FIELD_COMPARATOR);
      return sorted;
    }
  };
  
  public abstract String format(JsonRootNode paramJsonRootNode);
  
  public static abstract interface FieldSorter
  {
    public abstract List<JsonField> sort(List<JsonField> paramList);
  }
}
