package org.example.kickoff.view.auth;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import static java.util.Optional.ofNullable;
import static org.example.kickoff.model.LoginToken.TokenType.MAIL_VALIDATOR;
import static org.example.kickoff.model.LoginToken.TokenType.RESET_PASSWORD;
import static org.omnifaces.util.Faces.getRequestURL;
import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addFlashGlobalWarn;
import static org.omnifaces.utils.security.MessageDigests.digest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Logger;
import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.omnifaces.cdi.Param;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.credential.CallerOnlyCredential;
import jakarta.transaction.Transactional;

@Named
@RequestScoped
public class SignupBacking extends AuthBacking implements Serializable {

	
	@Inject @Param
	private String token;

	@Inject
	private PersonService personService;
	
	@Inject
	private EntityManager entityManager;

	@Inject
	private Logger logger;
	
	@Override
	@PostConstruct
	@Transactional
	public void init() {
		super.init();
		if (token != null && personService.findByLoginToken(token, MAIL_VALIDATOR).isPresent()) {
			Person person = personService.findByLoginToken(token, MAIL_VALIDATOR).get();
			personService.updateMailVerified(person);
			redirect("login");
		}
	}
	
	public static <Person> Optional<Person> getOptionalSingleResult(TypedQuery<Person> typedQuery) {
		return ofNullable(getSingleResultOrNull(typedQuery));
	}
	
	public static <T> T getSingleResultOrNull(TypedQuery<T> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional
	public void signup() throws IOException {
		personService.register(person, password, getRequestURL() + "?token=%s");
		authenticate(withParams().credential(new CallerOnlyCredential(person.getEmail())));
	}

}