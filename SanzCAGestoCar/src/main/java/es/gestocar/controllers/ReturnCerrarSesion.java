package es.gestocar.controllers;

import es.gestocar.models.Utilidades;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReturnCerrarSesion", urlPatterns = {"/ReturnCerrarSesion"})
public class ReturnCerrarSesion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Limpia la sesión usando el método de Utilidades
        Utilidades.limpiarSesion(request);
        
        // Invalida completamente la sesión
        request.getSession().invalidate();
        
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
} 