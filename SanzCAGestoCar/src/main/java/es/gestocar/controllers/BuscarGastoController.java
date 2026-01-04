package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.dao.IGastoDAO;
import es.gestocar.daofactory.DAOFactory;
import es.gestocar.models.Utilidades;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Lo llamo desde menuUsuario.jsp cuando hago clic en "Buscar Gastos"
 * 
 * @author alfon
 */
@WebServlet(name = "BuscarGastoController", urlPatterns = {"/BuscarGastoController"})
public class BuscarGastoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtengo la sesión y compruebo que el usuario esté logueado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        boolean procesarAccion = true;

        // Si no está logueado, no proceso nada
        if (usuario == null) {
            procesarAccion = false;
        }
        
        if (procesarAccion) {
            // Obtengo el DAO de gastos para trabajar con la base de datos
            DAOFactory daof = DAOFactory.getDAOFactory();
            IGastoDAO gastoDAO = daof.getGastoDAO();
            
            // Miro que acción quiere hacer el usuario
            String accion = request.getParameter("accion");
            
            if (accion == null || "buscar".equals(accion)) {
                // ACCIÓN: Busca gastos con los filtros que ha puesto
                // Uso mi clase Utilidades que tiene toda la lógica de búsqueda
                Utilidades.procesarBusquedaGastos(request, gastoDAO, usuario.getIdUsuario());
                
            } else if ("eliminar".equals(accion)) {
                // ACCIÓN: Elimina un gasto específico
                Utilidades.procesarEliminacionGasto(request, gastoDAO, usuario.getIdUsuario());
                
            } else if ("cargarModificar".equals(accion)) {
                // ACCIÓN: Carga los datos de un gasto para modificarlo
                Utilidades.procesarCargarModificarGasto(request, gastoDAO, usuario.getIdUsuario());
                
            } else if ("modificar".equals(accion)) {
                // ACCIÓN: Guarda los cambios de un gasto modificado
                Utilidades.procesarModificacionGasto(request, gastoDAO, usuario.getIdUsuario());
                
            } else if ("cancelar".equals(accion)) {
                // ACCIÓN: Cancela la modificación
                
                // Simplemente vuelvo a mostrar los resultados de búsqueda
                Utilidades.procesarBusquedaGastos(request, gastoDAO, usuario.getIdUsuario());
            }
            
            // Cargo los conceptos únicos del usuario para el formulario de búsqueda
            // Esto permite autocompletar en el campo "concepto"
            List<String> conceptos = gastoDAO.getConceptosUnicos(usuario.getIdUsuario());
            request.setAttribute("conceptos", conceptos);
        }
        
        request.getRequestDispatcher("JSP/buscarGasto.jsp").forward(request, response);
    }
} 