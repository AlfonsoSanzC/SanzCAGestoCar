package es.gestocar.dao;

import es.gestocar.beans.Vehiculo;
import es.gestocar.connections.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @autor alfon
 */
public class VehiculoDAO implements IVehiculoDAO {

    private Connection conexion;
    private MotorConverter motorConverter = new MotorConverter();

    @Override
    public Boolean add(Vehiculo vehiculo) {
        Boolean retorno = true;
        String sql = "INSERT INTO vehiculos (idusuario, marca, modelo, motor, matricula, cilindrada, caballos, color, fechacompra, fechaventa, preciocompra, precioventa)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setShort(1, vehiculo.getUsuarioId());
            preparada.setString(2, vehiculo.getMarca());
            preparada.setString(3, vehiculo.getModelo());
            preparada.setString(4, vehiculo.getMotor() != null ? vehiculo.getMotor().name() : null);
            preparada.setString(5, vehiculo.getMatricula());
            preparada.setString(6, vehiculo.getCilindrada());
            preparada.setString(7, vehiculo.getCaballos());
            preparada.setString(8, vehiculo.getColor());
            preparada.setDate(9, vehiculo.getFechaCompra() != null ? new Date(vehiculo.getFechaCompra().getTime()) : null);
            preparada.setDate(10, vehiculo.getFechaVenta() != null ? new Date(vehiculo.getFechaVenta().getTime()) : null);
            preparada.setDouble(11, vehiculo.getPrecioCompra() != null ? vehiculo.getPrecioCompra() : 0.0);
            preparada.setDouble(12, vehiculo.getPrecioVenta() != null ? vehiculo.getPrecioVenta() : 0.0);

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
    public List<Vehiculo> getVehiculosByUsuarioId(Short usuarioId) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE idusuario = ?";

        PreparedStatement preparada;
        try {
        conexion = ConnectionFactory.getConnection();
        conexion.setAutoCommit(false);

            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, usuarioId);
            ResultSet rs = preparada.executeQuery();
            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getShort("idvehiculo"));
                vehiculo.setUsuarioId(rs.getShort("idusuario"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                String motorStr = rs.getString("motor");
                if (motorStr != null) {
                    vehiculo.setMotor(Vehiculo.Motor.valueOf(motorStr));
                }
                vehiculo.setMatricula(rs.getString("matricula"));
                vehiculo.setCilindrada(rs.getString("cilindrada"));
                vehiculo.setCaballos(rs.getString("caballos"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setFechaCompra(rs.getDate("fechacompra"));
                vehiculo.setFechaVenta(rs.getDate("fechaventa"));
                vehiculo.setPrecioCompra(rs.getDouble("preciocompra"));
                vehiculo.setPrecioVenta(rs.getDouble("precioventa"));
                vehiculos.add(vehiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return vehiculos;
    }

    @Override
    public void update(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET marca=?, modelo=?, motor=?, matricula=?, cilindrada=?, caballos=?, color=?, fechacompra=?, fechaventa=?, preciocompra=?, precioventa=? " + "WHERE idvehiculo=?";

        PreparedStatement preparada;
        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            preparada = conexion.prepareStatement(sql);

            preparada.setString(1, vehiculo.getMarca());
            preparada.setString(2, vehiculo.getModelo());
            preparada.setString(3, vehiculo.getMotor() != null ? vehiculo.getMotor().name() : null);
            preparada.setString(4, vehiculo.getMatricula());
            preparada.setString(5, vehiculo.getCilindrada());
            preparada.setString(6, vehiculo.getCaballos());
            preparada.setString(7, vehiculo.getColor());
            preparada.setDate(8, vehiculo.getFechaCompra() != null ? new Date(vehiculo.getFechaCompra().getTime()) : null);
            preparada.setDate(9, vehiculo.getFechaVenta() != null ? new Date(vehiculo.getFechaVenta().getTime()) : null);
            preparada.setDouble(10, vehiculo.getPrecioCompra() != null ? vehiculo.getPrecioCompra() : 0.0);
            preparada.setDouble(11, vehiculo.getPrecioVenta() != null ? vehiculo.getPrecioVenta() : 0.0);
            preparada.setShort(12, vehiculo.getIdVehiculo());

            preparada.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

    }



    @Override
    public List<Vehiculo> getVehiculos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT idvehiculo, idusuario, marca, modelo, motor, matricula, cilindrada, caballos, color, fechacompra, fechaventa, preciocompra, precioventa FROM vehiculos";

        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            ResultSet rs = preparada.executeQuery();
            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getShort("idvehiculo"));
                vehiculo.setUsuarioId(rs.getShort("idusuario"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                String motorStr = rs.getString("motor");
                try {
                    vehiculo.setMotor((Vehiculo.Motor) motorConverter.convert(Vehiculo.Motor.class, motorStr));
                } catch (Exception ex) {
                    vehiculo.setMotor(null);
                }
                vehiculo.setMatricula(rs.getString("matricula"));
                vehiculo.setCilindrada(rs.getString("cilindrada"));
                vehiculo.setCaballos(rs.getString("caballos"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setFechaCompra(rs.getDate("fechacompra"));
                vehiculo.setFechaVenta(rs.getDate("fechaventa"));
                vehiculo.setPrecioCompra(rs.getDouble("preciocompra"));
                vehiculo.setPrecioVenta(rs.getDouble("precioventa"));

                vehiculos.add(vehiculo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return vehiculos;
    }

    @Override
    public int getLastInsertedId() {
        int id = 0;
        String sql = "SELECT LAST_INSERT_ID()";
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            ResultSet rs = preparada.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return id;
    }

    @Override
    public boolean matriculaExiste(String matricula) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM vehiculos WHERE matricula = ?";
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, matricula);
            ResultSet rs = preparada.executeQuery();
            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return existe;
    }

    @Override
    public List<Vehiculo> getVehiculosActivosByUsuarioId(Short usuarioId) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE idusuario = ? AND fechaventa IS NULL";

        PreparedStatement preparada;
        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, usuarioId);
            ResultSet rs = preparada.executeQuery();
            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getShort("idvehiculo"));
                vehiculo.setUsuarioId(rs.getShort("idusuario"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                String motorStr = rs.getString("motor");
                if (motorStr != null) {
                    vehiculo.setMotor(Vehiculo.Motor.valueOf(motorStr));
                }
                vehiculo.setMatricula(rs.getString("matricula"));
                vehiculo.setCilindrada(rs.getString("cilindrada"));
                vehiculo.setCaballos(rs.getString("caballos"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setFechaCompra(rs.getDate("fechacompra"));
                vehiculo.setFechaVenta(rs.getDate("fechaventa"));
                vehiculo.setPrecioCompra(rs.getDouble("preciocompra"));
                vehiculo.setPrecioVenta(rs.getDouble("precioventa"));
                vehiculos.add(vehiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return vehiculos;
    }

    @Override
    public boolean eliminarVehiculosLogico(Short[] vehiculosIds) {
        boolean exito = false;
        String sql = "UPDATE vehiculos SET fechaventa = CURRENT_DATE WHERE idvehiculo = ?";
        
        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement ps = conexion.prepareStatement(sql);
            
            int totalFilasAfectadas = 0;
            for (Short id : vehiculosIds) {
                ps.setShort(1, id);
                int filasAfectadas = ps.executeUpdate();
                totalFilasAfectadas += filasAfectadas;
            }
            
            if (totalFilasAfectadas > 0) {
                conexion.commit();
                exito = true;
            } else {
                conexion.rollback();
            }
            
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        
        return exito;
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
