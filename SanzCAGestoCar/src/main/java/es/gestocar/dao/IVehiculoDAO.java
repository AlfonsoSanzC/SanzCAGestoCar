package es.gestocar.dao;

import es.gestocar.beans.Vehiculo;
import java.util.List;

/**
 *
 * @author alfon
 */
public interface IVehiculoDAO {

    /**
     * Añade un vehiculo
     *
     * @param vehiculo
     * @return Objeto Boolean, TRUE si todo ha ido bien y FALSE en caso
     * contrario
     */
    public Boolean add(Vehiculo vehiculo);

    /**
     * Actualiza un vehiculo
     *
     * @param vehiculo
     */
    public void update(Vehiculo vehiculo);

    /**
     *
     * @param usuarioId
     * @return
     */
    List<Vehiculo> getVehiculosByUsuarioId(Short usuarioId);

    /**
     *
     *
     * @return
     */
    public int getLastInsertedId();

    
    /**
     * Obtiene todos los vehículos
     * @return Lista de todos los vehículos
     */
    public List<Vehiculo> getVehiculos();
    
    /**
     * Verifica si existe una matrícula en la base de datos
     * @param matricula la matrícula a verificar
     * @return true si existe, false si no existe
     */
    public boolean matriculaExiste(String matricula);
    
    /**
     * Obtiene vehículos activos (sin fecha de venta) de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de vehículos activos
     */
    public List<Vehiculo> getVehiculosActivosByUsuarioId(Short usuarioId);
    
    /**
     * Eliminación lógica de vehículos (actualiza fechaventa)
     * @param vehiculosIds Array de IDs de vehículos a eliminar
     * @return true si se eliminaron correctamente
     */
    public boolean eliminarVehiculosLogico(Short[] vehiculosIds);
    
    /**
     * Abandona el hilo de la conexión a la base de datos
     */
    public void closeConnection();

}
