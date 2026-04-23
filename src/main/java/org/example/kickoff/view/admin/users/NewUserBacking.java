package org.example.kickoff.view.admin.users;

import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addGlobalInfo;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolationException;

import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;


@Named
@RequestScoped
public class NewUserBacking {

	@Inject 
	private Person person;
	
	@PostConstruct
	public void init() {
		person = new Person();
	}

	@Inject
	private PersonService personService;

	
	public void create() {
		if (personService.persist(person) != null) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("SUCESSO!", "USUÁRIO ALTERADO!"));
			
			redirect("/security/users");
			
			
		};
		
		
	}

	public Person getPerson() {
		return person;
	}

}