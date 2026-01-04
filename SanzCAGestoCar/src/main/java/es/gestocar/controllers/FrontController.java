package es.gestocar.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alfon
 */
@WebServlet(name = "FrontController", urlPatterns = {"/FrontController"})
public class FrontController extends HttpServlet {

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp"; 
        String accion = request.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "login":
                    url = "/JSP/login.jsp";
                    break;
                case "registro":
                    url = "/JSP/registro.jsp";
                    break;
                default:
                    url = "/index.jsp";
                    break;
            }
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

}
