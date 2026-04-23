package org.tenant.hibernate.deltaspyke.jpa;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
public class TenantProducer {

	@Inject
	private HttpServletRequest request;

	public boolean getPermited(String status) {
		switch (status) {
		case "localhost":
			return false;
		case "127.0.0.1":
			return false;
		case "192.168.88.3":
			return false;
		case "192.168.88.4":
			return false;
		case "mapacultural-ro.com.br":
			return false;
		case "smartcloudio.com.br":
			return false;
		case "sicoob.smartcloudio.com.br":
			return false;
		case "osvaldoduarte.com.br":
			return false;
		default:
			return true;
		}
	}

	@Produces
	@RequestScoped
	@TenantInject
	public Tenant getTenant() {
		Tenant tenant;

		String serverVerify = request.getServerName();
		if (getPermited(serverVerify)) {
			throw new IllegalStateException("------------NOT IN SERVER LIST ALLOWED-----------");
		}
		
		String servername =serverVerify.replace("-", "_").replace(".", "_");
		
		
		try {
			tenant = new Tenant("geoazul_" + servername);
		} catch (Exception ex) {
			throw new IllegalStateException("Cannot find realm-specific configuration file");
		}
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tenant;
	}
}
