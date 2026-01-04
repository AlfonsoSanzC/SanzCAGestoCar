package es.gestocar.daofactory; 

import es.gestocar.dao.IFotoDAO;
import es.gestocar.dao.IGastoDAO;
import es.gestocar.dao.IUsuarioDAO;
import es.gestocar.dao.IVehiculoDAO;

public abstract class DAOFactory {

    /**
     * Una clase abstracta por cada tabla de la base de datos
     * @return Interfaz de las operaciones a realizar con la tabla
     */
    
      /**
     * Objeto DAO de Usuario
     * @return interface de dicho objeto DAO
     */
    public abstract IUsuarioDAO getUsuarioDAO();
    /**
     * Objeto DAO de Vehiculo
     * @return interface de dicho objeto DAO
     */
    public abstract IVehiculoDAO getVehiculoDAO();
    /**
     * Objeto DAO de Foto
     * @return interface de dicho objeto DAO
     */
    public abstract IFotoDAO getFotoDAO();
     /**
     * Objeto DAO de Gasto
     * @return interface de dicho objeto DAO
     */
    public abstract IGastoDAO getGastoDAO();


    /**
     * Fábrica abstracta
     * @return Objeto de la fábrica abstracta
     */
    public static DAOFactory getDAOFactory() {
        DAOFactory daof = null;
        daof = new MysqlDAOFactory();
        return daof;
    }
}
