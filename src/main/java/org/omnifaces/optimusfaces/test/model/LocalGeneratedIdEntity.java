package org.omnifaces.optimusfaces.test.model;


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import io.hypersistence.utils.hibernate.id.Tsid;

@MappedSuperclass
public class LocalGeneratedIdEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = -1515015185027970276L;
	
	@Id @Tsid
	protected Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}