package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.beans.Vehiculo;
import es.gestocar.dao.IUsuarioDAO;
import es.gestocar.dao.IVehiculoDAO;
import es.gestocar.daofactory.DAOFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author alfon
 */
@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        // IMPORTANTE: Configuro la respuesta como JSON
        resp.setContentType("application/json;charset=UTF-8");
        
        // Recojo la acción que me pide el JavaScript
        String accion = req.getParameter("accion");
        // Uso Gson para convertir objetos Java a JSON
        Gson gson = new Gson();

        try {
            if ("listarUsuarios".equals(accion)) {
                // ACCIÓN: Devuelve lista de usuarios en formato JSON
                // Lo llama listarUsuarios.jsp via AJAX
                
                IUsuarioDAO udao = DAOFactory.getDAOFactory().getUsuarioDAO();
                List<Usuario> usuarios = udao.getUsuarios();
                if (usuarios == null) usuarios = new ArrayList<>();
                
                // Filtro el administrador para que no aparezca en la lista
                List<Usuario> usuariosFiltrados = new ArrayList<>();
                for (Usuario u : usuarios) {
                    if (!"admin@iesalbarregas.es".equalsIgnoreCase(u.getEmail())) {
                        usuariosFiltrados.add(u);
                    }
                }
                
                // Devuelvo la lista en formato JSON
                resp.getWriter().print(gson.toJson(usuariosFiltrados));
                
            } else if ("listarVehiculos".equals(accion)) {
                // ACCIÓN: Devuelve lista de vehículos con sus propietarios en JSON
                // Lo llama listarVehiculos.jsp via AJAX
                
                IVehiculoDAO vdao = DAOFactory.getDAOFactory().getVehiculoDAO();
                IUsuarioDAO udao = DAOFactory.getDAOFactory().getUsuarioDAO();
                
                // Obtengo todos los vehículos del sistema
                List<Vehiculo> vehiculos = vdao.getVehiculos();
                
                if (vehiculos == null) vehiculos = new ArrayList<>();
                
                // Creo una lista que incluye el email del propietario
                // Porque en la tabla quiero mostrar a quién pertenece cada vehículo
                List<Map<String, Object>> vehiculosConPropietario = new ArrayList<>();
                
                for (int i = 0; i < vehiculos.size(); i++) {
                    try {
                        Vehiculo v = vehiculos.get(i);   
                        Map<String, Object> vehiculoMap = new HashMap<>();
                        
                        // Busco el propietario del vehículo
                        Usuario propietario = udao.getUsuarioById(v.getUsuarioId().intValue());
                        String emailPropietario = propietario != null  ? propietario.getEmail() : "Usuario no encontrado";
                        
                        // Creo un mapa con todos los datos del vehículo + el propietario
                        vehiculoMap.put("propietario", emailPropietario);
                        vehiculoMap.put("marca", v.getMarca());
                        vehiculoMap.put("modelo", v.getModelo());
                        vehiculoMap.put("motor", v.getMotor() != null ? v.getMotor().toString() : null);
                        vehiculoMap.put("matricula", v.getMatricula());
                        vehiculoMap.put("cilindrada", v.getCilindrada());
                        vehiculoMap.put("caballos", v.getCaballos());
                        vehiculoMap.put("color", v.getColor());
                        vehiculoMap.put("fechacompra", v.getFechaCompra());
                        vehiculoMap.put("fechaventa", v.getFechaVenta());
                        vehiculoMap.put("preciocompra", v.getPrecioCompra());
                        vehiculoMap.put("precioventa", v.getPrecioVenta());
                        vehiculosConPropietario.add(vehiculoMap);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw ex;
                    }
                }
                
                // Devuelvo la lista completa en formato JSON
                resp.getWriter().print(gson.toJson(vehiculosConPropietario));
                
            } else if ("cambiarEstadoUsuario".equals(accion)) {
                // ACCIÓN: Activar o desactivar un usuario
                // Lo llama el JavaScript cuando el admin hace clic en activar/desactivar
                
                int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
                boolean activar = Boolean.parseBoolean(req.getParameter("activar"));
                
                IUsuarioDAO udao = DAOFactory.getDAOFactory().getUsuarioDAO();
                // Cambio el estado del usuario (campo "baja" en la BD)
                boolean exito = udao.updateUsuarioCampoBaja(idUsuario, !activar);
                
                // Creo una respuesta JSON con el resultado
                Map<String, Object> response = new HashMap<>();
                response.put("ok", exito);
                response.put("mensaje", activar ? "Usuario reactivado" : "Usuario desactivado");
                resp.getWriter().print(gson.toJson(response));
            }
            
        } catch (Exception ex) {
            // Si algo sale mal, devuelvo un error en formato JSON
            ex.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("ok", false);
            error.put("mensaje", "Error: " + ex.getMessage());
            resp.getWriter().print(gson.toJson(error));
        }
    }
} 