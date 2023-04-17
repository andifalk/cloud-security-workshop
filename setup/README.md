# Setup

## Import the workshop project into your IDE

1. Clone the git repository https://github.com/andifalk/cloud-security-workshop or download it as zip file
2. Import the whole directory into your Java IDE as __Maven__ project
   * IntelliJ: File/New/Project from existing sources -> Select directory -> Select __Maven__ in next step
   * Eclipse: File/Import -> Select '__Maven__'/'__Existing Maven Projects__' -> Select directory -> Click 'Finish'
   * Visual Studio Code: Just open the directory with VS Code -> VS Code should automatically configure the project
3. You might have to explicitly trigger an update for the maven configuration to load dependencies (depending on your IDE)

## Run the java applications

All spring boot based java projects can either be run using your Java IDE or using the command line
with changing into the corresponding project directory and issuing a `./gradlew bootRun` command.

In this workshop we will use a customized version of [Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server) as local identity provider.  
[Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server) implements OAuth 2.0 and OpenID Connect 1.0.

## Identity Providers

This workshop requires a OAuth 2.0 / OpenID Connect 1.0 compliant identity provider.

### Setup Spring Authorization Server

Here we will use [Customized Version of Spring Authorization Server](https://github.com/andifalk/custom-spring-authorization-server).

To set up and run this project:

1. Clone or download the GitHub repository at: [https://github.com/andifalk/custom-spring-authorization-server](https://github.com/andifalk/custom-spring-authorization-server)
2. Import this project into your IDE as a gradle project
3. After the IDE has configured the project you can start the authorization server by running the main class _com.example.spring.authorizationserver.SpringAuthorizationServerApplication_

#### Check Running Server

The spring authorization server does not have a UI. Instead, to prove it is running just open the web browser and navigate to http://localhost:9000/.well-known/openid-configuration.

Now, if you see the openid configuration in the browser (how nice it is shown depends on your browser addons) then spring authorization server is ready to use it for this workshop.

![Openid Configuration](images/openid_config.png)

