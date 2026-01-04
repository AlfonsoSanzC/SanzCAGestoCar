package es.gestocar.dao;

import es.gestocar.beans.Usuario;
import java.util.List;

/**
 *
 * @author alfon
 */
public interface IUsuarioDAO {

    /**
     * Añade un nuevo usuario
     * @param usuario Objeto que recibe como parámetro con los valores a añadir
     */
    public void add(Usuario usuario);

    /**
     * Obtiene todos los usuarios
     * @return Lista de objetos usuario
     */
    public List<Usuario> getUsuarios();

    /**
     * Obtiene un objeto usuario a partir de su id
     * @param id Identificativo del usuario que se pretende obtener
     * @return Objeto usuario
     */
    public Usuario getUsuarioById(int id);

    /**
     * Actualiza un usuario
     * @param usuario Objeto que recibe como parámetro con los valores para actualizar
     */
    public void update(Usuario usuario);

    /**
     * Elimina un usuario
     * @param usuario Objeto usuario que se pretende eliminar
     */
    public void delete(Usuario usuario);

    /**
     * Actualiza el estado de campoBaja de un usuario
     * @param idUsuario identificador del usuario
     * @param estado true para dar de baja, false para activar
     * @return éxito de la operación
     */
    public boolean updateUsuarioCampoBaja(int idUsuario, boolean estado);

    /**
     * Obtiene un usuario a partir de su email
     * @param email
     * @return Objeto usuario
     */
    public Usuario getUsuarioByEmail(String email);

    /**
     * Comprueba si existe un email en la base de datos
     * @param email
     * @return true si existe, false si no
     */
    public boolean emailExiste(String email);
    
    /**
     * Devuelve el usuario cuyo email y hash de contraseña coinciden
     * y que no esté dado de baja (campobaja='F').
     *
     * @param email        correo del usuario
     * @param passwordHash contraseña ya codificada en MD5
     * @return el bean Usuario si las credenciales son válidas, o null si no lo son
     */
    Usuario login(String email, String passwordHash);
    
    /**
     * Actualiza sólo los nombres de archivo de avatar y carnet.
     * @param usuario bean con idUsuario, avatar y carneConducir ya poblados
     */
    void updateAvatarCarnet(Usuario usuario);

    /**
     * Abandona el hilo de la conexión a la base de datos
     */
    public void closeConnection();
}
