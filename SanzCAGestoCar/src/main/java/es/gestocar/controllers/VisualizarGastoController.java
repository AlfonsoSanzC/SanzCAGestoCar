package es.gestocar.controllers;

import es.gestocar.beans.Gasto;
import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.IGastoDAO;
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
 * Este es mi controlador para visualizar gastos de vehículos.
 * Permite seleccionar un vehículo y ver todos sus gastos.
 * 
 * Funcionalidades:
 * - Mostrar lista de vehículos del usuario para seleccionar
 * - Mostrar gastos del vehículo seleccionado
 * - Paginación de gastos (hasta 100 por página)
 * 
 * Lo llamo desde menuUsuario.jsp cuando hago clic en "Ver Gastos"
 * 
 * @author alfon
 */
@WebServlet(name = "VisualizarGastoController", urlPatterns = {"/VisualizarGastoController"})
public class VisualizarGastoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Compruebo que el usuario esté logueado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String url = "JSP/login.jsp";  // Por defecto al login si no está logueado
        boolean procesarAccion = true;
        
        if (usuario == null) {
            // Si no está logueado, lo mando al login
            procesarAccion = false;
        }
        
        if (procesarAccion) {
            DAOFactory daof = DAOFactory.getDAOFactory();
            IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();
            
            // Cargo los vehículos del usuario para el desplegable
            List<Vehiculo> vehiculos = vehiculoDAO.getVehiculosByUsuarioId(usuario.getIdUsuario());
            request.setAttribute("vehiculos", vehiculos);
            
            // Si se seleccionó un vehículo, cargo sus gastos
            String idVehiculoStr = request.getParameter("idVehiculo");
            if (idVehiculoStr != null && !idVehiculoStr.trim().isEmpty()) {
                try {
                    int idVehiculo = Integer.parseInt(idVehiculoStr);
                    
                    IGastoDAO gastoDAO = daof.getGastoDAO();
                    List<Gasto> gastos = gastoDAO.obtenerGastosPorVehiculoPaginados(idVehiculo, 0, 100); 
                    
                    request.setAttribute("gastos", gastos);
                    request.setAttribute("vehiculoSeleccionado", idVehiculo);
                    
                    // Busco el nombre del vehículo seleccionado para mostrarlo
                    for (Vehiculo v : vehiculos) {
                        if (v.getIdVehiculo() == idVehiculo) {
                            request.setAttribute("nombreVehiculo", v.getMarca() + " " + v.getModelo() + " - " + v.getMatricula());
                            break;
                        }
                    }
                } catch (NumberFormatException ex) {
                    // Si el ID no es válido, simplemente no cargo gastos
                    request.setAttribute("error", "ID de vehículo inválido.");
                }
            }
            
            url = "JSP/visualizarGasto.jsp";
        }
        
        request.getRequestDispatcher(url).forward(request, response);
    }
} 
