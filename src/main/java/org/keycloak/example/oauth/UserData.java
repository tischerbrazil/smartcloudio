package org.keycloak.example.oauth;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.example.kickoff.config.auth.KickoffCallerPrincipal;
import org.example.kickoff.view.ActiveUser;
import org.example.kickoff.model.Person;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

/**
 * @author <a href="mailto:tischerbr@gmail.com">Laercio Tischer</a>
 */

@SessionScoped
@Named("userData2")
public class UserData implements Serializable {

	@Inject
	private HttpServletRequest request;

	private String tenant;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	private Person systemUser;

	public Person getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(Person systemUser) {
		this.systemUser = systemUser;
	}

	public String getRealmAttribute(String name) {
		return null;
	}

	public String getAppImg(String name) {
		return null;
	}

	public String getUserImg(String name) {
		return null;
	}

	private static final long serialVersionUID = 1L;
	private String locale = "pt_BR";

	public KickoffCallerPrincipal getKickoffCallerPrincipal() {
		Principal userPrincipal = request.getUserPrincipal();
		if (this.userPrincipal() instanceof KickoffCallerPrincipal) {
			return (KickoffCallerPrincipal) userPrincipal;
		} else {
			return null;
		}
	}

	public Principal userPrincipal() {
		return request.getUserPrincipal();
	}

	public Person getPerson() {
		if (activeUser.isPresent()) {
			return activeUser.get();
		}
		return null;
	}
	
	public String getPersonUUID() {
		if (activeUser.isPresent()) {
			return activeUser.get().getUuid().toString();
		}
		return null;
	}
	
	public Long getPersonId() {
		if (activeUser.isPresent()) {
			return activeUser.get().getId();
		}
		return null;
	}

	@Inject
	private ActiveUser activeUser;

	public String getUserEntity() {
		if (activeUser.isPresent()) {
			return activeUser.get().getEmail();
		}
		return null;
	}

	public String getUserName() {
		return activeUser.get().getEmail();
	}

	public String getEmail() {
		return activeUser.get().getEmail();
	}

	private List<String> products;
	private List<String> customers;

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public boolean isHasProducts() {
		return products != null;
	}

	public List<String> getCustomers() {
		return customers;
	}

	public void setCustomers(List<String> customers) {
		this.customers = customers;
	}

	public boolean isHasCustomers() {
		return customers != null;
	}

	public String accountLinkUrl() {
		return "error787";
	}

	public String logoutLinkUrl() {
		return "error787";
	}

	public String loginLinkUrl() {
		return "error787";
	}

	public String loginLinkUrl2() {
		return "error787";
	}

	public static final String REGISTER_PATH = "/realms/{realm-name}/protocol/openid-connect/registrations";

	public static final String LOGIN_PATH = "/realms/{realm-name}/protocol/openid-connect/auth";

	public String getWorkPublicPath() {
		return this.getGeoazulURL() + "/index";
	}

	public String getWorkApplicationPath() {
		return this.getGeoazulURL() + "/application/";
	}

	public String getWorkAdministratorPath() {
		return this.getGeoazulURL() + "/administrator/";
	}

	public String getBaseHostName() {
		return getSchemeUrl() + request.getServerName();
	}

	public String getRestwork() {
		return getBaseHostName();
	}

	public String getGeoserverURL() {
		return this.getRestwork() + "/geoserver";
	}

	public String getBaseHostNamePath() {
		return this.getBaseHostName() + request.getContextPath();
	}


	public String getGeoazulURL() {
		return getBaseHostNamePath();
	}

	public String getSchemeUrl() {
		if (request.getServerName().equals("localhost") || request.getServerName().equals("127.0.0.1" )  ) {
			return request.getScheme() + "://";
		}else {
			return "https://";
		}
	}

	public String getGeoazulURLFiles() {
		return this.getGeoazulURL() + "/files/" + this.getRealmEntity();
	}

	public String getKeyCloakURL() {
		return this.getRestwork() + "/auth";
	}

	public String getRealmEntity() {
		return request.getServerName();
	}

	public boolean isUser() {
		try {
			return this.request.isUserInRole("user");
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAdministrator() {
		try {
			return this.request.isUserInRole("administrator");
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isSecurity() {
		try {
			return this.request.isUserInRole("security");
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAdmin() {
		try {
			return this.request.isUserInRole("admin");

		} catch (Exception e) {
			return false;
		}
	}

	// -------------------------------------

	// -----------------------------------------------------------------------
	public String getServerName() {

		try {
			return request.getServerName();
		} catch (Exception e1) {
			return null;
		}

	}

	public String getRealm() {
		try {
			return request.getServerName().replace("-", "_").replace(".", "_");
		} catch (Exception e1) {
			return null;
		}
	}

	private String realmInfoUrl;
	private String tokenUrl;

	private String accountUrl;
	private String registerNodeUrl;
	private String unregisterNodeUrl;
	private String jwksUrl;

	public String logoutLinkUr2l() {
		return "error787";
	}

	public String getRealmInfoUrl() {
		return realmInfoUrl;
	}

	public void setRealmInfoUrl(String realmInfoUrl) {
		this.realmInfoUrl = realmInfoUrl;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getAccountUrl() {
		return accountUrl;
	}

	public void setAccountUrl(String accountUrl) {
		this.accountUrl = accountUrl;
	}

	public String getRegisterNodeUrl() {
		return registerNodeUrl;
	}

	public void setRegisterNodeUrl(String registerNodeUrl) {
		this.registerNodeUrl = registerNodeUrl;
	}

	public String getUnregisterNodeUrl() {
		return unregisterNodeUrl;
	}

	public void setUnregisterNodeUrl(String unregisterNodeUrl) {
		this.unregisterNodeUrl = unregisterNodeUrl;
	}

	public String getJwksUrl() {
		return jwksUrl;
	}

	public void setJwksUrl(String jwksUrl) {
		this.jwksUrl = jwksUrl;
	}

	public void getHeader() {







	}

}