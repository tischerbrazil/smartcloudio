package atest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api2/hello") // Maps the servlet to the /api/hello URL
public class HelloApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
           throws ServletException, IOException {
    	
    	Map<String, String[]> paramMap = request.getParameterMap();
    	for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
    	    String paramName = entry.getKey();
    	    String[] paramValues = entry.getValue();
    	}
    	 //String userName = request.getParameter("txtUserName");
         //String password = request.getParameter("txtPassword");
        
        // Set the content type and encoding for the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Use a PrintWriter to send the JSON response
        try (PrintWriter writer = response.getWriter()) {
            writer.println("{\"message\": \"Hello, World! This is a Jakarta Servlet API.\"}");
        }
    }
    
    // You can override other methods like doPost(), doPut(), doDelete() for other HTTP methods
    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    //        throws ServletException, IOException { ... }
}