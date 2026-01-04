package es.gestocar.dao;

import es.gestocar.beans.Gasto;
import es.gestocar.connections.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfon
 */
public class GastoDAO implements IGastoDAO {

    private Connection conexion;

    @Override
    public Boolean add(Gasto gasto) {
        Boolean retorno = true;
        String sql = "INSERT INTO gastos (idvehiculo, concepto, fechagasto, descripcion, importe, establecimiento, kilometros)" + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, gasto.getIdVehiculo());
            preparada.setString(2, gasto.getConcepto());
            preparada.setDate(3, gasto.getFechaGasto() != null ? new java.sql.Date(gasto.getFechaGasto().getTime()) : null);
            preparada.setString(4, gasto.getDescripcion());
            preparada.setDouble(5, gasto.getImporte());
            preparada.setString(6, gasto.getEstablecimiento());
            preparada.setString(7, gasto.getKilometros());

            preparada.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        } finally {
            closeConnection();
        }
        return retorno;
    }

    @Override
    public List<Gasto> getGastosByVehiculoId(int vehiculoId) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT idvehiculo, concepto, fechagasto, descripcion, importe, establecimiento, kilometros FROM gastos WHERE idvehiculo = ?";

        PreparedStatement preparada;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, vehiculoId);
            ResultSet rs = preparada.executeQuery();
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gastos.add(gasto);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return gastos;
    }

    @Override
    public Double getTotalGastosByUsuarioId(int usuarioId) {
        Double total = 0.0;
        String sql = "SELECT SUM(importe) FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo WHERE v.idusuario = ?";

        PreparedStatement preparada = null;
        ResultSet rs = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);

            preparada.setInt(1, usuarioId);

            rs = preparada.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparada != null) {
                    preparada.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return total;
    }

    @Override
    public List<Gasto> obtenerGastosPorVehiculoPaginados(int idVehiculo, int offset, int limit) {
        List<Gasto> listaGastos = new ArrayList<>();
        String sql = "SELECT idvehiculo, concepto, fechagasto, descripcion, importe, establecimiento, kilometros "  + "FROM gastos WHERE idvehiculo = ? ORDER BY fechagasto DESC LIMIT ? OFFSET ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idVehiculo);
            preparada.setInt(2, limit);
            preparada.setInt(3, offset);

            ResultSet rs = preparada.executeQuery();
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                listaGastos.add(gasto);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return listaGastos;
    }

    @Override
    public int contarGastosPorVehiculo(int idVehiculo) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM gastos WHERE idvehiculo = ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idVehiculo);
            
            ResultSet rs = preparada.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return total;
    }

    @Override
    public List<String> getConceptosUnicos(int usuarioId) {
        List<String> conceptos = new ArrayList<>();
        String sql = "SELECT DISTINCT g.concepto FROM gastos g " + "JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " + "WHERE v.idusuario = ? ORDER BY g.concepto ASC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                conceptos.add(rs.getString("concepto"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return conceptos;
    }

    @Override
    public List<Gasto> getGastosByConcepto(String concepto, int usuarioId) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.idvehiculo, g.concepto, g.fechagasto, g.descripcion, g.importe, g.establecimiento, g.kilometros " + "FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " +"WHERE g.concepto = ? AND v.idusuario = ? ORDER BY g.fechagasto DESC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, concepto);
            preparada.setInt(2, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gastos;
    }

    @Override
    public List<Integer> getAnosUnicos(int usuarioId) {
        List<Integer> anos = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(g.fechagasto) as ano FROM gastos g " + "JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " + "WHERE v.idusuario = ? ORDER BY ano DESC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                anos.add(rs.getInt("ano"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return anos;
    }

    @Override
    public List<Gasto> getGastosByAno(int ano, int usuarioId) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.idvehiculo, g.concepto, g.fechagasto, g.descripcion, g.importe, g.establecimiento, g.kilometros " +
                    "FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " +
                    "WHERE YEAR(g.fechagasto) = ? AND v.idusuario = ? ORDER BY g.fechagasto DESC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, ano);
            preparada.setInt(2, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gastos;
    }

    @Override
    public List<Gasto> getGastosByConceptoYAno(String concepto, int ano, int usuarioId) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.idvehiculo, g.concepto, g.fechagasto, g.descripcion, g.importe, g.establecimiento, g.kilometros " + "FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " +"WHERE g.concepto = ? AND YEAR(g.fechagasto) = ? AND v.idusuario = ? ORDER BY g.fechagasto DESC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, concepto);
            preparada.setInt(2, ano);
            preparada.setInt(3, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gastos;
    }

    @Override
    public List<Gasto> getGastosSuperanImporte(double importe, int usuarioId) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.idvehiculo, g.concepto, g.fechagasto, g.descripcion, g.importe, g.establecimiento, g.kilometros " + "FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo " + "WHERE g.importe > ? AND v.idusuario = ? ORDER BY g.importe DESC";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setDouble(1, importe);
            preparada.setInt(2, usuarioId);
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gastos;
    }

    @Override
    public List<Gasto> buscarGastos(int usuarioId, java.util.Date fechaDesde, java.util.Date fechaHasta,  String concepto, String descripcion, String establecimiento, Double importeMin, Double importeMax) {
        List<Gasto> gastos = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.idgasto, g.idvehiculo, g.concepto, g.fechagasto, g.descripcion, ");
        sql.append("g.importe, g.establecimiento, g.kilometros ");
        sql.append("FROM gastos g JOIN vehiculos v ON g.idvehiculo = v.idvehiculo ");
        sql.append("WHERE v.idusuario = ?");
        
        List<Object> parametros = new ArrayList<>();
        parametros.add(usuarioId);
        
        if (fechaDesde != null) {
            sql.append(" AND g.fechagasto >= ?");
            parametros.add(new java.sql.Date(fechaDesde.getTime()));
        }
        
        if (fechaHasta != null) {
            sql.append(" AND g.fechagasto <= ?");
            parametros.add(new java.sql.Date(fechaHasta.getTime()));
        }
        
        if (concepto != null && !concepto.trim().isEmpty()) {
            sql.append(" AND g.concepto LIKE ?");
            parametros.add("%" + concepto + "%");
        }
        
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND g.descripcion LIKE ?");
            parametros.add("%" + descripcion + "%");
        }
        
        if (establecimiento != null && !establecimiento.trim().isEmpty()) {
            sql.append(" AND g.establecimiento LIKE ?");
            parametros.add("%" + establecimiento + "%");
        }
        
        if (importeMin != null) {
            sql.append(" AND g.importe >= ?");
            parametros.add(importeMin);
        }
        
        if (importeMax != null) {
            sql.append(" AND g.importe <= ?");
            parametros.add(importeMax);
        }
        
        sql.append(" ORDER BY g.fechagasto DESC");
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql.toString());
            
            for (int i = 0; i < parametros.size(); i++) {
                preparada.setObject(i + 1, parametros.get(i));
            }
            
            ResultSet rs = preparada.executeQuery();
            
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setIdGasto(rs.getShort("idgasto"));
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gastos;
    }

    @Override
    public Gasto getGastoById(int idGasto) {
        Gasto gasto = null;
        String sql = "SELECT idgasto, idvehiculo, concepto, fechagasto, descripcion, importe, establecimiento, kilometros FROM gastos WHERE idgasto = ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idGasto);
            ResultSet rs = preparada.executeQuery();
            
            if (rs.next()) {
                gasto = new Gasto();
                gasto.setIdGasto(rs.getShort("idgasto"));
                gasto.setIdVehiculo(rs.getShort("idvehiculo"));
                gasto.setConcepto(rs.getString("concepto"));
                gasto.setFechaGasto(rs.getDate("fechagasto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setImporte(rs.getDouble("importe"));
                gasto.setEstablecimiento(rs.getString("establecimiento"));
                gasto.setKilometros(rs.getString("kilometros"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return gasto;
    }

    @Override
    public Boolean updateGasto(Gasto gasto) {
        Boolean resultado = false;
        String sql = "UPDATE gastos SET concepto = ?, fechagasto = ?, descripcion = ?, importe = ?, establecimiento = ?, kilometros = ? WHERE idgasto = ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, gasto.getConcepto());
            preparada.setDate(2, new java.sql.Date(gasto.getFechaGasto().getTime()));
            preparada.setString(3, gasto.getDescripcion());
            preparada.setDouble(4, gasto.getImporte());
            preparada.setString(5, gasto.getEstablecimiento());
            preparada.setString(6, gasto.getKilometros());
            preparada.setShort(7, gasto.getIdGasto());
            
            int filasAfectadas = preparada.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return resultado;
    }

    @Override
    public Boolean deleteGasto(int idGasto) {
        Boolean resultado = false;
        String sql = "DELETE FROM gastos WHERE idgasto = ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idGasto);
            
            int filasAfectadas = preparada.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return resultado;
    }

    @Override
    public void closeConnection() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
