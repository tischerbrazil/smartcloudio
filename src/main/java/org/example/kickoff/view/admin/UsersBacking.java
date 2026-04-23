package org.example.kickoff.view.admin;

import static org.omnifaces.util.Messages.addGlobalWarn;
import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.refact.PartnerJpaLazyDataModel;


@Named
@ViewScoped
public class UsersBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	private LazyDataModel<Person> model;
	
	@Inject
	private EntityManager entityManager;

	@Inject
	private PersonService personService;

	@PostConstruct
	public void init() {
		//model = PagedDataModel.lazy(personService).build();
		
		
		model = new PartnerJpaLazyDataModel<>(Person.class, () -> entityManager);
		 
		 
	}

	public void delete(Person person) {
		// personService.delete(person);
		addGlobalWarn("This is just a demo, we won't actually delete users for now.");
	}

	public LazyDataModel<Person> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Person> model) {
		this.model = model;
	}


}