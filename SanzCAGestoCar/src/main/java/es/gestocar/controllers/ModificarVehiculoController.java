package es.gestocar.controllers;

import es.gestocar.dao.IVehiculoDAO;

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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.google.gson.Gson;
import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.MotorConverter;
import es.gestocar.daofactory.DAOFactory;

/**
 * @author alfon
 */
@WebServlet(name = "ModificarVehiculoController", urlPatterns = {"/ModificarVehiculoController"})
public class ModificarVehiculoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String vista = "JSP/modificarVehiculo.jsp";
        Gson gson = new Gson();
        String accion = request.getParameter("accion");
        boolean hayError = false;
        
        if (accion == null) {
            vista = "JSP/modificarVehiculo.jsp";
        } else {
            // Es una petición AJAX, respondo JSON directamente
            response.setContentType("application/json;charset=UTF-8");
            
            try {
                // Verifico que el usuario esté logueado
                HttpSession session = request.getSession();
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                
                if (usuario == null) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("ok", false);
                    error.put("mensaje", "Debes identificarte.");
                    response.getWriter().print(gson.toJson(error));
                    hayError = true;
                } else {
                    DAOFactory daof = DAOFactory.getDAOFactory();
                    IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();
                    
                    switch (accion) {
                        case "listarVehiculos":
                            // ACCIÓN: Devuelve una lista de vehículos del usuario en JSON
                            // Para que pueda seleccionar cuál modificar
                            if (!hayError) {
                                List<Vehiculo> vehiculos = vehiculoDAO.getVehiculosActivosByUsuarioId(usuario.getIdUsuario());
                                response.getWriter().print(gson.toJson(vehiculos));
                            }
                            break;
                            
                        case "obtenerVehiculo":
                            // ACCIÓN: Obtiene datos de un vehículo específico
                            // Para cargar el formulario con los datos actuales
                            String idVehiculoStr = request.getParameter("idVehiculo");
                            if (idVehiculoStr == null || idVehiculoStr.trim().isEmpty()) {
                                Map<String, Object> error = new HashMap<>();
                                error.put("ok", false);
                                error.put("mensaje", "ID de vehículo requerido.");
                                response.getWriter().print(gson.toJson(error));
                                hayError = true;
                            } else {
                                try {
                                    Short idVehiculo = Short.parseShort(idVehiculoStr);
                                    List<Vehiculo> todosVehiculos = vehiculoDAO.getVehiculosActivosByUsuarioId(usuario.getIdUsuario());
                                    Vehiculo vehiculoEncontrado = null;
                                    
                                    // Busco el vehículo entre los que pertenecen al usuario
                                    for (Vehiculo v : todosVehiculos) {
                                        if (v.getIdVehiculo().equals(idVehiculo)) {
                                            vehiculoEncontrado = v;
                                            break;
                                        }
                                    }
                                    
                                    if (vehiculoEncontrado != null) {
                                        response.getWriter().print(gson.toJson(vehiculoEncontrado));
                                    } else {
                                        Map<String, Object> error = new HashMap<>();
                                        error.put("ok", false);
                                        error.put("mensaje", "Vehículo no encontrado o no pertenece al usuario.");
                                        response.getWriter().print(gson.toJson(error));
                                        hayError = true;
                                    }
                                } catch (NumberFormatException ex) {
                                    Map<String, Object> error = new HashMap<>();
                                    error.put("ok", false);
                                    error.put("mensaje", "ID de vehículo inválido.");
                                    response.getWriter().print(gson.toJson(error));
                                    hayError = true;
                                }
                            }
                            break;
                            
                        case "modificarVehiculo":
                            // ACCIÓN: Modifica un vehículo existente
                            // Procesa el formulario de modificación
                            String idModificarStr = request.getParameter("idVehiculo");
                            if (idModificarStr == null || idModificarStr.trim().isEmpty()) {
                                Map<String, Object> error = new HashMap<>();
                                error.put("ok", false);
                                error.put("mensaje", "ID de vehículo requerido.");
                                response.getWriter().print(gson.toJson(error));
                                hayError = true;
                            } else {
                                // Valido los precios antes de modificar el vehículo
                                String precioCompraStr = request.getParameter("precioCompra");
                                String precioVentaStr = request.getParameter("precioVenta");
                                boolean validacionOk = true;
                                
                                // Valido precio de compra
                                if (precioCompraStr == null || precioCompraStr.trim().isEmpty()) {
                                    Map<String, Object> error = new HashMap<>();
                                    error.put("ok", false);
                                    error.put("mensaje", "El precio de compra es obligatorio.");
                                    response.getWriter().print(gson.toJson(error));
                                    validacionOk = false;
                                    hayError = true;
                                } else {
                                    try {
                                        // Permito usar coma como separador decimal
                                        double precioCompra = Double.parseDouble(precioCompraStr.replace(",", "."));
                                        if (precioCompra <= 1) {
                                            Map<String, Object> error = new HashMap<>();
                                            error.put("ok", false);
                                            error.put("mensaje", "El precio de compra debe ser mayor que 1.");
                                            response.getWriter().print(gson.toJson(error));
                                            validacionOk = false;
                                            hayError = true;
                                        }
                                    } catch (NumberFormatException ex) {
                                        Map<String, Object> error = new HashMap<>();
                                        error.put("ok", false);
                                        error.put("mensaje", "El precio de compra debe ser un número válido.");
                                        response.getWriter().print(gson.toJson(error));
                                        validacionOk = false;
                                        hayError = true;
                                    }
                                }
                                
                                // Valido precio de venta
                                if (validacionOk && precioVentaStr != null && !precioVentaStr.trim().isEmpty()) {
                                    try {
                                        double precioVenta = Double.parseDouble(precioVentaStr.replace(",", "."));
                                        if (precioVenta <= 1) {
                                            Map<String, Object> error = new HashMap<>();
                                            error.put("ok", false);
                                            error.put("mensaje", "El precio de venta debe ser mayor que 1.");
                                            response.getWriter().print(gson.toJson(error));
                                            validacionOk = false;
                                            hayError = true;
                                        }
                                    } catch (NumberFormatException ex) {
                                        Map<String, Object> error = new HashMap<>();
                                        error.put("ok", false);
                                        error.put("mensaje", "El precio de venta debe ser un número válido.");
                                        response.getWriter().print(gson.toJson(error));
                                        validacionOk = false;
                                        hayError = true;
                                    }
                                }
                                
                                if (validacionOk && !hayError) {
                                    try {
                                        Short idModificar = Short.parseShort(idModificarStr);
                                        
                                        // Verifico que el vehículo pertenece al usuario
                                        List<Vehiculo> vehiculosUsuario = vehiculoDAO.getVehiculosActivosByUsuarioId(usuario.getIdUsuario());
                                        boolean perteneceAlUsuario = false;
                                        for (Vehiculo v : vehiculosUsuario) {
                                            if (v.getIdVehiculo().equals(idModificar)) {
                                                perteneceAlUsuario = true;
                                                break;
                                            }
                                        }
                                        
                                        if (!perteneceAlUsuario) {
                                            Map<String, Object> error = new HashMap<>();
                                            error.put("ok", false);
                                            error.put("mensaje", "No tienes permisos para modificar este vehículo.");
                                            response.getWriter().print(gson.toJson(error));
                                            hayError = true;
                                        } else {
                                            // Configuro los conversores para BeanUtils
                                            ConvertUtils.register(new MotorConverter(), Vehiculo.Motor.class);
                                            DateConverter converter = new DateConverter(null);
                                            converter.setPattern("yyyy-MM-dd");
                                            ConvertUtils.register(converter, java.util.Date.class);
                                            
                                            // Creo el vehículo con los nuevos datos
                                            Vehiculo vehiculoModificado = new Vehiculo();
                                            BeanUtils.populate(vehiculoModificado, request.getParameterMap());
                                            vehiculoModificado.setIdVehiculo(idModificar);
                                            vehiculoModificado.setUsuarioId(usuario.getIdUsuario());
                                            
                                            // Verifico que la matrícula no esté duplicada, solo si cambió
                                            String nuevaMatricula = vehiculoModificado.getMatricula();
                                            boolean matriculaOk = true;
                                            if (nuevaMatricula != null) {
                                                // Obtengo la matrícula actual para compararla
                                                List<Vehiculo> todosVehiculos = vehiculoDAO.getVehiculosActivosByUsuarioId(usuario.getIdUsuario());
                                                String matriculaActual = null;
                                                for (Vehiculo v : todosVehiculos) {
                                                    if (v.getIdVehiculo().equals(idModificar)) {
                                                        matriculaActual = v.getMatricula();
                                                        break;
                                                    }
                                                }
                                                
                                                // Solo verifico duplicados si la matrícula cambió
                                                if (!nuevaMatricula.equals(matriculaActual) && vehiculoDAO.matriculaExiste(nuevaMatricula.trim())) {
                                                    Map<String, Object> error = new HashMap<>();
                                                    error.put("ok", false);
                                                    error.put("mensaje", "Ya existe un vehículo con esa matrícula.");
                                                    response.getWriter().print(gson.toJson(error));
                                                    matriculaOk = false;
                                                    hayError = true;
                                                }
                                            }
                                            
                                            if (matriculaOk && !hayError) {
                                                try {
                                                    // Intento actualizar el vehículo en la base de datos
                                                    vehiculoDAO.update(vehiculoModificado);
                                                    Map<String, Object> resultado = new HashMap<>();
                                                    resultado.put("ok", true);
                                                    resultado.put("mensaje", "Vehículo modificado correctamente.");
                                                    response.getWriter().print(gson.toJson(resultado));
                                                } catch (Exception updateEx) {
                                                    Map<String, Object> resultado = new HashMap<>();
                                                    resultado.put("ok", false);
                                                    resultado.put("mensaje", "No se pudo modificar el vehículo: " + updateEx.getMessage());
                                                    response.getWriter().print(gson.toJson(resultado));
                                                    hayError = true;
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException ex) {
                                        Map<String, Object> error = new HashMap<>();
                                        error.put("ok", false);
                                        error.put("mensaje", "ID de vehículo inválido.");
                                        response.getWriter().print(gson.toJson(error));
                                        hayError = true;
                                    }
                                }
                            }
                            break;
                            
                        default:
                            // Si llega una acción que no reconozco
                            Map<String, Object> error = new HashMap<>();
                            error.put("ok", false);
                            error.put("mensaje", "Acción no válida.");
                            response.getWriter().print(gson.toJson(error));
                            hayError = true;
                            break;
                    }
                }
            } catch (Exception ex) {
                // Si algo sale mal, devuelvo un error en formato JSON
                Map<String, Object> error = new HashMap<>();
                error.put("ok", false);
                error.put("mensaje", "Error: " + ex.getMessage());
                response.getWriter().print(gson.toJson(error));
                hayError = true;
            }
        }
        
        // Solo hago forward si no es una petición AJAX
        if (accion == null) {
            request.getRequestDispatcher(vista).forward(request, response);
        }
    }
} 