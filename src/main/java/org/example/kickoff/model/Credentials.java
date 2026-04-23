package org.example.kickoff.model;

import static org.omnifaces.utils.security.MessageDigests.digest;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import atest.GeneratedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SEC_CREDENTIALS")
public class Credentials extends GeneratedIdEntity {

	private static final long serialVersionUID = 1L;

	private static final int HASH_LENGTH = 32;
	private static final int SALT_LENGTH = 40;

	@ManyToOne(optional = false)
	private @NotNull Person person;

	@Column(name="password_hash", length = HASH_LENGTH, nullable = false)
	private @NotNull byte[] passwordHash;

	@Column(length = SALT_LENGTH, nullable = false)
	private @NotNull byte[] salt = new byte[SALT_LENGTH];

	public void setPerson(Person person) {
		person.setCredentials(this);
		this.person = person;
	}

	public void setPassword(String password) {
		ThreadLocalRandom.current().nextBytes(salt);
		passwordHash = hash(password);
	}

	public boolean isValid(String password) {
		return Arrays.equals(passwordHash, hash(password));
	}

	private byte[] hash(String password) {
		return digest(password, salt, "SHA-256");
	}

}