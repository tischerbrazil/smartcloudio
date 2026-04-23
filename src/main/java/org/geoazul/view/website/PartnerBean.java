package org.geoazul.view.website;

import java.io.Serializable;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.example.kickoff.model.Person;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.refact.PartnerJpaLazyDataModel;

@Named
@ViewScoped
public class PartnerBean implements Serializable {

	
	private LazyDataModel<Person> model;
	
	@Inject
	EntityManager entityManager;

	public PartnerBean() {
		model = new PartnerJpaLazyDataModel<>(Person.class, () -> entityManager);
	}

	public LazyDataModel<Person> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Person> model) {
		this.model = model;
	}

	

}