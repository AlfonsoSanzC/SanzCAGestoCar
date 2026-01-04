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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 * Lo llamo desde menuUsuario.jsp cuando hago clic en "Introducir Gasto"
 * 
 * @author alfon
 */
@WebServlet(name = "GastoController", urlPatterns = {"/GastoController"})
public class GastoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Por defecto siempre vuelvo a la página de introducir gastos
        String vista = "JSP/introducirGasto.jsp";
        boolean hayError = false;
        String mensaje = "";
        
        // Recojo los parámetros que me llegan del formulario
        String boton = request.getParameter("boton");
        String idVehiculoStr = request.getParameter("idVehiculo");
        
        try {
            // Primero compruebo que el usuario esté logueado
            // Si no está logueado, lo mando al login
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            if (usuario == null) {
                vista = "JSP/login.jsp";
                request.setAttribute("error", "Debes identificarte para acceder.");
                hayError = true;
            } else {
                // Si está logueado, obtengo los DAOs que necesito
                DAOFactory daof = DAOFactory.getDAOFactory();
                IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();
                IGastoDAO gastoDAO = daof.getGastoDAO();
                
                // Cargo los vehículos del usuario para el desplegable del formulario
                // Solo cargo los vehículos que pertenecen al usuario logueado
                List<Vehiculo> vehiculosUsuario = vehiculoDAO.getVehiculosByUsuarioId(usuario.getIdUsuario());
                request.setAttribute("vehiculos", vehiculosUsuario);
                
                // Cargo los conceptos únicos que ya ha usado este usuario
                // para el datalist que le ayuda a autocompletar
                List<String> conceptos = gastoDAO.getConceptosUnicos(usuario.getIdUsuario());
                request.setAttribute("conceptos", conceptos);
                
                // Miro qué botón ha pulsado el usuario
                if ("insertarGasto".equals(boton)) {
                    // Si viene del menú, solo muestro el formulario vacío
                    vista = "JSP/introducirGasto.jsp";
                    
                } else if ("guardarGasto".equals(boton)) {
                    // Si ha pulsado "Guardar Gasto", proceso el formulario
                    boolean validacionOk = true;
                    
                    // Primero valido que haya seleccionado un vehículo
                    if (idVehiculoStr == null || idVehiculoStr.trim().isEmpty()) {
                        request.setAttribute("error", "Debes seleccionar un vehículo.");
                        validacionOk = false;
                        hayError = true;
                    } else {
                        try {
                            Short idVehiculo = Short.parseShort(idVehiculoStr);
                            
                            // Compruebo que el vehículo seleccionado realmente pertenece al usuario
                            // Esto es importante para la seguridad, evito que manipulen el formulario
                            boolean perteneceAlUsuario = false;
                            for (Vehiculo v : vehiculosUsuario) {
                                if (v.getIdVehiculo().equals(idVehiculo)) {
                                    perteneceAlUsuario = true;
                                    break;
                                }
                            }
                            
                            if (!perteneceAlUsuario) {
                                request.setAttribute("error", "El vehículo seleccionado no te pertenece.");
                                validacionOk = false;
                                hayError = true;
                            } else {
                                // Recojo todos los campos del formulario
                                String concepto = request.getParameter("concepto");
                                String descripcion = request.getParameter("descripcion");
                                String importeStr = request.getParameter("importe");
                                String fechaGastoStr = request.getParameter("fechaGasto");
                                
                                // Valido que todos los campos obligatorios estén rellenos
                                if (concepto == null || concepto.trim().isEmpty()) {
                                    request.setAttribute("error", "El concepto es obligatorio.");
                                    validacionOk = false;
                                    hayError = true;
                                } else if (descripcion == null || descripcion.trim().isEmpty()) {
                                    request.setAttribute("error", "La descripción es obligatoria.");
                                    validacionOk = false;
                                    hayError = true;
                                } else if (importeStr == null || importeStr.trim().isEmpty()) {
                                    request.setAttribute("error", "El importe es obligatorio.");
                                    validacionOk = false;
                                    hayError = true;
                                } else if (fechaGastoStr == null || fechaGastoStr.trim().isEmpty()) {
                                    request.setAttribute("error", "La fecha del gasto es obligatoria.");
                                    validacionOk = false;
                                    hayError = true;
                                } else {
                                    // Valido específicamente el importe
                                    try {
                                        // Permito que usen coma o punto como separador decimal
                                        double importe = Double.parseDouble(importeStr.replace(",", "."));
                                        
                                        if (importe <= 0) {
                                            request.setAttribute("error", "El importe debe ser mayor que 0.");
                                            validacionOk = false;
                                            hayError = true;
                                        } else if (importe > 9999.99) {
                                            // Este límite viene de la bbdd: DECIMAL(6,2)
                                            request.setAttribute("error", "El importe no puede superar 9999.99€.");
                                            validacionOk = false;
                                            hayError = true;
                                        }
                                    } catch (NumberFormatException ex) {
                                        request.setAttribute("error", "El importe debe ser un número válido.");
                                        validacionOk = false;
                                        hayError = true;
                                    }
                                }
                                
                                // Si todas las validaciones han pasado, guardo el gasto
                                if (validacionOk && !hayError) {
                                    try {
                                        // Configuro el conversor de fechas para BeanUtils
                                        DateConverter converter = new DateConverter(null);
                                        converter.setPattern("yyyy-MM-dd");
                                        ConvertUtils.register(converter, java.util.Date.class);
                                        
                                        // Creo el objeto Gasto y lo relleno automáticamente
                                        // con los parámetros del request
                                        Gasto gasto = new Gasto();
                                        BeanUtils.populate(gasto, request.getParameterMap());
                                        gasto.setIdVehiculo(idVehiculo);
                                        
                                        // Intento guardar el gasto en la base de datos
                                        Boolean exito = gastoDAO.add(gasto);
                                        
                                        if (exito) {
                                            request.setAttribute("exito", "Gasto añadido correctamente.");
                                            mensaje = "Gasto añadido correctamente.";
                                        } else {
                                            request.setAttribute("error", "No se pudo guardar el gasto.");
                                            hayError = true;
                                        }
                                        
                                    } catch (Exception ex) {
                                        request.setAttribute("error", "Error al procesar el gasto: " + ex.getMessage());
                                        hayError = true;
                                    }
                                }
                            }
                            
                        } catch (NumberFormatException ex) {
                            request.setAttribute("error", "ID de vehículo inválido.");
                            validacionOk = false;
                            hayError = true;
                        }
                    }
                    
                    // Después de procesar, siempre vuelvo al formulario
                    vista = "JSP/introducirGasto.jsp";
                }
            }
            
        } catch (Exception ex) {
            // Si algo sale mal, muestro un error genérico
            request.setAttribute("error", "Error del sistema: " + ex.getMessage());
            hayError = true;
            vista = "JSP/introducirGasto.jsp";
        }
        
        request.getRequestDispatcher(vista).forward(request, response);
    }
} 