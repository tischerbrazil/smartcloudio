package org.geoazul.ecommerce.view.shopping;

import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class ActiveCookieUuid {
	
	public static final String COOKIE_NAME = "cart_uuid";
	public static final int COOKIE_MAX_AGE_IN_DAYS = 30;
	private static final UUID GENERATE_UUID = UUID.randomUUID();
	private String path;
	private boolean explicitlyRequested;
	private boolean changed;
	private String uri;
	private ActiveCookieUuid current;
	
	@Inject
	private HttpServletRequest request;

	public ActiveCookieUuid() {
		// Keep default c'tor alive for CDI.
	}

	public ActiveCookieUuid(UUID uuid) {
		path = "/" + uuid.toString();
		current = this;
	}

	@PostConstruct
	public void init() {
			current = new ActiveCookieUuid(GENERATE_UUID);
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
