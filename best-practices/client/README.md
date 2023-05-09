# Best Practices for the Client side

This section describes important security recommendations and best-practices for the client side.

## Main Risks on the client side

* Usage of an unsafe OAuth grant flow like the _implicit grant_, the _authorization code grant_ without _PKCE_ or the _resource owner password grant_ (i.e. Redirect URI attacks).
* Leakage of tokens to hackers who might reuse these on different applications (i.e. Token leakage and replay attacks)

## Client Types

The risks are different depending on the client type used. In general server side web clients have much less risk for attacks than clients running inside the user's browser.

Therefore, we divide the recommendations and best-practices for the client type:

* Web Clients running on the server (i.e. Spring MVC clients or ASP.NET clients)
* Web Clients running on the client side inside the user's web browser (Single Page Applications)

### Server-side Clients

Web clients running on the server side are called __confidential__ clients in OAuth terms as these client type can keep secrets in a safe place on the server. Unless your server network is being hacked secrets like database credentials or the client secret for OAuth are quite secure from being leaked into the public.  
The same is true for tokens. As tokens are stored on the server side it is much more difficult for an attacker to steal tokens.

On the server side it is possible to use the OAuth _authorization code grant_ flow with or without _PKCE_.  
Just to remember: If you want to use the _authorization code grant_ flow without _PKCE_ you need to specify the _client_id_ and the _client_secret_ when exchanging the authorization code into an access token. On the server side the _client_secret_ is safe and may be used.
As attacks are still possible (but with higher effort, e.g. by installing a malicious browser addon) the OAuth working group at the IETF also recommends using _authorization code grant_ flow with _PKCE_ on the server side.

### Single Page Applications

When using single page applications (SPA) like for example clients implemented using Angular, React or Vue.js, this exposes a much higher risk.
SPA is at risk for Cross-Site Scripting (XSS) attacks. We distinguish different kind of XSS attacks:

* Reflected XSS: Here the malicious input inside the request just gets reflected by the response and is rendered with all malicious code in the browser.
* Persistent XSS: Almost the same as the reflected one but additionally the malicious input is stored in the database and gets reflected every time this malicious data is loaded from the database.
* DOM-based XSS: This attack only happens on the client side without performing any request to the server side. So your monitoring or logging system won't notice such an attack as this happens completely in the user's web browser.

With __just one__ XSS vulnerability it may be possible to steal all tokens from the browser storage. 

You can reduce the risk of getting XSS by using a good framework/library like Angular, React or Vue.js.  
But even here there are differences in the grade of protection between those products:

* Angular is [very secure from getting XSS](https://angular.io/guide/security#preventing-cross-site-scripting-xss) as this framework automatically escapes html/javascript output and sanitizes all malicious javascript output from rendered html.
* React and Vue.js only support the escaping part but miss a sanitizer component. If you are using these libraries please add a sanitizer like [DOMPurify](https://github.com/cure53/DOMPurify)).

If you don't want to take the risk of getting tokens stolen due to a XSS problem then you might look for other solutions.
The OAuth working group at the IETF has published several possible mitigation patterns in the [OAuth 2.0 for Browser-Based Apps](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html):

* [Single-Domain Browser-Based Apps (not using OAuth)](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html#name-single-domain-browser-based)
* [Backend For Frontend (BFF) Proxy](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html#name-backend-for-frontend-bff-pr)
* [Acquiring tokens from a Service Worker](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html#name-acquiring-tokens-from-a-ser)

## Best Practices and Recommendations

1. If possible use server side web client implementations (unfortunately this recommendation contradicts modern business requirements. We all know that currently the trend is towards SPA as these clients feel much more like a real desktop app running inside the browser)
2. Independent of the client type used please always use the _authorization code grant_ flow with _PKCE_ (the only exception is the _client credentials grant_ for non-interactive applications like batch processing apps, but this can only bes used for server-side applications)
3. When using SPA clients then you either can 
   * try to minimize the risk of having a XSS vulnerability (by using Angular or by using React/Vue.js in combination with [DOMPurify](https://github.com/cure53/DOMPurify))
   * don't store tokens in the browser storage by using the Backend for Frontend (BFF) pattern or service workers
4. Don't use Redirect URIs with wildcards as these open up attacks like _subdomain takeover_ or _open redirects_.
5. Remember: The ID token is for the client side, the access token is for the server side (the resource server).Never send the ID token to the resource server or use the access token on the client side.
6. Always validate the ID token on the client side (at least the signature and expiry date/time)
7. Keep lifetime short for access tokens (especially when these are stored in the browser), 5-30 minutes are OK
8. When using refresh tokens make sure these are rotated after each use (a refresh token can only be used one time to exchange for an access token)
9. Always use a proven library for all that OAuth2 and OpenID Connect protocol stuff (you find a good library for almost every kind of programming language). Please check the [OpenID Connect certified implementations](https://openid.net/developers/certified/).

You can find even more recommendations and best-practices here at the [IETF](https://datatracker.ietf.org/wg/oauth/documents/):

* [OAuth 2.0 for Browser-Based Apps](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html)
* [OAuth 2.0 Security Best Current Practice](https://www.ietf.org/archive/id/draft-ietf-oauth-security-topics-22.html)
* [The OAuth 2.1 Authorization Framework](https://www.ietf.org/archive/id/draft-ietf-oauth-v2-1-08.html)
* [JSON Web Token Best Current Practices](https://www.rfc-editor.org/rfc/rfc8725.html)