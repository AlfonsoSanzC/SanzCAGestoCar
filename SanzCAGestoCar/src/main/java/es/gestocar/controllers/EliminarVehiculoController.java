package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.IVehiculoDAO;
import es.gestocar.daofactory.DAOFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**

 * @author alfon
 */
@WebServlet(name = "EliminarVehiculoController", urlPatterns = {"/EliminarVehiculoController"})
public class EliminarVehiculoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Compruebo que el usuario esté logueado
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String url = "/index.jsp";  
        String accion = request.getParameter("accion");
        
        if (usuario != null) {
            // Si está logueado, obtengo el DAO de vehículos
            DAOFactory daof = DAOFactory.getDAOFactory();
            IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();
            
            if ("eliminarVehiculos".equals(accion)) {
                // ACCIÓN: Elimina vehículos seleccionados (petición AJAX)
                // El usuario ha marcado checkboxes y pulsado "Eliminar seleccionados"
                
                response.setContentType("application/json;charset=UTF-8");
                Gson gson = new Gson();
                
                try {
                    // Recojo los IDs de los vehículos seleccionados
                    // Vienen como array desde los checkboxes del formulario
                    String[] vehiculosIds = request.getParameterValues("vehiculosIds[]");
                    
                    if (vehiculosIds != null && vehiculosIds.length > 0) {
                        // Convierto los IDs de String a Short
                        Short[] ids = new Short[vehiculosIds.length];
                        for (int i = 0; i < vehiculosIds.length; i++) {
                            ids[i] = Short.parseShort(vehiculosIds[i]);
                        }
                        
                        // Solo marco el campo "baja" como true en la base de datos, osea no los borro fisicamente
                        boolean exito = vehiculoDAO.eliminarVehiculosLogico(ids);
                        
                        // Creo la respuesta JSON
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("ok", exito);
                        if (exito) {
                            resultado.put("mensaje", "Vehículos dados de baja correctamente");
                        } else {
                            resultado.put("mensaje", "Error al dar de baja los vehículos");
                        }
                        response.getWriter().print(gson.toJson(resultado));
                        
                    } else {
                        // Si no seleccionaron ningún vehículo
                        Map<String, Object> error = new HashMap<>();
                        error.put("ok", false);
                        error.put("mensaje", "No se seleccionaron vehículos");
                        response.getWriter().print(gson.toJson(error));
                    }
                } catch (Exception ex) {
                    // Si algo sale mal, devuelvo error en JSON
                    Map<String, Object> error = new HashMap<>();
                    error.put("ok", false);
                    error.put("mensaje", "Error: " + ex.getMessage());
                    ex.printStackTrace();
                    response.getWriter().print(gson.toJson(error));
                }
            } else {
                // ACCIÓN por defecto: Muestra la página de eliminar vehículos
                // Cargo solo los vehículos ACTIVOS del usuario (no los dados de baja)
                List<Vehiculo> vehiculos = vehiculoDAO.getVehiculosActivosByUsuarioId(usuario.getIdUsuario());
                request.setAttribute("vehiculos", vehiculos);
                url = "/JSP/eliminarVehiculo.jsp";
            }
        }
        
        // Solo hago forward si NO es una petición AJAX de eliminación
        if (!"eliminarVehiculos".equals(accion)) {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
} 