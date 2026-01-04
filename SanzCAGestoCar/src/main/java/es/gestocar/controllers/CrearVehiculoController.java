package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.IVehiculoDAO;
import es.gestocar.dao.MotorConverter;
import es.gestocar.daofactory.DAOFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
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

/**
 * 
 * @author alfon
 */
@WebServlet(name = "CrearVehiculoController", urlPatterns = {"/CrearVehiculoController"})
public class CrearVehiculoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuro la codificación y el tipo de respuesta JSON
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        String accion = request.getParameter("accion");
        Gson gson = new Gson();
        Map<String, Object> resultado = new HashMap<>();
        boolean procesarAccion = true;
        
        try {
            if ("validarMatricula".equals(accion)) {
                // ACCIÓN: Valida matrícula en tiempo real
                // Se ejecuta cuando el usuario escribe en el campo matrícula
                
                String matricula = request.getParameter("matricula");
                
                if (matricula == null || matricula.trim().isEmpty()) {
                    resultado.put("error", "Matrícula vacía");
                } else {
                    // Consulto en la base de datos si ya existe esa matrícula
                    DAOFactory daof = DAOFactory.getDAOFactory();
                    IVehiculoDAO vehiculoDAO = daof.getVehiculoDAO();
                    boolean existe = vehiculoDAO.matriculaExiste(matricula.trim());
                    resultado.put("existe", existe);
                }
                
            } else if ("crearVehiculo".equals(request.getParameter("boton"))) {
                // ACCIÓN: Crea un nuevo vehículo
                // Se ejecuta cuando el usuario envía el formulario
                
                // Primero compruebo que esté logueado
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                if (usuario == null) {
                    resultado.put("ok", false);
                    resultado.put("mensaje", "Debes identificarte.");
                    procesarAccion = false;
                }
                
                if (procesarAccion) {
                    DAOFactory daof = DAOFactory.getDAOFactory();
                    IVehiculoDAO adao = daof.getVehiculoDAO();

                    // Valido los precios antes de crear el vehículo
                    String precioCompraStr = request.getParameter("precioCompra");
                    String precioVentaStr = request.getParameter("precioVenta");
                    boolean validacionOk = true;
                    
                    // Valido precio de compra 
                    if (precioCompraStr == null || precioCompraStr.trim().isEmpty()) {
                        resultado.put("ok", false);
                        resultado.put("mensaje", "El precio de compra es obligatorio.");
                        validacionOk = false;
                    } else {
                        try {
                            // Permito usar coma y punto como separador decimal
                            double precioCompra = Double.parseDouble(precioCompraStr.replace(",", "."));
                            if (precioCompra <= 1) {
                                resultado.put("ok", false);
                                resultado.put("mensaje", "El precio de compra debe ser mayor que 1.");
                                validacionOk = false;
                            }
                        } catch (NumberFormatException ex) {
                            resultado.put("ok", false);
                            resultado.put("mensaje", "El precio de compra debe ser un número válido.");
                            validacionOk = false;
                        }
                    }
                    
                    // Valido precio de venta 
                    if (validacionOk && precioVentaStr != null && !precioVentaStr.trim().isEmpty()) {
                        try {
                            double precioVenta = Double.parseDouble(precioVentaStr.replace(",", "."));
                            if (precioVenta <= 1) {
                                resultado.put("ok", false);
                                resultado.put("mensaje", "El precio de venta debe ser mayor que 1.");
                                validacionOk = false;
                            }
                        } catch (NumberFormatException ex) {
                            resultado.put("ok", false);
                            resultado.put("mensaje", "El precio de venta debe ser un número válido.");
                            validacionOk = false;
                        }
                    }

                    if (validacionOk) {
                        // Configuro los conversores para BeanUtils
                        // Necesito convertir el enum Motor y las fechas
                        ConvertUtils.register(new MotorConverter(), Vehiculo.Motor.class);
                        DateConverter converter = new DateConverter(null);
                        converter.setPattern("yyyy-MM-dd");
                        ConvertUtils.register(converter, java.util.Date.class);

                        // Creo el objeto Vehiculo y lo relleno con los datos del formulario
                        Vehiculo vehiculo = new Vehiculo();
                        BeanUtils.populate(vehiculo, request.getParameterMap());
                        // Asigno el vehículo al usuario logueado
                        vehiculo.setUsuarioId(usuario.getIdUsuario());
                        String matricula = vehiculo.getMatricula();
                        
                        // Hago una última comprobación de que la matrícula no existe
                        if (matricula != null && adao.matriculaExiste(matricula.trim())) {
                            resultado.put("ok", false);
                            resultado.put("mensaje", "Ya existe un vehículo con esa matrícula.");
                        } else {
                            // Intento guardar el vehículo en la base de datos
                            boolean exito = adao.add(vehiculo);
                            resultado.put("ok", exito);
                            if (exito) {
                                // Si se crea correctamente, guardo el ID en la sesión
                                // por si lo necesito para otras operaciones
                                int vehiculoId = adao.getLastInsertedId();
                                session.setAttribute("vehiculoId", vehiculoId);
                                resultado.put("mensaje", "Vehículo creado correctamente");
                            } else {
                                resultado.put("mensaje", "No se pudo crear el vehículo.");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // Si algo sale mal, devuelvo un error en formato JSON
            resultado.clear();
            resultado.put("ok", false);
            resultado.put("mensaje", "Error: " + ex.getMessage());
        }
        
        // Devuelvo la respuesta en formato JSON
        response.getWriter().print(gson.toJson(resultado));
    }

}