package org.example.kickoff.view.user;

import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addGlobalInfo;
import static org.omnifaces.util.Messages.createError;

import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;

import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.example.kickoff.model.validator.Password;
import org.example.kickoff.view.ActiveUser;

@Named
@RequestScoped
public class ProfileBacking {

	private Person person;
	
	private @NotNull @Password String currentPassword;
	
	private @NotNull @Password String newPassword;

	@Inject
	private ActiveUser activeUser;

	@Inject
	private PersonService personService;

	@PostConstruct
	public void init() {
		
		person = activeUser.get();
	}

	public void save() {
		
		if (personService.update(person) != null) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("Sucesso!", "Usuário Alterado!"));
			
			redirect("/application");
			
			
		};
		
		
		
	}

	public void changePassword() {
		
		
		personService.updatePassword(person, newPassword);
		
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String messageBundleName = facesContext.getApplication().getMessageBundle();
		Locale locale = facesContext.getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
		String message = bundle.getString("user_profile.message.info.password_changed");
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		facesContext.addMessage(null, new FacesMessage("Sucesso!", message));
		redirect("/application");
	}

	public Person getPerson() {
		return person;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}