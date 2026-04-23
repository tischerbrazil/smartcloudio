package org.geoazul.locale.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.example.kickoff.view.ActiveLocale;
import org.geoazul.erp.Countries;
import org.hibernate.annotations.ColumnDefault;
import org.example.kickoff.model.Person;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.PrimeApplicationContext;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import modules.test.UserActive;
import jakarta.faces.context.FacesContext;

@ViewScoped
@Named
public class SessionTest implements Serializable {

	private static final long serialVersionUID = 1L;

    @ColumnDefault("true")
	private Boolean hasAccess;

	public Boolean getHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(Boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	private String theme = "saga";
	private boolean darkMode = false;
	private String inputStyle = "outlined";

	@Inject
	private EntityManager entityManager;

	private Person userAccount;

	private List<ActiveLocale> others;

	public List<ActiveLocale> getOthers() {
		return others;
	}

	public void setOthers(List<ActiveLocale> others) {
		this.others = others;
	}

	@Produces
	@UserActive
	public Person getUserActive() {
		return userAccount;
	}

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		try {
			TypedQuery<Person> queryUser = entityManager.createNamedQuery(Person.USER_UUID_GET, Person.class);
			queryUser.setParameter("uuid", UUID.fromString(request.getUserPrincipal().getName()));
			List<Person> usersListRet = queryUser.getResultList();
			if (usersListRet.size() > 0) {
				userAccount = usersListRet.get(0);
			} else {
				userAccount = null;
			}
		} catch (Exception e) {
			userAccount = null;
		}

		try {
			List<Countries> dd1 = Lists.newArrayList(Iterables.filter(
					entityManager.createQuery("Select l from Countries l where l.enabled = true").getResultList(),
					Countries.class));
			List<ActiveLocale> others3 = new ArrayList<ActiveLocale>();
			for (Countries languag : dd1) {
				Locale localeFromTag = Locale.forLanguageTag(languag.getLang().replace("_", "-"));
				others3.add(new ActiveLocale(localeFromTag));
			}
			others = others3;
			hasAccess = true;
		} catch (Exception e2) {
			e2.printStackTrace();
			hasAccess = false;
		}
	}

	public Person getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(Person userAccount) {
		this.userAccount = userAccount;
	}

	public String getTheme() {
		return theme;
	}

	public boolean isDarkMode() {
		return darkMode;
	}

	public void setDarkMode(boolean darkMode) {
		this.darkMode = darkMode;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getInputStyle() {
		return inputStyle;
	}

	public void setInputStyle(String inputStyle) {
		this.inputStyle = inputStyle;
	}

	public String getInputStyleClass() {
		return "filled".equals(this.inputStyle) ? "ui-input-filled" : "";
	}

	public void changeTheme(String theme, boolean darkMode) {
		this.theme = theme;
		this.darkMode = darkMode;
	}

	public String getPrimeFacesVersion() {
		return PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance()).getEnvironment()
				.getBuildVersion();
	}

}