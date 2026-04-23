package org.omnifaces.optimusfaces.test.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GeneratedIdEntity<I> extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Id @Tsid
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
