package joptsimple;
















abstract class OptionParserState
{
  OptionParserState() {}
  














  static OptionParserState noMoreOptions()
  {
    new OptionParserState()
    {
      protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
        parser.handleNonOptionArgument(arguments.next(), arguments, detectedOptions);
      }
    };
  }
  
  static OptionParserState moreOptions(boolean posixlyCorrect) {
    new OptionParserState()
    {
      protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
        String candidate = arguments.next();
        try {
          if (ParserRules.isOptionTerminator(candidate)) {
            parser.noMoreOptions();
            return; }
          if (ParserRules.isLongOptionToken(candidate)) {
            parser.handleLongOptionToken(candidate, arguments, detectedOptions);
            return; }
          if (ParserRules.isShortOptionToken(candidate)) {
            parser.handleShortOptionToken(candidate, arguments, detectedOptions);
            return;
          }
        } catch (UnrecognizedOptionException e) {
          if (!parser.doesAllowsUnrecognizedOptions()) {
            throw e;
          }
        }
        if (val$posixlyCorrect) {
          parser.noMoreOptions();
        }
        parser.handleNonOptionArgument(candidate, arguments, detectedOptions);
      }
    };
  }
  
  protected abstract void handleArgument(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet);
}
