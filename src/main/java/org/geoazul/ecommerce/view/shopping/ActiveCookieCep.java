package org.geoazul.ecommerce.view.shopping;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class ActiveCookieCep {
	
	public static final String COOKIE_NAME = "postal_code";
	public static final int COOKIE_MAX_AGE_IN_DAYS = 30;
	private String path;
	private boolean explicitlyRequested;
	private boolean changed;
	private String uri;
	private ActiveCookieCep current;
	
	@Inject
	private HttpServletRequest request;

	public ActiveCookieCep() {
		// Keep default c'tor alive for CDI.
	}

	public ActiveCookieCep(String postalCode) {
		path = "/" + postalCode;
		current = this;
	}

	
	public void init(String postalCode) {
			current = new ActiveCookieCep(postalCode);
	}

	public String getPath() {
		return current.path;
	}

	public boolean isExplicitlyRequested() {
		return explicitlyRequested;
	}

	public boolean isChanged() {
		return changed;
	}

	public String getUri() {
		return uri;
	}



}
