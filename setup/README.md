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

## OpenID Connect Identity Providers

This workshop requires a OAuth 2.0 / OpenID Connect 1.0 compliant identity provider.
There are lots of products available in this area, i.e.:

* [Keycloak](https://keycloak.org) (Open Source IAM by RedHat)
* [Spring Authorization Server](https://spring.io/projects/spring-authorization-server) (Open Source IAM implemented by the Spring Framework community)
* [Auth0](https://auth0.com/) (Cloud based IAM service)
* [Okta](https://www.okta.com/) (Cloud based IAM service)
* [Azure Active Directory](https://azure.microsoft.com/en-us/products/active-directory/) (Well-known cloud-based IAM service by Microsoft)
* [ForgeRock](https://www.forgerock.com/) (Leading IAM product)

This workshop supports the following identity providers:

* [Keycloak](https://keycloak.org)
* [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
* [Auth0](https://auth0.com/)

The preferred default identity provider that is pre-configured for all workshop parts is the [Spring Authorization Server](https://spring.io/projects/spring-authorization-server).  
You will learn how to run this one in the following section.

### Spring Authorization Server

Here we will use a [customized version of the Spring Authorization Server](https://github.com/andifalk/custom-spring-authorization-server). This version contains pre-configured users and registered OAuth/OIDC clients for the sample applications we will use as part of this workshop.

To set up and run the customized Spring Authorizationserver:

1. Clone or download the GitHub repository at: [https://github.com/andifalk/custom-spring-authorization-server](https://github.com/andifalk/custom-spring-authorization-server)
2. Import this project into your IDE as a gradle project
3. After the IDE has configured the project you can start the authorization server by running the main class _com.example.spring.authorizationserver.SpringAuthorizationServerApplication_

#### Check Running Server

The spring authorization server runs on port _9000_. To validate that it is running as expected, please open the web browser and navigate to the [OpenID Connect discovery endpoint](http://localhost:9000/.well-known/openid-configuration).

If you can see the openid configuration in the browser (how nice it is shown depends on your browser addons) then spring authorization server is ready for use in this workshop.

![Openid Configuration](images/openid_config.png)

You can use the following users to log into the custom Spring Authorization Server:

| User / Password   | Role(s)     |
|-------------------|-------------|
| bwayne / wayne    | USER        |
| pparker / parker  | USER, ADMIN |
| ckent / kent      | USER        |

### Keycloak

To use Keycloak for this workshop, please follow these steps to install and run this identity provider:

1. Download the distribution from https://www.keycloak.org/downloads
2. Extract the downloaded archive (zip or tar.gzip) to a directory of your choice
3. Open a terminal and change directory to the directory you extracted the archive into
4. Create the subdirectory `data/import` and copy the file `workshop-realm.json` from the workshop directory `setup/keycloak` into the `data/import` subdirectory.
5. Change into the `bin` subdirectory of the directory you extracted the archive into
6. Run the command `./kc.sh start-dev --import-realm` or `kc.bat start-dev --import-realm` (depending on your operating system) to start Keycloak
7. Wait until Keycloak has started completely, then navigate your web browser to http://localhost:8080
8. Now let's create an initial admin user, just chose `admin` as both username and password and click create
9. After creating the admin user we can now log into the administration console, just click on the corresponding link and use your admin credentials for the login.
10. Initially, you should see the `master` realm page

![Keycloak_Master_Realm](images/keycloak_master_realm.png)

11. On the upper left you can change the realm. When starting Keycloak we have imported a custom realm called `workshop`. To switch realms just select the `workshop' realm in the drop-down box.
Next please select the menu item `Clients` on the left. You should see 3 configured user accounts like in the screenshot below.

![Keycloak_Users](images/keycloak_workshop_realm_users.png)

12. Finally, select the menu item `Clients` on the left. Here you should see the `product-client` in the client list.

![Keycloak_Clients](images/keycloak_workshop_realm_clients.png)

Now you are all set with Keycloak. To check the OpenID configuration for this `workshop` realm navigate your web browser to http://localhost:8080/realms/workshop/.well-known/openid-configuration.

You can use the following users to log into Keycloak:

| User / Password   | Role(s)     |
|-------------------|-------------|
| bwayne / wayne    | USER        |
| pparker / parker  | USER, ADMIN |
| ckent / kent      | USER        |

### Auth0

Using [Auth0](https://auth0.com/) for this workshop is quite easy. There is nothing to install.
Just make sure you can access the corresponding OpenID configuration of the Auth0 at https://access-me.eu.auth0.com/.well-known/openid-configuration.

You can use the following users to log into Auth0:

| User / Password                         | Role(s)     |
|-----------------------------------------|-------------|
| bruce.wayne@example.com / bruce_4demo!  | USER        |
| peter.parker@example.com / peter_4demo! | USER, ADMIN |
| clark.kent@example.com / clark_4demo!   | USER        |

