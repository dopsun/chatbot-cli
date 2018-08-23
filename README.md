# chatbot-cli
[![Build Status](https://travis-ci.org/dopsun/chatbot-cli.svg?branch=master)](https://travis-ci.org/dopsun/chatbot-cli)

A simple ChatBot command line parser, trained with templates.

* It's designed for Command Line Interface (CLI) for ChatBot, with predefined list of command syntax.
  * This is *NOT* a NLP (Natural Language Processing) parser.
* It's *smart* and predictable.
* *To Be Added* [Reinforcement learning](https://en.wikipedia.org/wiki/Reinforcement_learning)

# Build
```java
public static void prepareParser() throws URISyntaxException {
    URL csUrl = ClassLoader.getSystemResource("input/command-data.properties");
    Path csPath = Paths.get(csUrl.toURI());

    CommandSetReader csReader = new CommandSetReader();
    CommandSet commandSet = csReader.read(csPath);

    URL tsUrl = ClassLoader.getSystemResource("input/training-data.yml");
    Path tsPath = Paths.get(tsUrl.toURI());
    TrainingSetReader tsReader = new TrainingSetReader();
    TrainingSet trainingSet = tsReader.read(tsPath);

    ParserBuilder parserBuilder = Parser.newBuilder();
    parserBuilder.addCommandSet(commandSet);
    parserBuilder.addTrainingSet(trainingSet);

    parserBuilder.setTracer(System.out::println);
    parser = parserBuilder.build();
 }
```
In the above example, a [Parser](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/Parser.java)  is created based on command data set [Example](chatbot-cli/src/test/resources/input/command-data.properties).

# Parse
```java
Optional<ParseResult> optResult = parser.tryParse(command);
```
If `optResult`, is present, parse succeed. And returned [ParseResult](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/ParseResult.java) includes command.

# Continuous improving
*To Be Added* Successfully parsing result, along with feedback, will be stored as training set, and improve parser working better next time.

# Work in process

Here are items pending to implement:
* Tolerance for spelling errors
* More command template syntax support
* Reinforcement learning
* performance improvements
