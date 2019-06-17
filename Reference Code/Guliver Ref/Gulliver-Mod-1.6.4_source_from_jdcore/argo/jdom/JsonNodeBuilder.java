package argo.jdom;

public abstract interface JsonNodeBuilder<T extends JsonNode>
{
  public abstract T build();
}
