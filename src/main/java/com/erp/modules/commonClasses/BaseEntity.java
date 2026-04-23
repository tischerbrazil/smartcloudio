package com.erp.modules.commonClasses;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends org.omnifaces.optimusfaces.test.model.BaseEntity<Long> {

    
	@Id @Tsid
	protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}