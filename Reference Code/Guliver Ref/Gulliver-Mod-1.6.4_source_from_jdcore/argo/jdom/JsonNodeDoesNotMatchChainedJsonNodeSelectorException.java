package argo.jdom;

import java.util.LinkedList;
import java.util.List;






final class JsonNodeDoesNotMatchChainedJsonNodeSelectorException
  extends JsonNodeDoesNotMatchJsonNodeSelectorException
{
  final Functor failedNode;
  final List<JsonNodeSelector> failPath;
  
  static JsonNodeDoesNotMatchJsonNodeSelectorException createJsonNodeDoesNotMatchJsonNodeSelectorException(Functor failedNode)
  {
    return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(failedNode, new LinkedList());
  }
  
  static JsonNodeDoesNotMatchJsonNodeSelectorException createChainedJsonNodeDoesNotMatchJsonNodeSelectorException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException e, JsonNodeSelector parentJsonNodeSelector)
  {
    LinkedList<JsonNodeSelector> chainedFailPath = new LinkedList(failPath);
    chainedFailPath.add(parentJsonNodeSelector);
    return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(failedNode, chainedFailPath);
  }
  
  static JsonNodeDoesNotMatchJsonNodeSelectorException createUnchainedJsonNodeDoesNotMatchJsonNodeSelectorException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException e, JsonNodeSelector parentJsonNodeSelector)
  {
    LinkedList<JsonNodeSelector> unchainedFailPath = new LinkedList();
    unchainedFailPath.add(parentJsonNodeSelector);
    return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(failedNode, unchainedFailPath);
  }
  


  private JsonNodeDoesNotMatchChainedJsonNodeSelectorException(Functor failedNode, List<JsonNodeSelector> failPath)
  {
    super("Failed to match any JSON node at [" + getShortFormFailPath(failPath) + "]");
    this.failedNode = failedNode;
    this.failPath = failPath;
  }
  
  static String getShortFormFailPath(List<JsonNodeSelector> failPath) {
    StringBuilder result = new StringBuilder();
    for (int i = failPath.size() - 1; i >= 0; i--) {
      result.append(((JsonNodeSelector)failPath.get(i)).shortForm());
      if (i != 0) {
        result.append(".");
      }
    }
    return result.toString();
  }
  
  public String toString()
  {
    return "JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=" + failedNode + ", failPath=" + failPath + '}';
  }
}
