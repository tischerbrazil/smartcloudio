package org.geoazul.view.security;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.geoazul.view.location.LocationService;
import java.net.InetAddress;
import java.net.Inet6Address;
import org.omnifaces.cdi.ViewScoped;

@ViewScoped
public class SessionListener implements HttpSessionListener, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	@Inject
	LocationService locationService;

	ServletContext ctx = null;

	static int current = 0;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		String createTime = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"))
				.format(new Date(event.getSession().getCreationTime()));

		current++;

		ctx = event.getSession().getServletContext();
		ctx.setAttribute("currentusers", current);

		String ipCapture = null;

		try {
			String xForwardedForHeader = request.getHeader("X-Forwarded-For");

			if (xForwardedForHeader == null) {
				ipCapture = request.getRemoteAddr();
			} else {
				ipCapture = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
			}
			InetAddress inetAddress = InetAddress.getByName(ipCapture);
			if (inetAddress instanceof Inet6Address) {
			} else {
			}

		} catch (Exception ex) {
		}

		if (ipCapture.equals("127.0.0.1")) {
			ipCapture = "2804:8cc:1103:8000:d99c:29f8:2e5:9f89";
		}

		if (ipCapture.equals("::1")) {
			ipCapture = "2804:8cc:1103:8000:d99c:29f8:2e5:9f89";
		}

		if (!(ipCapture.equals("::1") || ipCapture.equals("127.0.0.1"))) {
			try {


			//	locationService.locationPersist(ipCapture, event.getSession().getId(), 1L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		String ultimoAcesso = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"))
				.format(new Date(event.getSession().getLastAccessedTime()));
		current--;
		ctx.setAttribute("currentusers", current);
	}

}