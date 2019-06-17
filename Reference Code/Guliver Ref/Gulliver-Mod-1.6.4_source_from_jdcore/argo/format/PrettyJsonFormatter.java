package argo.format;

import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

















public final class PrettyJsonFormatter
  implements JsonFormatter
{
  private final JsonFormatter.FieldSorter fieldSorter;
  
  public PrettyJsonFormatter()
  {
    this(DO_NOTHING_FIELD_SORTER);
  }
  
  private PrettyJsonFormatter(JsonFormatter.FieldSorter fieldSorter) {
    this.fieldSorter = fieldSorter;
  }
  




  public static PrettyJsonFormatter fieldOrderPreservingPrettyJsonFormatter()
  {
    return new PrettyJsonFormatter();
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
    formatJsonNode(jsonNode, new PrintWriter(writer), 0);
  }
  
  private void formatJsonNode(JsonNode jsonNode, PrintWriter writer, int indent) throws IOException {
    switch (1.$SwitchMap$argo$jdom$JsonNodeType[jsonNode.getType().ordinal()]) {
    case 1: 
      writer.append('[');
      Iterator<JsonNode> elements = jsonNode.getElements().iterator();
      while (elements.hasNext()) {
        JsonNode node = (JsonNode)elements.next();
        writer.println();
        addTabs(writer, indent + 1);
        formatJsonNode(node, writer, indent + 1);
        if (elements.hasNext()) {
          writer.append(",");
        }
      }
      if (!jsonNode.getElements().isEmpty()) {
        writer.println();
        addTabs(writer, indent);
      }
      writer.append(']');
      break;
    case 2: 
      writer.append('{');
      Iterator<JsonField> jsonStringNodes = fieldSorter.sort(jsonNode.getFieldList()).iterator();
      while (jsonStringNodes.hasNext()) {
        JsonField field = (JsonField)jsonStringNodes.next();
        writer.println();
        addTabs(writer, indent + 1);
        formatJsonNode(field.getName(), writer, indent + 1);
        writer.append(": ");
        formatJsonNode(field.getValue(), writer, indent + 1);
        if (jsonStringNodes.hasNext()) {
          writer.append(",");
        }
      }
      if (!jsonNode.getFieldList().isEmpty()) {
        writer.println();
        addTabs(writer, indent);
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
  
  private void addTabs(PrintWriter writer, int tabs) {
    for (int i = 0; i < tabs; i++) {
      writer.write(9);
    }
  }
}
