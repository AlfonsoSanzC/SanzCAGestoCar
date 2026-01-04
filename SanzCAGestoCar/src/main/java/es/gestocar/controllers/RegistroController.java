package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.dao.IUsuarioDAO;
import es.gestocar.daofactory.DAOFactory;
import es.gestocar.models.Utilidades;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author alfon
 */
@MultipartConfig  // Necesario para manejar formularios con archivos
@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
public class RegistroController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Configuro la codificación para evitar problemas con tildes
        req.setCharacterEncoding("UTF-8");
        String url = "/JSP/registro.jsp";  
        String mensaje = null;

        if ("crearCuenta".equals(req.getParameter("boton"))) {
            
            // Primero valido que todos los campos obligatorios estén rellenos
            if (Utilidades.isFormCompleto(req.getParameterNames(), req)) {
                mensaje = "Todos los campos son obligatorios.";
                
            } else if (!req.getParameter("dni").matches("^[0-9]{8}[A-Za-z]$")) {
                // Valido el formato del DNI: 8 números + 1 letra
                mensaje = "Formato de DNI no válido.";
                
            } else {
                // Obtengo los archivos subidos (avatar y carnet)
                Part avatarPart = req.getPart("avatar");
                Part carnePart = req.getPart("carneconducir");

                // Valido el tamaño de los archivos (máximo 100KB cada uno)
                if (avatarPart != null && avatarPart.getSize() > 0 && avatarPart.getSize() > 102400) {
                    mensaje = "El avatar no puede superar los 100KB.";
                } else if (carnePart != null && carnePart.getSize() > 0 && carnePart.getSize() > 102400) {
                    mensaje = "El carnet de conducir no puede superar los 100KB.";
                } else {
                    // Si las validaciones pasan, intento crear el usuario
                    IUsuarioDAO udao = DAOFactory.getDAOFactory().getUsuarioDAO();

                    // Compruebo que el email no esté ya registrado
                    if (udao.emailExiste(req.getParameter("email"))) {
                        mensaje = "El email ya está registrado.";
                    } else {
                        // Creo el objeto Usuario y lo relleno con los datos del formulario
                        Usuario u = new Usuario();
                        try {
                            BeanUtils.populate(u, req.getParameterMap());
                            // IMPORTANTE: Hasheo la contraseña antes de guardarla
                            u.setPassword(DigestUtils.md5Hex(req.getParameter("password")));
                            // Pongo valores por defecto para avatar y carnet
                            u.setAvatar("default-avatar.png");
                            u.setCarneConducir("default-carne.png");
                        } catch (Exception ex) {
                            throw new ServletException(ex);
                        }

                        // Guardo el usuario en la base de datos
                        udao.add(u);

                        // Obtengo el ID que se ha generado automáticamente
                        short idUsuario = u.getIdUsuario();

                        // Creo las carpetas para guardar las imágenes si no existen
                        String avatarBasePath = getServletContext().getRealPath("/IMG/avatares");
                        String carnetBasePath = getServletContext().getRealPath("/IMG/carnets");

                        File avatarDir = new File(avatarBasePath);
                        File carnetDir = new File(carnetBasePath);

                        if (!avatarDir.exists()) {
                            avatarDir.mkdirs();
                        }

                        if (!carnetDir.exists()) {
                            carnetDir.mkdirs();
                        }

                        boolean needsUpdate = false;

                        // Proceso el avatar si el usuario ha subido uno
                        if (avatarPart != null && avatarPart.getSize() > 0) {
                            String contentType = avatarPart.getContentType();
                            String ext = ".jpg";  // Por defecto JPG
                            if (contentType != null && contentType.equals("image/png")) {
                                ext = ".png";
                            }
                            // Nombro el archivo con el ID del usuario
                            String avatarName = "avatar" + idUsuario + ext;
                            String avatarFilePath = avatarBasePath + File.separator + avatarName;
                            // Guardo el archivo en el servidor
                            avatarPart.write(avatarFilePath);
                            u.setAvatar(avatarName);
                            needsUpdate = true;
                        }

                        // Proceso el carnet de conducir si el usuario ha subido uno
                        if (carnePart != null && carnePart.getSize() > 0) {
                            String contentType2 = carnePart.getContentType();
                            if (contentType2 != null && (contentType2.equals("image/png") || contentType2.equals("image/jpeg"))) {
                                String ext2 = contentType2.equals("image/png") ? ".png" : ".jpg";

                                // Nombro el archivo con el ID del usuario
                                String carnetName = "carnet" + idUsuario + ext2;
                                String carnetFilePath = carnetBasePath + File.separator + carnetName;
                                // Guardo el archivo en el servidor
                                carnePart.write(carnetFilePath);

                                // Guardo el nombre del archivo en la base de datos
                                u.setCarneConducir(carnetName);
                                needsUpdate = true;
                            }
                        }

                        // Si he subido archivos, actualizo el registro en la BD
                        if (needsUpdate) {
                            udao.updateAvatarCarnet(u);
                        }

                        // Marco que el usuario se ha creado correctamente
                        req.setAttribute("usuarioCreado", true);
                        mensaje = null; 
                    }
                }
            }
        }

        // Si hay algún mensaje de error, lo paso a la vista
        if (mensaje != null) {
            req.setAttribute("mensajeError", mensaje);
        }
        
        req.getRequestDispatcher(url).forward(req, resp);
    }
}
