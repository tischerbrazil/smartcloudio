package org.example.kickoff.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.example.kickoff.model.validator.Email;

import atest.TimestampedEntity;


@Entity
@Table(name = "ERP_NEWS_LETTER", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }),
		 })
@NamedQueries({
	    @NamedQuery(name = "Newsletter.findAll", query = "SELECT p FROM Newsletter p"),
		@NamedQuery(name = "Newsletter.findById", query = "SELECT p FROM Newsletter p WHERE p.id = :id"),
		@NamedQuery(name = "Newsletter.findByEmail", query = "SELECT p FROM Newsletter p WHERE p.email = :email"),
		 })
public class Newsletter extends TimestampedEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_ID = "getUserById";
	public static final String FIND_BY_EMAIL = "Newsletter.findByEmail";
	public static final String FIND_ALL = "Newsletter.findAll";



	public static final int EMAIL_MAXLENGTH = 254;
	public static final int NAME_MAXLENGTH = 32;

	@Column(length = EMAIL_MAXLENGTH, nullable = false, unique = true)
	private @NotNull @Size(max = EMAIL_MAXLENGTH) @Email String email;

	@Column(length = NAME_MAXLENGTH, nullable = false)
	private @NotNull @Size(max = NAME_MAXLENGTH) String firstName;

	@Column(length = NAME_MAXLENGTH, nullable = false)
	private @NotNull @Size(max = NAME_MAXLENGTH) String lastName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public Object getSample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}