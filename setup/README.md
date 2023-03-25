# System Requirements and Setup

## System Requirements

* LTS version 17 of the [Java SDK](https://adoptopenjdk.net/), other versions later than 17 might also work but are not tested
* A Java IDE (like [Eclipse](https://www.eclipse.org/downloads/), [SpringToolSuite](https://spring.io/tools), [IntelliJ](https://www.jetbrains.com/idea/download), [Visual Studio Code](https://code.visualstudio.com/))
* [Postman](https://www.getpostman.com/downloads/) to test requests to the REST Api

## Setup

### Import the workshop project into your IDE

1. Clone the git repository https://github.com/andifalk/cloud-security-workshop or download it as zip file
2. Import the whole directory into your Java IDE as __Maven__ project
  * IntelliJ: File/New/Project from existing sources -> Select directory -> Select __Maven__ in next step
  * Eclipse: File/Import -> Select '__Maven__'/'__Existing Maven Projects__' -> Select directory -> Click 'Finish'
  * Visual Studio Code: Just open the directory with VS Code -> VS Code should automatically configure the project
3. You might have to explicitly trigger an update for the maven configuration to load dependencies (depending on your IDE)

### Identity Provider

