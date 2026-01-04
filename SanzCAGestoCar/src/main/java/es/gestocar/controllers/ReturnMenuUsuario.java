package es.gestocar.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReturnMenuUsuario", urlPatterns = {"/ReturnMenuUsuario"})
public class ReturnMenuUsuario extends HttpServlet {

    //Para volver a la vista de menuUsuario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("JSP/menuUsuario.jsp").forward(request, response);
    }

}
