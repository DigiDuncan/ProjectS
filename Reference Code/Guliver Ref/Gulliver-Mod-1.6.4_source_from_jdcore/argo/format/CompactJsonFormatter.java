package argo.format;

import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;















public final class CompactJsonFormatter
  implements JsonFormatter
{
  private final JsonFormatter.FieldSorter fieldSorter;
  
  public CompactJsonFormatter()
  {
    this(DO_NOTHING_FIELD_SORTER);
  }
  
  private CompactJsonFormatter(JsonFormatter.FieldSorter fieldSorter) {
    this.fieldSorter = fieldSorter;
  }
  




  public static CompactJsonFormatter fieldOrderPreservingCompactJsonFormatter()
  {
    return new CompactJsonFormatter();
  }
  











  public String format(JsonRootNode jsonNode)
  {
    StringWriter stringWriter = new StringWriter();
    try {
      format(jsonNode, stringWriter);
    } catch (IOException e) {
      throw new RuntimeException("Coding failure in Argo:  StringWriter threw an IOException", e);
    }
    return stringWriter.toString();
  }
  
  public void format(JsonRootNode jsonNode, Writer writer) throws IOException {
    formatJsonNode(jsonNode, writer);
  }
  
  private void formatJsonNode(JsonNode jsonNode, Writer writer) throws IOException {
    boolean first = true;
    switch (1.$SwitchMap$argo$jdom$JsonNodeType[jsonNode.getType().ordinal()]) {
    case 1: 
      writer.append('[');
      for (JsonNode node : jsonNode.getElements()) {
        if (!first) {
          writer.append(',');
        }
        first = false;
        formatJsonNode(node, writer);
      }
      writer.append(']');
      break;
    case 2: 
      writer.append('{');
      for (JsonField field : fieldSorter.sort(jsonNode.getFieldList())) {
        if (!first) {
          writer.append(',');
        }
        first = false;
        formatJsonNode(field.getName(), writer);
        writer.append(':');
        formatJsonNode(field.getValue(), writer);
      }
      writer.append('}');
      break;
    case 3: 
      writer.append('"').append(JsonEscapedString.escapeString(jsonNode.getText())).append('"');
      

      break;
    case 4: 
      writer.append(jsonNode.getText());
      break;
    case 5: 
      writer.append("false");
      break;
    case 6: 
      writer.append("true");
      break;
    case 7: 
      writer.append("null");
      break;
    default: 
      throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + jsonNode.getType() + "];");
    }
  }
}
