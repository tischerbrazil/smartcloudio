package modules;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String filename = request.getPathInfo().substring(1);
    File file = new File("/home/tischer/aimg/", filename);
    response.setHeader("Content-Type", getServletContext().getMimeType(filename));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
    Files.copy(file.toPath(), response.getOutputStream());
}
}
