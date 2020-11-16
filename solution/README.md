# cloud-security-workshop
Workshop on building secure cloud-native applications using spring cloud security (OAuth2)

This workshop is the hands-on part of the [cloud security presentation](https://andifalk.github.io/security-cloud-presentation/index.html).

## OAuth2 Security Workshop Reference (Step 1)

You can find the completed applications for step 1 of the workshop here:

* product: Spring boot application providing a rest api for products: http://localhost:8080/products
* ui: A Spring boot application providing thymeleaf based html frontend to display products

In this step user credentials are configured using the _application.properties_ file.
Here the authorization server uses basic authentication as login type. 

Username: _user_  
Password: _secret_

