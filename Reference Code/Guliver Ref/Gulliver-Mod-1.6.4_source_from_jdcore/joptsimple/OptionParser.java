package joptsimple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import joptsimple.internal.AbbreviationMap;
import joptsimple.util.KeyValuePair;















































































































































































public class OptionParser
{
  private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
  private final Map<Collection<String>, Set<OptionSpec<?>>> requiredIf;
  private OptionParserState state;
  private boolean posixlyCorrect;
  private boolean allowsUnrecognizedOptions;
  private HelpFormatter helpFormatter = new BuiltinHelpFormatter();
  



  public OptionParser()
  {
    recognizedOptions = new AbbreviationMap();
    requiredIf = new HashMap();
    state = OptionParserState.moreOptions(false);
    
    recognize(new NonOptionArgumentSpec());
  }
  

















































  public OptionSpecBuilder accepts(String option, String description)
  {
    return acceptsAll(Collections.singletonList(option), description);
  }
  
























  public OptionSpecBuilder acceptsAll(Collection<String> options, String description)
  {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("need at least one option");
    }
    ParserRules.ensureLegalOptions(options);
    
    return new OptionSpecBuilder(this, options, description);
  }
  






















































  boolean doesAllowsUnrecognizedOptions()
  {
    return allowsUnrecognizedOptions;
  }
  











  void recognize(AbstractOptionSpec<?> spec)
  {
    recognizedOptions.putAll(spec.options(), spec);
  }
  








  public void printHelpOn(OutputStream sink)
    throws IOException
  {
    printHelpOn(new OutputStreamWriter(sink));
  }
  








  public void printHelpOn(Writer sink)
    throws IOException
  {
    sink.write(helpFormatter.format(recognizedOptions.toJavaUtilMap()));
    sink.flush();
  }
  




















  public OptionSet parse(String... arguments)
  {
    ArgumentList argumentList = new ArgumentList(arguments);
    OptionSet detected = new OptionSet(defaultValues());
    detected.add((AbstractOptionSpec)recognizedOptions.get("[arguments]"));
    
    while (argumentList.hasMore()) {
      state.handleArgument(this, argumentList, detected);
    }
    reset();
    
    ensureRequiredOptions(detected);
    
    return detected;
  }
  
  private void ensureRequiredOptions(OptionSet options) {
    Collection<String> missingRequiredOptions = missingRequiredOptions(options);
    boolean helpOptionPresent = isHelpOptionPresent(options);
    
    if ((!missingRequiredOptions.isEmpty()) && (!helpOptionPresent))
      throw new MissingRequiredOptionException(missingRequiredOptions);
  }
  
  private Collection<String> missingRequiredOptions(OptionSet options) {
    Collection<String> missingRequiredOptions = new HashSet();
    
    for (AbstractOptionSpec<?> each : recognizedOptions.toJavaUtilMap().values()) {
      if ((each.isRequired()) && (!options.has(each))) {
        missingRequiredOptions.addAll(each.options());
      }
    }
    for (Map.Entry<Collection<String>, Set<OptionSpec<?>>> eachEntry : requiredIf.entrySet()) {
      AbstractOptionSpec<?> required = specFor((String)((Collection)eachEntry.getKey()).iterator().next());
      
      if ((optionsHasAnyOf(options, (Collection)eachEntry.getValue())) && (!options.has(required))) {
        missingRequiredOptions.addAll(required.options());
      }
    }
    
    return missingRequiredOptions;
  }
  
  private boolean optionsHasAnyOf(OptionSet options, Collection<OptionSpec<?>> specs) {
    for (OptionSpec<?> each : specs) {
      if (options.has(each)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isHelpOptionPresent(OptionSet options) {
    boolean helpOptionPresent = false;
    for (AbstractOptionSpec<?> each : recognizedOptions.toJavaUtilMap().values()) {
      if ((each.isForHelp()) && (options.has(each))) {
        helpOptionPresent = true;
        break;
      }
    }
    return helpOptionPresent;
  }
  
  void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
    KeyValuePair optionAndArgument = parseLongOptionWithArgument(candidate);
    
    if (!isRecognized(key)) {
      throw OptionException.unrecognizedOption(key);
    }
    AbstractOptionSpec<?> optionSpec = specFor(key);
    optionSpec.handleOption(this, arguments, detected, value);
  }
  
  void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
    KeyValuePair optionAndArgument = parseShortOptionWithArgument(candidate);
    
    if (isRecognized(key)) {
      specFor(key).handleOption(this, arguments, detected, value);
    }
    else
      handleShortOptionCluster(candidate, arguments, detected);
  }
  
  private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected) {
    char[] options = extractShortOptionsFrom(candidate);
    validateOptionCharacters(options);
    
    for (int i = 0; i < options.length; i++) {
      AbstractOptionSpec<?> optionSpec = specFor(options[i]);
      
      if ((optionSpec.acceptsArguments()) && (options.length > i + 1)) {
        String detectedArgument = String.valueOf(options, i + 1, options.length - 1 - i);
        optionSpec.handleOption(this, arguments, detected, detectedArgument);
        break;
      }
      
      optionSpec.handleOption(this, arguments, detected, null);
    }
  }
  
  void handleNonOptionArgument(String candidate, ArgumentList arguments, OptionSet detectedOptions) {
    specFor("[arguments]").handleOption(this, arguments, detectedOptions, candidate);
  }
  
  void noMoreOptions() {
    state = OptionParserState.noMoreOptions();
  }
  



  boolean isRecognized(String option)
  {
    return recognizedOptions.contains(option);
  }
  



















  private AbstractOptionSpec<?> specFor(char option)
  {
    return specFor(String.valueOf(option));
  }
  
  private AbstractOptionSpec<?> specFor(String option) {
    return (AbstractOptionSpec)recognizedOptions.get(option);
  }
  
  private void reset() {
    state = OptionParserState.moreOptions(posixlyCorrect);
  }
  
  private static char[] extractShortOptionsFrom(String argument) {
    char[] options = new char[argument.length() - 1];
    argument.getChars(1, argument.length(), options, 0);
    
    return options;
  }
  
  private void validateOptionCharacters(char[] options) {
    for (char each : options) {
      String option = String.valueOf(each);
      
      if (!isRecognized(option)) {
        throw OptionException.unrecognizedOption(option);
      }
      if (specFor(option).acceptsArguments())
        return;
    }
  }
  
  private static KeyValuePair parseLongOptionWithArgument(String argument) {
    return KeyValuePair.valueOf(argument.substring(2));
  }
  
  private static KeyValuePair parseShortOptionWithArgument(String argument) {
    return KeyValuePair.valueOf(argument.substring(1));
  }
  
  private Map<String, List<?>> defaultValues() {
    Map<String, List<?>> defaults = new HashMap();
    for (Map.Entry<String, AbstractOptionSpec<?>> each : recognizedOptions.toJavaUtilMap().entrySet())
      defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues());
    return defaults;
  }
}
