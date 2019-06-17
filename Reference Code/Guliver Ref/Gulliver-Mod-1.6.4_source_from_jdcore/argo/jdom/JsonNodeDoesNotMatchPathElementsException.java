package argo.jdom;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;












final class JsonNodeDoesNotMatchPathElementsException
  extends JsonNodeDoesNotMatchJsonNodeSelectorException
{
  private static final JsonFormatter JSON_FORMATTER = ;
  
  static JsonNodeDoesNotMatchPathElementsException jsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, JsonRootNode rootNode) {
    return new JsonNodeDoesNotMatchPathElementsException(delegate, pathElements, rootNode);
  }
  
  private JsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, JsonRootNode rootNode) {
    super(formatMessage(delegate, pathElements, rootNode));
  }
  
  private static String formatMessage(JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, JsonRootNode rootNode) {
    return "Failed to find " + failedNode.toString() + " at [" + JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(failPath) + "] while resolving [" + commaSeparate(pathElements) + "] in " + JSON_FORMATTER.format(rootNode) + ".";
  }
  
  private static String commaSeparate(Object[] pathElements) {
    StringBuilder result = new StringBuilder();
    boolean firstElement = true;
    for (Object pathElement : pathElements) {
      if (!firstElement) {
        result.append(".");
      }
      firstElement = false;
      if ((pathElement instanceof String)) {
        result.append("\"").append(pathElement).append("\"");
      } else {
        result.append(pathElement);
      }
    }
    return result.toString();
  }
}
