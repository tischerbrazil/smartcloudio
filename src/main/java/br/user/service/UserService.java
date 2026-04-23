package br.user.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.example.kickoff.view.ActiveLocale;
import org.keycloak.example.oauth.UserData;

@RequestScoped
public class UserService {

	@Inject
	private EntityManager entityManager;

	@Inject
	private ActiveLocale activeLocale;
	
	@Inject
	private UserData userData;
	
	

	

}
