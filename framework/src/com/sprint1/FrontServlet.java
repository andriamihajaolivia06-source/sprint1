package com.sprint1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FrontServlet extends HttpServlet {

    private void servicePersonnalisee(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String fullUri = request.getRequestURI();       
        String contextPath = request.getContextPath(); 
        String relativeUri = fullUri.substring(contextPath.length());  

        if (relativeUri.startsWith("/")) {
            relativeUri = relativeUri.substring(1);  
        }

        // Construct resource path with leading / (e.g., /a.jsp, /index.html)
        String resourcePath = "/" + (relativeUri.isEmpty() ? "index.html" : relativeUri);

        // Check if the resource exists
        if (getServletContext().getResource(resourcePath) != null) {
            // For .jsp files, forward to Tomcat's JSP servlet
            if (resourcePath.endsWith(".jsp")) {
                getServletContext().getNamedDispatcher("jsp").forward(request, response);
                return; // Exit after forwarding
            }
            // For other files (e.g., .html), use standard forwarding
            request.getRequestDispatcher(resourcePath).forward(request, response);
            return; // Exit after forwarding
        }

        // Resource does not exist, display URL
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>FrontServlet</title></head>");
            out.println("<body>");
            out.println("<h1>URL saisie : " + relativeUri + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        servicePersonnalisee(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        servicePersonnalisee(request, response);
    }
}