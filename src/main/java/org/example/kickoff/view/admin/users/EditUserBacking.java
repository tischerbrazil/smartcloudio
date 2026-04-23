package org.example.kickoff.view.admin.users;

import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addGlobalInfo;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;

@Named
@ViewScoped
public class EditUserBacking implements Serializable {


	private Person person;
	
	@Inject @Param(pathIndex=0)
	private Long id;
	
	@PostConstruct
	public void init() {
	
			person = personService.findById(this.id).get();
		
	}
	
	
	

	@Inject
	private PersonService personService;

	@Transactional
	public void save() throws IOException {
	
		if (personService.update(person) != null) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("SUCESSO!", "USUÁRIO ALTERADO!"));
			
			redirect("/security/users");
			
				
		};
		
		
		
	}
	
	public void create() throws IOException {
		
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