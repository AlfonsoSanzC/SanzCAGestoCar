package es.gestocar.daofactory;

import es.gestocar.dao.FotoDAO;
import es.gestocar.dao.GastoDAO;
import es.gestocar.dao.IFotoDAO;
import es.gestocar.dao.IGastoDAO;
import es.gestocar.dao.IUsuarioDAO;
import es.gestocar.dao.IVehiculoDAO;
import es.gestocar.dao.UsuarioDAO;
import es.gestocar.dao.VehiculoDAO;

/**
 *
 * @author alfon
 */
public class MysqlDAOFactory extends DAOFactory {

    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    @Override
    public IVehiculoDAO getVehiculoDAO() {
        return new VehiculoDAO();
    }

    @Override
    public IFotoDAO getFotoDAO() {
        return new FotoDAO(); 
    }

    @Override
    public IGastoDAO getGastoDAO() {
        return new GastoDAO();
    }

}
