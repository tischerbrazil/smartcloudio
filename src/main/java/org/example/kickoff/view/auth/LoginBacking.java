package org.example.kickoff.view.auth;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import static org.omnifaces.util.Faces.redirect;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;


import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;

@Named
@RequestScoped
public class LoginBacking extends AuthBacking implements Serializable{



	@Inject @Param(name = "continue") // Defined in @LoginToContinue of KickoffFormAuthenticationMechanism.
	private boolean loginToContinue;

	public void login() throws IOException {
		authenticate(withParams()
			.credential(new UsernamePasswordCredential(person.getEmail(), password))
			.newAuthentication(!loginToContinue)
			.rememberMe(rememberMe));
	}
	

}