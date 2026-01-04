package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.IVehiculoDAO;
import es.gestocar.daofactory.DAOFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author alfon
 */
@WebServlet(name = "VisualizarVehiculoController", urlPatterns = {"/VisualizarVehiculoController"})
public class VisualizarVehiculoController extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Compruebo que el usuario esté logueado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        boolean procesarAccion = true;
        String vista = "JSP/login.jsp";

        if (usuario == null) {
            // Si no está logueado, lo mando al login
            procesarAccion = false;
        }

        if (procesarAccion) {
            try {
                // Obtengo el DAO de vehículos para consultar la base de datos
                DAOFactory daof = DAOFactory.getDAOFactory();
                IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();

                // Obtengo SOLO los vehículos que pertenecen al usuario logueado
                List<Vehiculo> vehiculosUsuario = vehiculoDAO.getVehiculosByUsuarioId(usuario.getIdUsuario());

                // Paso la lista de vehículos a la vista JSP
                request.setAttribute("vehiculos", vehiculosUsuario);

                // Si no tiene vehículos, muestro un mensaje informativo
                if (vehiculosUsuario == null || vehiculosUsuario.isEmpty()) {
                    request.setAttribute("mensaje", "No tienes vehículos registrados.");
                }

            } catch (Exception e) {
                request.setAttribute("error", "Error al cargar los vehículos: " + e.getMessage());
            }

            vista = "JSP/visualizarVehiculo.jsp";
        }

        request.getRequestDispatcher(vista).forward(request, response);
    }
} 