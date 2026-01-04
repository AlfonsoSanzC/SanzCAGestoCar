package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.dao.UsuarioDAO;
import es.gestocar.models.Utilidades;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * @author alfon
 */
@MultipartConfig  // Necesario para manejar formularios con archivos
@WebServlet(name = "ModificarDatosController", urlPatterns = {"/ModificarDatosController"})
public class ModificarDatosController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/modificarDatos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        String url = "/JSP/menuUsuario.jsp";  
        boolean procesarAccion = true;
        
        // Compruebo que el usuario esté logueado
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            // Si no está logueado, lo mando al inicio
            url = "/index.jsp";
            request.setAttribute("error", "Debes identificarte para acceder.");
            procesarAccion = false;
        }
        
        if (procesarAccion) {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            if ("modificarDatos".equals(accion)) {
                // ACCIÓN: Mostrar el formulario de modificar datos
                url = "/JSP/modificarDatos.jsp";
                
            } else if ("guardarCambios".equals(accion)) {
                // ACCIÓN: Procesar los cambios del formulario
                
                // Recojo todos los campos del formulario
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String email = request.getParameter("email");
                String dni = request.getParameter("dni");
                String contrasenaActual = request.getParameter("contrasenaActual");
                String nuevaContrasena = request.getParameter("nuevaContrasena");
                String confirmarContrasena = request.getParameter("confirmarContrasena");
                
                // Obtengo los archivos subidos (avatar y carnet)
                Part avatarPart = request.getPart("avatar");
                Part carnePart = request.getPart("carneconducir");
                
                // Empiezo las validaciones
                String error = null;
                
                // Valido el formato del DNI si lo han cambiado
                if (dni != null && !dni.trim().isEmpty() && !dni.matches("^[0-9]{8}[A-Za-z]$")) {
                    error = "Formato de DNI no válido.";
                } else if (Utilidades.tieneVacios(contrasenaActual)) {
                    // La contraseña actual es OBLIGATORIA para cualquier cambio
                    error = "La contraseña actual es obligatoria";
                } else {
                    // Verifico que la contraseña actual sea correcta
                    if (!Utilidades.hashPassword(contrasenaActual).equals(usuario.getPassword())) {
                        error = "La contraseña actual es incorrecta";
                    }
                    
                    // Si quiere cambiar la contraseña, valido que coincidan
                    if (error == null && nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                        if (!nuevaContrasena.equals(confirmarContrasena)) {
                            error = "La nueva contraseña y la confirmación no coinciden";
                        }
                    }
                    
                    // Valido el tamaño de los archivos (máximo 100KB cada uno)
                    if (error == null && avatarPart != null && avatarPart.getSize() > 102400) {
                        error = "El avatar no puede superar los 100KB";
                    }
                    
                    if (error == null && carnePart != null && carnePart.getSize() > 102400) {
                        error = "El carnet no puede superar los 100KB";
                    }
                }
                
                if (error != null) {
                    // Si hay errores, vuelvo al formulario con el mensaje
                    request.setAttribute("error", error);
                    url = "/JSP/modificarDatos.jsp";
                } else {
                    // Si no hay errores, actualizo el usuario
                    
                    // Creo un nuevo objeto Usuario con los datos actualizados
                    Usuario usuarioActualizado = new Usuario();
                    usuarioActualizado.setIdUsuario(usuario.getIdUsuario());
                    usuarioActualizado.setNombre(nombre != null ? nombre.trim() : usuario.getNombre());
                    usuarioActualizado.setApellidos(apellidos != null ? apellidos.trim() : usuario.getApellidos());
                    usuarioActualizado.setEmail(usuario.getEmail());  // El email no se puede cambiar
                    usuarioActualizado.setDni(dni != null ? dni.trim() : usuario.getDni());
                    usuarioActualizado.setCampoBaja(usuario.getCampoBaja());
                    usuarioActualizado.setAvatar(usuario.getAvatar()); // Mantengo el avatar actual por defecto
                    usuarioActualizado.setCarneConducir(usuario.getCarneConducir()); // Mantengo el carnet actual por defecto
                    
                    // Mantengo la contraseña actual o uso la nueva
                    if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                        usuarioActualizado.setPassword(Utilidades.hashPassword(nuevaContrasena));
                    } else {
                        usuarioActualizado.setPassword(usuario.getPassword());
                    }
                    
                    try {
                        boolean needsUpdate = false;
                        
                        // Proceso el avatar si han subido uno nuevo
                        if (avatarPart != null && avatarPart.getSize() > 0) {
                            String contentType = avatarPart.getContentType();
                            if (contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
                                String extension = contentType.equals("image/png") ? ".png" : ".jpg";
                                String avatarName = "avatar" + usuario.getIdUsuario() + extension;
                                
                                // Creo la carpeta si no existe
                                String avatarDir = getServletContext().getRealPath("/IMG/avatares/");
                                File avatarDirFile = new File(avatarDir);
                                if (!avatarDirFile.exists()) {
                                    avatarDirFile.mkdirs();
                                }
                                
                                // Guardo el archivo en el servidor
                                String avatarFilePath = avatarDir + avatarName;
                                avatarPart.write(avatarFilePath);
                                
                                usuarioActualizado.setAvatar(avatarName);
                                needsUpdate = true;
                            }
                        }
                        
                        // Proceso el carnet si han subido uno nuevo
                        if (carnePart != null && carnePart.getSize() > 0) {
                            String contentType2 = carnePart.getContentType();
                            if (contentType2 != null && (contentType2.equals("image/png") || contentType2.equals("image/jpeg"))) {
                                String extension2 = contentType2.equals("image/png") ? ".png" : ".jpg";
                                String carnetName = "carnet" + usuario.getIdUsuario() + extension2;
                                
                                // Creo la carpeta si no existe
                                String carnetDir = getServletContext().getRealPath("/IMG/carnets/");
                                File carnetDirFile = new File(carnetDir);
                                if (!carnetDirFile.exists()) {
                                    carnetDirFile.mkdirs();
                                }
                                
                                // Guardo el archivo en el servidor
                                String carnetFilePath = carnetDir + carnetName;
                                carnePart.write(carnetFilePath);
                                
                                // Guardo el nombre del archivo en la base de datos
                                usuarioActualizado.setCarneConducir(carnetName);
                                needsUpdate = true;
                            }
                        }
                        
                        // Actualizo los datos básicos siempre
                        usuarioDAO.update(usuarioActualizado);
                        
                        // Solo actualizo avatar/carnet si han cambiado
                        if (needsUpdate) {
                            usuarioDAO.updateAvatarCarnet(usuarioActualizado);
                        }
                        
                        // Actualizo la sesión con los nuevos datos
                        session.setAttribute("usuario", usuarioActualizado);
                        request.setAttribute("exito", "Datos actualizados correctamente");
                        url = "/JSP/modificarDatos.jsp";
                        
                    } catch (Exception e) {
                        request.setAttribute("error", "Error al actualizar los datos: " + e.getMessage());
                        url = "/JSP/modificarDatos.jsp";
                    }
                }
            }
        }
        
        request.getRequestDispatcher(url).forward(request, response);
    }
} 