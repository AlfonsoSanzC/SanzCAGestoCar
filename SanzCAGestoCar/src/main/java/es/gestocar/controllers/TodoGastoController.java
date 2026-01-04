package es.gestocar.controllers;

import es.gestocar.beans.Gasto;
import es.gestocar.beans.Usuario;
import es.gestocar.dao.IGastoDAO;
import es.gestocar.daofactory.DAOFactory;
import es.gestocar.models.Utilidades;
import java.io.IOException;
import java.math.BigDecimal;
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
@WebServlet(name = "TodoGastoController", urlPatterns = {"/TodoGastoController"})
public class TodoGastoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Compruebo que el usuario esté logueado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        boolean procesarAccion = true;
        String vista = "JSP/login.jsp";

        if (usuario == null) {
            procesarAccion = false;
        }

        if (procesarAccion) {
            // Obtengo el DAO de gastos para trabajar con la base de datos
            DAOFactory daof = DAOFactory.getDAOFactory();
            IGastoDAO gastoDAO = daof.getGastoDAO();

            // Siempre cargo los datos básicos para que no desaparezcan
            Utilidades.cargarDatosEstadisticas(request, gastoDAO, usuario.getIdUsuario());

            // Miro qué acción específica quiere hacer el usuario
            String accion = request.getParameter("accion");

            if ("gastosPorConcepto".equals(accion)) {
                // CONSULTA: Gastos filtrados por concepto
                String concepto = request.getParameter("concepto");
                if (concepto != null && !concepto.trim().isEmpty()) {
                    List<Gasto> gastos = gastoDAO.getGastosByConcepto(concepto, usuario.getIdUsuario());
                    BigDecimal total = Utilidades.calcularTotalGastos(gastos);
                    
                    request.setAttribute("gastosConcepto", gastos);
                    request.setAttribute("totalConcepto", total);
                    request.setAttribute("conceptoSeleccionado", concepto);
                }
                
            } else if ("gastosPorAno".equals(accion)) {
                // CONSULTA: Gastos filtrados por año
                String anoStr = request.getParameter("ano");
                if (anoStr != null && !anoStr.trim().isEmpty()) {
                    try {
                        int ano = Integer.parseInt(anoStr);
                        List<Gasto> gastos = gastoDAO.getGastosByAno(ano, usuario.getIdUsuario());
                        BigDecimal total = Utilidades.calcularTotalGastos(gastos);
                        
                        request.setAttribute("gastosAno", gastos);
                        request.setAttribute("totalAno", total);
                        request.setAttribute("anoSeleccionado", ano);
                    } catch (NumberFormatException ex) {
                        request.setAttribute("error", "Año inválido.");
                    }
                }
                
            } else if ("gastosConceptoAno".equals(accion)) {
                // CONSULTA: Gastos filtrados por concepto Y año
                String concepto = request.getParameter("concepto");
                String anoStr = request.getParameter("ano");
                
                if (concepto != null && !concepto.trim().isEmpty() && 
                    anoStr != null && !anoStr.trim().isEmpty()) {
                    try {
                        int ano = Integer.parseInt(anoStr);
                        List<Gasto> gastos = gastoDAO.getGastosByConceptoYAno(concepto, ano, usuario.getIdUsuario());
                        BigDecimal total = Utilidades.calcularTotalGastos(gastos);
                        
                        request.setAttribute("gastosConceptoAno", gastos);
                        request.setAttribute("totalConceptoAno", total);
                        request.setAttribute("conceptoAnoSeleccionado", concepto);
                        request.setAttribute("anoConceptoSeleccionado", ano);
                    } catch (NumberFormatException ex) {
                        request.setAttribute("error", "Año inválido.");
                    }
                }
                
            } else if ("gastosSuperanImporte".equals(accion)) {
                // CONSULTA: Gastos que superan un importe mínimo
                String importeStr = request.getParameter("importe");
                if (importeStr != null && !importeStr.trim().isEmpty()) {
                    try {
                        double importe = Double.parseDouble(importeStr);
                        List<Gasto> gastos = gastoDAO.getGastosSuperanImporte(importe, usuario.getIdUsuario());
                        BigDecimal total = Utilidades.calcularTotalGastos(gastos);
                        
                        request.setAttribute("gastosImporte", gastos);
                        request.setAttribute("totalImporte", total);
                        request.setAttribute("importeMinimo", importe);
                    } catch (NumberFormatException ex) {
                        request.setAttribute("error", "Importe inválido.");
                    }
                }
            }
            
            vista = "JSP/totalGastos.jsp";
        }
        
        request.getRequestDispatcher(vista).forward(request, response);
    }
} 