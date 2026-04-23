package org.example.kickoff.config.auth;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.authentication.mechanism.http.RememberMe;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Authentication mechanism that authenticates according to the Servlet spec defined FORM authentication mechanism.
 * See Servlet spec for further details.
 *
 * @author Arjan Tijms
 */
@AutoApplySession // For "Is user already logged-in?"
@RememberMe(
		cookieSecureOnly = false, // Remove this when login is served over HTTPS.
		cookieMaxAgeSeconds = 60 * 60 * 24 * 14) // 14 days.
@LoginToContinue(
		loginPage = "/login?continue=true",
		errorPage = "",
		useForwardToLogin = false)
@ApplicationScoped
public class KickoffFormAuthenticationMechanism implements HttpAuthenticationMechanism {

    //static final String USERNAME_HEADER = "X-USERNAME";
    //static final String PASSWORD_HEADER = "X-PASSWORD";
    
    @Inject
    private IdentityStoreHandler identityStoreHandler;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
		Credential credential = context.getAuthParameters().getCredential();
		
		   //final String username = request.getHeader(USERNAME_HEADER);
	       //final String password = request.getHeader(PASSWORD_HEADER);
	        
	       
		if (credential != null) {
            return context.notifyContainerAboutLogin(identityStoreHandler.validate(credential));
       // }else if (username != null){
        //	  UsernamePasswordCredential ii = new  UsernamePasswordCredential(username, password);
       // 	  return context.notifyContainerAboutLogin(identityStoreHandler.validate(ii));
        }

        return context.doNothing();
	}

}