package es.gestocar.dao;

import es.gestocar.beans.Gasto;
import java.util.List;

/**
 *
 * @author alfon
 */
public interface IGastoDAO {

    /**
     *
     * @param gasto
     * @return
     */
    public Boolean add(Gasto gasto);
    /**
     * 
     * @param usuarioId
     * @return 
     */
    public Double getTotalGastosByUsuarioId(int usuarioId);
    /**
     * 
     * @param vehiculoId
     * @return 
     */
     public List<Gasto> getGastosByVehiculoId(int vehiculoId);
 
     /**
      * Obtiene gastos paginados de un vehículo ordenados por fecha descendente
      * @param idVehiculo ID del vehículo
      * @param offset Número de registros a saltar
      * @param limit Número máximo de registros a devolver
      * @return Lista de gastos paginados
      */
       public List<Gasto> obtenerGastosPorVehiculoPaginados(int idVehiculo, int offset, int limit);
       
       /**
        * Cuenta el total de gastos de un vehículo
        * @param idVehiculo ID del vehículo
        * @return Número total de gastos
        */
       public int contarGastosPorVehiculo(int idVehiculo);
       
       /**
        * Obtiene lista de conceptos únicos ordenados alfabéticamente del usuario
        * @param usuarioId ID del usuario
        * @return Lista de conceptos únicos
        */
       public List<String> getConceptosUnicos(int usuarioId);
       
       /**
        * Obtiene gastos filtrados por concepto del usuario
        * @param concepto Concepto a filtrar
        * @param usuarioId ID del usuario
        * @return Lista de gastos del concepto
        */
       public List<Gasto> getGastosByConcepto(String concepto, int usuarioId);
       
       /**
        * Obtiene años únicos de los gastos del usuario
        * @param usuarioId ID del usuario
        * @return Lista de años únicos
        */
       public List<Integer> getAnosUnicos(int usuarioId);
       
       /**
        * Obtiene gastos filtrados por año del usuario
        * @param ano Año a filtrar
        * @param usuarioId ID del usuario
        * @return Lista de gastos del año
        */
       public List<Gasto> getGastosByAno(int ano, int usuarioId);
       

       
       /**
        * Busca gastos por múltiples criterios
        * @param usuarioId ID del usuario
        * @param fechaDesde Fecha desde 
        * @param fechaHasta Fecha hasta 
        * @param concepto Concepto
        * @param descripcion Descripción parcial
        * @param establecimiento Establecimiento 
        * @param importeMin Importe mínimo 
        * @param importeMax Importe máximo 
        * @return Lista de gastos que cumplen los criterios
        */
       public List<Gasto> buscarGastos(int usuarioId, java.util.Date fechaDesde, java.util.Date fechaHasta,   String concepto, String descripcion, String establecimiento, Double importeMin, Double importeMax);
       
       /**
        * Obtiene un gasto por su ID
        * @param idGasto ID del gasto
        * @return Gasto encontrado o null
        */
       public Gasto getGastoById(int idGasto);
       
       /**
        * Actualiza un gasto existente
        * @param gasto Gasto con los datos actualizados
        * @return true si se actualizó correctamente
        */
       public Boolean updateGasto(Gasto gasto);
       
       /**
        * Elimina un gasto por su ID
        * @param idGasto ID del gasto a eliminar
        * @return true si se eliminó correctamente
        */
       public Boolean deleteGasto(int idGasto);
       
       /**
        * Cierra la conexión a la base de datos
        */
    public void closeConnection();

    /**
     * Obtiene gastos filtrados por concepto y año del usuario
     * @param concepto Concepto a filtrar
     * @param ano Año a filtrar
     * @param usuarioId ID del usuario
     * @return Lista de gastos del concepto y año
     */
    public List<Gasto> getGastosByConceptoYAno(String concepto, int ano, int usuarioId);
    
    /**
     * Obtiene gastos que superan un importe del usuario
     * @param importe Importe mínimo
     * @param usuarioId ID del usuario
     * @return Lista de gastos que superan el importe
     */
    public List<Gasto> getGastosSuperanImporte(double importe, int usuarioId);

}
