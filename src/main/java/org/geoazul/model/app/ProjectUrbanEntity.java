package org.geoazul.model.app;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("/urban/") 
public class ProjectUrbanEntity extends ApplicationEntity { 
 	 
	
	public ProjectUrbanEntity() 
	{
	}


   
}