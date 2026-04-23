package atest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/api2/secure-data")
public class SecureServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json");
        
        
        String authHeader = request.getHeader("Authorization");
        String secretToken = request.getHeader("X-Secure-Token");
        
        String username = request.getRemoteUser();

      
        
     
       
        
        
        Map<String, String[]> paramMap = request.getParameterMap();
    	for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
    	    String paramName = entry.getKey();
    	    String[] paramValues = entry.getValue();
    	}
    	
    	Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
        }
        
        String authHeader2 = request.getHeader("Authorization");
        if (authHeader2 != null && authHeader2.startsWith("Basic")) {
            // Decoding Base64 (requires java.util.Base64)
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
            // Credentials format is "user:password"
            final String[] values = credentials.split(":", 2);
            String username2 = values[0];
            String password2= values[1];
            
            
            
            
            
            
        }
        
        
        // 1. Validate Token
        if (secretToken == null || !secretToken.equals("super-secret-key")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("{\"error\": \"Invalid Token\"}");
            return;
        }

        // 2. Simple Basic Auth Check Example (Simplified)
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\": \"Unauthorized\"}");
            return;
        }

        // Process request
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"message\": \"Secure data accessed\"}");
    }
}