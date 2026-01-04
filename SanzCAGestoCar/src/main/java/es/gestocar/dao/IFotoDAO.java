package es.gestocar.dao;

import es.gestocar.beans.Foto;
import java.util.List;

/**
 *
 * @author alfon
 */
public interface IFotoDAO {

    /**
     * Obtiene las fotos cuyo id está dentro de un rango de valores
     *
     * @param idVehiculo
     * @return Lista de objetos fotos
     */
    public List<Foto> getFotosByVehiculoId(Short idVehiculo);

    
    /**
     * 
     * @param foto
     * @return id de foto
     */
    public Short insertAndGetId(Foto foto);
    /**
     *
     * @param foto
     */
 
    public void update(Foto foto);
       /**
     * 
     * 
     * @param foto 
     */
    public void delete(Foto foto);

    /**
     * Abandona el hilo de la conexión a la base de datos
     */
    public void closeConnection();

}
