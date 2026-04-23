package org.example.kickoff.business.service;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.example.kickoff.model.Group.USER;
import static org.omnifaces.util.Faces.getRemoteAddr;
import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addFlashGlobalWarn;
import static org.omnifaces.utils.security.MessageDigests.digest;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.example.kickoff.business.email.EmailService;
import org.example.kickoff.business.email.EmailTemplate;
import org.example.kickoff.business.email.EmailUser;
import org.example.kickoff.business.exception.DuplicateEntityException;
import org.example.kickoff.business.exception.InvalidPasswordException;
import org.example.kickoff.business.exception.InvalidTokenException;
import org.example.kickoff.business.exception.InvalidUsernameException;
import org.example.kickoff.model.Credentials;
import org.example.kickoff.model.Group;
import org.example.kickoff.model.LoginToken.TokenType;
import org.tenant.hibernate.deltaspyke.jpa.Tenant;
import org.tenant.hibernate.deltaspyke.jpa.TenantInject;
import org.example.kickoff.model.Person;

@Stateless
public class PersonService  {

	private static final long DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES = TimeUnit.HOURS.toMinutes(1);

	@Resource
	private SessionContext sessionContext;

	@Inject
	private LoginTokenService loginTokenService;
	
	
	@Inject
	private EntityManager entityManager;
	
	  
	

	@Inject
	private EmailService emailService;
	
	    @Inject
	    @TenantInject
	    private Tenant tenant;
	
	public Optional<Person> findById(Long id) {
		TypedQuery<Person> query = entityManager.createNamedQuery(Person.USER_ID_GET, Person.class);
		query.setParameter("id", id);
		return  getOptionalSingleResult(query);
	}
	
	public Optional<Person> findByEmail(String email) {
		TypedQuery<Person> query = entityManager.createNamedQuery(Person.USER_EMAIL_GET, Person.class);
		query.setParameter("email", email);
		return  getOptionalSingleResult(query);
	}

	@Transactional
	public void register(Person person, String password, String callbackUrlFormat , Group... additionalGroups) {
		if (findByEmail(person.getEmail()).isPresent()) {
			throw new DuplicateEntityException();
		}
		person.getGroups().add(USER);
		person.getGroups().addAll(asList(additionalGroups));
		persist(person);
		  entityManager.flush();
	    setPassword(person, password);
	    
	   entityManager.flush();
	    
	    
	 
		ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES);
		String email = person.getEmail();
		String ipAddress = getRemoteAddr();
		String token = loginTokenService.generate(email, ipAddress, "Confirm Email", TokenType.MAIL_VALIDATOR, expiration.toInstant());

		
		
		String stringFormatada  = String.format(callbackUrlFormat, token);
		
		EmailTemplate emailVerifyTemplate = new EmailTemplate("newEmailVerified")
			.setToUser(new EmailUser(person))
			.setCallToActionURL(String.format(callbackUrlFormat, token));

		Map<String, Object> messageParameters = new HashMap<>();
		messageParameters.put("expiration", expiration);
		messageParameters.put("ip", ipAddress);
	    
	    	

