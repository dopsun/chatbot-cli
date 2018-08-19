# chatbot-cli
[![Build Status](https://travis-ci.org/dopsun/chatbot-cli.svg?branch=master)](https://travis-ci.org/dopsun/chatbot-cli)

A simple ChatBot command line parser, trained with templates.

* It's designed for Command Line Interface (CLI) for ChatBot, with predefined list of command syntax.
  * This is *NOT* a NLP (Natural Language Processing) parser.
* It's *smart*, but predictable.

# Train
```java
public static void prepareParser() throws URISyntaxException {
    URL url = ClassLoader.getSystemResource("input/training-data.properties");
    Path dsPath = Paths.get(url.toURI());

    TemplateDataSet dataSet = new TemplateDataSet(dsPath);

    SmlCliParserBuilder builder = new SmlCliParserBuilder();
    builder.add(dataSet);
    cliParser = builder.build();
}
```
In the above example, a [CliParser](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/CliParser.java)  is created based on taining data set [Example](chatbot-cli/src/test/resources/input/training-data.properties).

# Parse
```java
Optional<CliParseResult> optResult = cliParser.tryParse(command);
```
If `optResult`, is present, parse succeed. And returned [CliParseResult](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/CliParseResult.java) includes command.

# Work in process

Here are items pending to implement:
* Tolerance for spelling errors
* More training syntax support
* performance improvements
