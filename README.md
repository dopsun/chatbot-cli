# chatbot-cli
[![Build Status](https://travis-ci.org/dopsun/chatbot-cli.svg?branch=master)](https://travis-ci.org/dopsun/chatbot-cli)

# What, What and How
## Why
This is designed specifically for enterprise chat bot command line interface (CLI). By targeting enterprise chat bot, I mean:

* each chat bot has finite list of capabilities,
* users of chatbots have been communicated with its capabilities,
* developers behind chatbot has domain knowledge while building chatbot

These characteristics adding up, means it's very different from public facing general purpose chat bot. And we need a special designed CLI to help enterprise chatbot build.

## What
In the heart of `chatbot-cli`, it contains a [Parser](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/Parser.java) parses users input to command with parameters. And from there, enterprise bot developers can implement different functions.

Here are parser features implemented/ planned:

* Command is defined by template with parameter placeholders.
  * One command can have multiple templates defined.
  * In a single template, it may have optional/ alternative formates of certain part of the command.
* Multiple commands can be matched for a certain input
  * All these matched commands are rated with rank, and highest rank chose by default, but dewveloper of chatbot can choose alternatives if needed.

*To be added*

* Command ranking is resolved by predefined rules together with [Reinforcement Machine Learning](https://en.wikipedia.org/wiki/Reinforcement_learning)
  * Selected command and user feedback can be recorded and as factors for resolving command in future.
  * User feedback cna be positive and negative, or no feedback.
* 
* Bot developer can extend the resolve logic to customize the matching logic.

## How

A simple ChatBot command line parser, trained with templates.

* It's designed for Command Line Interface (CLI) for ChatBot, with predefined list of command syntax.
  * This is *NOT* a NLP (Natural Language Processing) parser.
* It's *smart* and predictable.
* *To Be Added* [Reinforcement learning](https://en.wikipedia.org/wiki/Reinforcement_learning)

### Build
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

### Parse
```java
Optional<ParseResult> optResult = parser.tryParse(command);
```
If `optResult`, is present, parse succeed. And returned [ParseResult](chatbot-cli/src/main/java/com/dopsun/chatbot/cli/ParseResult.java) includes command.

### Continuous improving
*To Be Added* Successfully parsing result, along with feedback, will be stored as training set, and improve parser working better next time.

# Comments and suggestions
Raising issues if you have any feedback or reporting bugs.