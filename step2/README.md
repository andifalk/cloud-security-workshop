# cloud-security-workshop
Workshop on building secure cloud-native applications using spring cloud security (OAuth2)

This workshop is the hands-on part of the [cloud security presentation](https://andifalk.github.io/security-cloud-presentation/index.html).

## OAuth2 Security Workshop Reference (Step 2)

You can find the completed applications for step 2 of the workshop here:

* product: Spring boot application providing a rest api for products: http://localhost:8080/products
* ui: A Spring boot application providing thymeleaf based html frontend to display products
* authorizationserver: A Spring boot based OAuth2 authorization server

In this step user credentials are stored in a relational database with encrypting the passwords.
Here the authorization server uses form based authentication as login type. 

**Standard user:**  
Username: _user@example.com_   
Password: _secret_

**Administrative user:**  
Username: _admin@example.com_  
Password: _geheim_

