package org.geoazul.model.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.example.kickoff.model.Person;
import org.hibernate.annotations.Nationalized;

import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
		@NamedQuery(name = "deleteUserAttributes", query = "delete from  UserAttributeEntity attr where attr.user IN (select u from Person u)"),
		@NamedQuery(name = "deleteUserAttributesByNameAndUser", query = "delete from  UserAttributeEntity attr where attr.user.id = :userId and attr.name = :name"),
		@NamedQuery(name = "deleteUserAttributesByNameAndUserOtherThan", query = "delete from  UserAttributeEntity attr where attr.user.id = :userId and attr.name = :name and attr.id <> :attrId"),
		@NamedQuery(name = "userAttibuteFind", query = "select attr from UserAttributeEntity attr where attr.user.id = :userid and attr.name = :name"),
		@NamedQuery(name = "userAttibuteFindTemp", query = "select attr from UserAttributeEntity attr where attr.user.id = :userid and attr.name = :name and attr.temp = false"),
		@NamedQuery(name = "userAttibuteUpdateName", query = "UPDATE UserAttributeEntity SET value = :value  where user.id = :userid and name = :name"),
		@NamedQuery(name = "userAttributeCount", query = "SELECT COUNT(attr) FROM UserAttributeEntity attr WHERE attr.user.id = :userid and attr.name = :name"),
		@NamedQuery(name = "userAttributeCountNTemp", query = "SELECT COUNT(attr) FROM UserAttributeEntity attr WHERE attr.user.id = :userid and attr.name = :name and attr.temp = false")

})
@Table(name = "SEC_USER_ATTRIBUTE")
@Entity
@JsonIgnoreProperties({"id","uuid","temp","user","mimeType"})

public class UserAttributeEntity extends BaseEntity {

	public UserAttributeEntity(Long id, boolean temp, Person user, String name, String value,
			MimeType mimeType) {
		super();
		this.id = id;
		this.temp = temp;
		this.user = user;
		this.name = name;
		this.value = value;
		this.mimeType = mimeType;
	}

	public UserAttributeEntity(UserAttributeEntity attributeEntity) {
		super();
		this.id = attributeEntity.getId();
		this.temp = attributeEntity.isTemp();
		this.user = attributeEntity.getUser();
		this.name = attributeEntity.getName();
		this.value = attributeEntity.getValue();
		this.mimeType = attributeEntity.getMimeType();
	}


	private static final long serialVersionUID = 1L;

	public static final String DELETE_ALL = "deleteUserAttributes";
	public static final String DELETE_NAME_USER = "deleteUserAttributesByNameAndUser";
	public static final String DELETE_NAME_USER2 = "deleteUserAttributesByNameAndUserOtherThan";
	public static final String UPDATE_ATTRIB_NAME = "userAttibuteUpdateName";
	public static final String FIND_ATTRIB = "userAttibuteFind";
	public static final String FIND_ATTRIB_TEMP = "userAttibuteFindTemp";
	public static final String FIND_COUNT = "userAttributeCount";
	public static final String FIND_COUNT_NTEMP = "userAttributeCountNTemp";

	public enum MimeType {
		text, application, image, audio, video
	}

	@Column(name = "temp")
	protected boolean temp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	protected Person user;

	@Column(name = "NAME")
	protected String name;

	@Nationalized
	@Column(name = "VALUE")
	protected String value;

	@Column(name = "mime_type")
	private MimeType mimeType;

	public UserAttributeEntity() {
		this.temp = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Person getUser() {
		return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public MimeType getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}

	

}
