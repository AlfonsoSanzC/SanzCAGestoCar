package es.gestocar.controllers;

import es.gestocar.beans.Usuario;
import es.gestocar.dao.UsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author alfon
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recojo los datos del formulario de login
        String email = request.getParameter("email");
        String pwd = request.getParameter("password");
        String vista;

        try {
            // Creo el DAO para consultar usuarios en la base de datos
            UsuarioDAO dao = new UsuarioDAO();
            String hash;
            
            // El admin no está en la bbdd normal, tiene credenciales hardcodeadas
            if ("admin@iesalbarregas.es".equals(email) && "admin".equals(pwd)) {
                hash = DigestUtils.md5Hex("admin");
            } else {
                // Para usuarios normales, codifico la contraseña en MD5
                // Las contraseñas se guardan hasheadas en la base de datos
                hash = DigestUtils.md5Hex(pwd);
            }
            
            // Intento hacer login con el email y la contraseña hasheada
            Usuario u = dao.login(email, hash); 

            if (u == null) {
                // Si no encuentra el usuario, muestro error y vuelvo al login
                request.setAttribute("error", "E-mail o contraseña incorrectos");
                vista = "/JSP/login.jsp";

            } else {
                // Si el login es correcto, creo la sesión
                HttpSession ses = request.getSession(true);
                ses.setAttribute("usuario", u);

                // Decido a qué menú mandar al usuario según su tipo
                if ("admin@iesalbarregas.es".equalsIgnoreCase(u.getEmail())) {
                    // Si es el administrador, lo mando al menú de admin
                    vista = "/JSP/menuAdmin.jsp";
                } else {
                    // Si es un usuario normal, lo mando al menú de usuario
                    vista = "/JSP/menuUsuario.jsp";
                }
            }

        } catch (Exception ex) {
            // Si hay algún error con la base de datos, muestro error 
            request.setAttribute("error", "Error interno en la base de datos");
            vista = "/JSP/login.jsp";
        }

        request.getRequestDispatcher(vista).forward(request, response);
    }

}
