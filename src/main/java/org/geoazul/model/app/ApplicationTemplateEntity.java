package org.geoazul.model.app;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;


/**
 * @author Laercio Tischer
 */

@Entity
@NamedQueries({
@NamedQuery(name = ApplicationTemplateEntity.DEFAULT_TEMPLATE, query = "SELECT a FROM ApplicationTemplateEntity a WHERE a.locale = :locale and a.clientEntity.clientId = :dtype order by a.defaultApp")
})
@DiscriminatorValue("/template/") 
public class ApplicationTemplateEntity extends ApplicationIdentityEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_TEMPLATE = "ApplicationTemplateEntity.defaultAPP";

	public ApplicationTemplateEntity() {
	}
	




}