		String messageRetorno = null;
		try {
		
			 messageRetorno = emailService.sendTemplate(emailVerifyTemplate, messageParameters);
		
			}catch(Exception ex) {

				addFlashGlobalWarn(messageRetorno);
				redirect("/signup");
				
				
			}
	    
	    
		//redirect("/index");
	    
	}
	
	@Transactional
	protected Person manage(Person entity) {
		if (entity == null) {
			throw new NullPointerException("Entity is null.");
				}
		
		
		Long id = entity == null ? null : entity.getId();

		if (id == null) {
			  return entity;
		}
		
		
		if (entityManager.contains(entity)) {
			return entity;
		}

		Person managed = findById(id).get();
		//if (managed == null) {
		//	throw new EntityNotFoundException("Entity has in meanwhile been deleted.");
		//}
		return managed;
	}
	
	
	protected <T> TypedQuery<T> createTypedQuery(String jpql, Class<T> resultType) {
		return entityManager.createQuery(jpql, resultType);
	}
	
	/**
	 * Create an instance of {@link TypedQuery} for executing the given Java Persistence Query Language statement which
	 * returns a <code>Long</code>, usually a SELECT e.id or SELECT COUNT(e).
	 * @param jpql The Java Persistence Query Language statement.
	 * @return An instance of {@link TypedQuery} for executing the given Java Persistence Query Language statement which
	 * returns a <code>Long</code>, usually a SELECT e.id or SELECT COUNT(e).
	 */
	protected TypedQuery<Long> createLongQuery(String jpql) {
		return createTypedQuery(jpql, Long.class);
	}
	
	/**
	 * Check whether given entity exists.
	 * This method supports proxied entities.
	 * @param entity Entity to check.
	 * @return Whether entity with given entity exists.
	 */
	protected boolean exists(Person entity) {
		Long id = entity.getId();
		return id != null && createLongQuery("SELECT COUNT(e) FROM Person e WHERE e.id = :id")
			.setParameter("id", id)
			.getSingleResult().intValue() > 0;
	}
	
	@Transactional
	public Person update_(Person entity) {
		if (entity.getId() == null) {
			throw new NullPointerException("Entity id is null.");
		}

		if (!exists(entity)) {
			throw new NullPointerException("Entity is not persisted. Use persist() instead.");
		}
		
		//Person hhh = entityManager.merge(entity);
		//entityManager.flush();
		
		
		
		return entityManager.merge(entity);
	}

	@Transactional
	public Long persist(Person entity) {
		if (entity.getId() != null) {
			if (exists(entity)) {
				//FIXME
				//throw new IllegalEntityStateException(entity, "Entity is already persisted. Use update() instead.");
			}
		}
		

		try {
				entityManager.persist(entity);

		}
		catch (ConstraintViolationException e) {
		
			throw e;
		}

		// Entity is not guaranteed to have been given an ID before either the TX commits or flush is called.
		entityManager.flush();

		return entity.getId();
	}
	
	@Transactional
	public Person update(Person person) {
		Person existingPerson = manage(person);
		if (!person.getEmail().equals(existingPerson.getEmail())) { 
			Optional<Person> otherPerson = findByEmail(person.getEmail());
									
			if (otherPerson.isPresent()) {
					
				if (!person.equals(otherPerson.get())) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO!", "EMAIL JÁ CADASTRADO!"));
					return null;
				}
				else {
					person.setEmailVerified(otherPerson.get().isEmailVerified());
				}
			}
			else {
				person.setEmailVerified(false);
			}
		}
		return update_(person);
	}

	public void updatePassword(Person person, String password) {
		Person existingPerson = manage(person);
		setPassword(existingPerson, password);
		update(existingPerson);
	}
	
	@Transactional
	public void updateMailVerified(Person person) {
	Query query = entityManager.createNamedQuery(Person.UPDATE_MAIL_VERIFIED);
	query.setParameter("person", person);
	query.executeUpdate();
	}

	@Transactional
	public void updatePassword(String loginToken, String password) {
		Optional<Person> person = findByLoginToken(loginToken, TokenType.RESET_PASSWORD);

		if (person.isPresent()) {
			updatePassword(person.get(), password);
			loginTokenService.remove(loginToken);
		}
	}

	@Transactional
	public void requestResetPassword(String email, String ipAddress, String callbackUrlFormat) {
		
		
		
		Person person = findByEmail(email).orElseThrow(InvalidUsernameException::new);
		ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES);
		String token = loginTokenService.generate(email, ipAddress, "Reset Password", TokenType.RESET_PASSWORD, expiration.toInstant());

		
		
		String stringFormatada  = String.format(callbackUrlFormat, token);
		
		EmailTemplate emailTemplate = new EmailTemplate("resetPassword")
			.setToUser(new EmailUser(person))
			.setCallToActionURL(String.format(callbackUrlFormat, token));

		Map<String, Object> messageParameters = new HashMap<>();
		messageParameters.put("expiration", expiration);
		messageParameters.put("ip", ipAddress);

				
		try {
			emailService.sendTemplate(emailTemplate, messageParameters);
			}catch(Exception ex) {
			}
		
	}
	
	protected TypedQuery<Person> createNamedTypedQuery(String name) {
		return entityManager.createNamedQuery(name, Person.class);
	}

	
	
	/**
	 * Returns single result of given typed query, or <code>null</code> if there is none.
	 * @param <T> The generic result type.
	 * @param typedQuery The involved typed query.
	 * @return Single result of given typed query, or <code>null</code> if there is none.
	 * @throws NonUniqueResultException When there is no unique result.
	 */
	public static <T> T getSingleResultOrNull(TypedQuery<T> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	public static <Person> Optional<Person> getOptionalSingleResult(TypedQuery<Person> typedQuery) {
		return ofNullable(getSingleResultOrNull(typedQuery));
	}
	
	public Optional<Person> findByLoginToken(String loginToken, TokenType type) {
		TypedQuery<Person> query = entityManager.createNamedQuery(Person.GET_BY_LOGIN_TOKEN, Person.class);
		query.setParameter("tokenHash", digest(loginToken, "SHA-256"));
		query.setParameter("tokenType", type);
		return  getOptionalSingleResult(query);
	}

	public Person getByEmail(String email) {
		return findByEmail(email).orElseThrow(InvalidUsernameException::new);
	}

	public Person getByEmailAndPassword(String email, String password) {
	    Person person = getByEmail(email);

	    if (!person.getCredentials().isValid(password)) {
	        throw new InvalidPasswordException();
	    }

	    return person;
	}

	public Person getByLoginToken(String loginToken, TokenType type) {
		return findByLoginToken(loginToken, type).orElseThrow(InvalidTokenException::new);
	}

	public Person getActivePerson() {
		return findByEmail(sessionContext.getCallerPrincipal().getName()).orElse(null);
	}

	@Transactional
	public void setPassword(Person person, String password) {
		Person managedPerson = manage(person);
		Credentials credentials = person.getCredentials();

		if (credentials == null) {
			credentials = new Credentials();
			credentials.setPerson(managedPerson);
		}

		credentials.setPassword(password);
		
		entityManager.persist(credentials);
		entityManager.flush();
		
	}
	
	
	
	
	
	

}