package org.example.kickoff.business.service;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.UUID.randomUUID;
import static org.omnifaces.utils.security.MessageDigests.digest;
import java.time.Instant;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.kickoff.business.exception.InvalidUsernameException;
import org.example.kickoff.model.LoginToken;
import org.example.kickoff.model.LoginToken.TokenType;
import org.example.kickoff.model.Person;

@Stateless
public class LoginTokenService {

	private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";

	@Inject
	private PersonService personService;
	
	@Inject
	private EntityManager entityManager;

	@Transactional
	public String generate(String email, String ipAddress, String description, TokenType tokenType) {
		Instant expiration = now().plus(30, DAYS);
		return generate(email, ipAddress, description, tokenType, expiration);
	}

	@Transactional
	public String generate(String email, String ipAddress, String description, TokenType tokenType, Instant expiration) {
		String rawToken = randomUUID().toString();
		Person person = personService.findByEmail(email).orElseThrow(InvalidUsernameException::new);
		LoginToken loginToken = new LoginToken();
		loginToken.setTokenHash(digest(rawToken, MESSAGE_DIGEST_ALGORITHM));
		loginToken.setExpiration(expiration);
		loginToken.setDescription(description);
		loginToken.setType(tokenType);
		loginToken.setIpAddress(ipAddress);
		loginToken.setPerson(person);
		person.getLoginTokens().add(loginToken);
		return rawToken;
	}

	@Transactional
	public void remove(String loginToken) {
		Query query = entityManager.createNamedQuery("LoginToken.remove");
		query.setParameter("tokenHash", digest(loginToken, MESSAGE_DIGEST_ALGORITHM));
		query.executeUpdate();
	}

	@Transactional
	public void removeExpired() {
		Query query = entityManager.createNamedQuery("LoginToken.removeExpired");
		query.executeUpdate();
	}

}