package org.example.kickoff.view.auth;

import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static org.example.kickoff.model.Group.ADMIN;
import static org.example.kickoff.model.Group.USER;
import static org.omnifaces.util.Faces.getRequest;
import static org.omnifaces.util.Faces.getResponse;
import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Faces.responseComplete;
import static org.omnifaces.util.Faces.validationFailed;
import static org.omnifaces.util.Messages.addFlashGlobalWarn;
import static org.omnifaces.util.Messages.addGlobalError;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.validation.constraints.NotNull;

import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.example.kickoff.model.validator.Password;
import org.example.kickoff.view.ActiveUser;

public abstract class AuthBacking {

	protected Person person;
	protected @NotNull @Password String password;
	protected boolean rememberMe;

	@Inject
	protected PersonService personService;

	@Inject
	private SecurityContext securityContext;

	@Inject
	private ActiveUser activeUser;

	@PostConstruct
	public void init() {
		if (activeUser.isPresent()) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String messageBundleName = facesContext.getApplication().getMessageBundle();
			Locale locale = facesContext.getViewRoot().getLocale();
			ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
			String message = bundle.getString("auth.message.warn.already_logged_in");
			
			addFlashGlobalWarn(message);
			redirect("/user/profile");
		}
		else {
			person = new Person();
		}
	}
	
	public void auth() {
	}

	protected void authenticate(AuthenticationParameters parameters) throws IOException {
		AuthenticationStatus status = securityContext.authenticate(getRequest(), getResponse(), parameters);

		if (status == SEND_FAILURE) {
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String messageBundleName = facesContext.getApplication().getMessageBundle();
			Locale locale = facesContext.getViewRoot().getLocale();
			ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
			String message = bundle.getString("auth.message.error.failure");

			addGlobalError(message);
			validationFailed();
		}
		else if (status == SEND_CONTINUE) {
			responseComplete(); // Prevent JSF from rendering a response so authentication mechanism can continue.
        }
		else if (activeUser.hasGroup(ADMIN)) {
			redirect("/index");
		}
		else if (activeUser.hasGroup(USER)) {
			
			addFlashGlobalWarn("Bem-vindo " + activeUser.get().getFirstName());
			
			redirect("/index");
		}
		else {
			redirect("/index");
		}
	}

	public Person getPerson() {
		return person;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}