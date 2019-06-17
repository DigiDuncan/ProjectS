package argo.format;













final class JsonEscapedString
{
  static String escapeString(String unescapedString)
  {
    return unescapedString.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
  }
}
