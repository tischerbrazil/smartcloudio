package org.geoazul.model.app;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("/rural/") 
public class ProjectRuralEntity extends ApplicationEntity {
 	 
	
	public ProjectRuralEntity() 
	{
	}


   
}