package es.gestocar.dao;

import es.gestocar.beans.Usuario;
import es.gestocar.connections.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author alfon
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public void add(Usuario usuario) {
        String sql = "INSERT INTO usuarios " + "(nombre, apellidos, email, password, dni, avatar, carneconducir) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);

            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getDni());
            ps.setString(6, usuario.getAvatar());
            ps.setString(7, usuario.getCarneConducir());

            ps.executeUpdate();

            // recupera el id generado
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setIdUsuario(rs.getShort(1));
            }

            conexion.commit();
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignore) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignore) {
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    public Usuario login(String email, String passwordHash) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND campobaja = 'F'";
        Connection conexion = null;

        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, email);
            preparada.setString(2, passwordHash);
            ResultSet resultado = preparada.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getShort("idusuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidos(resultado.getString("apellidos"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setDni(resultado.getString("dni"));
                usuario.setCampoBaja("T".equals(resultado.getString("campobaja")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }

        return usuario;
    }

    @Override
    public boolean updateUsuarioCampoBaja(int idUsuario, boolean estado) {
        boolean exito = false;
        String sql = "UPDATE usuarios SET campobaja = ? WHERE idusuario = ?";
        Connection conexion = null;
        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, estado ? "T" : "F");
            ps.setInt(2, idUsuario);

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas > 0) {
                exito = true;
                conexion.commit();
            }
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
        return exito;
    }

    @Override
    public List<Usuario> getUsuarios() {
        Connection conexion = null;
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT idusuario, nombre, apellidos, email, password, dni, campobaja, avatar, carneconducir FROM usuarios";
        try {
            conexion = ConnectionFactory.getConnection();
            Statement sentencia = conexion.createStatement();

            try (ResultSet resultado = sentencia.executeQuery(sql)) {
                while (resultado.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultado.getShort("idusuario"));
                    usuario.setNombre(resultado.getString("nombre"));
                    usuario.setApellidos(resultado.getString("apellidos"));
                    usuario.setEmail(resultado.getString("email"));
                    usuario.setPassword(resultado.getString("password"));
                    usuario.setDni(resultado.getString("dni"));
                    usuario.setCampoBaja("T".equals(resultado.getString("campobaja")));
                    usuario.setAvatar(resultado.getString("avatar"));
                    usuario.setCarneConducir(resultado.getString("carneconducir"));
                    lista.add(usuario);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
        if (lista.isEmpty()) {
            lista = null;
        }
        return lista;
    }

    @Override
    public Usuario getUsuarioById(int id) {
        Connection conexion = null;
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE idusuario=?";
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, id);
            ResultSet resultado = preparada.executeQuery();
            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getShort("idusuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidos(resultado.getString("apellidos"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setDni(resultado.getString("dni"));
                usuario.setCampoBaja("T".equals(resultado.getString("campobaja")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
        return usuario;
    }

    @Override
    public void update(Usuario usuario) {
        Connection conexion = null;
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, email=?, password=?, dni=?, campobaja=? WHERE idusuario=?";
        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, usuario.getNombre());
            preparada.setString(2, usuario.getApellidos());
            preparada.setString(3, usuario.getEmail());
            preparada.setString(4, usuario.getPassword());
            preparada.setString(5, usuario.getDni());
            preparada.setString(6, usuario.getCampoBaja() != null && usuario.getCampoBaja() ? "T" : "F");
            preparada.setShort(7, usuario.getIdUsuario());

            preparada.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
    }

    @Override
    public void delete(Usuario usuario) {
        Connection conexion = null;
        String sql = "DELETE FROM usuarios WHERE idusuario=?";
        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            PreparedStatement preparada = conexion.prepareStatement(sql);

            preparada.setShort(1, usuario.getIdUsuario());
            preparada.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email=?";
        Connection conexion = null;
        try {
            conexion = ConnectionFactory.getConnection();
            PreparedStatement preparada = conexion.prepareStatement(sql);
            preparada.setString(1, email);
            ResultSet resultado = preparada.executeQuery();
            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getShort("idusuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidos(resultado.getString("apellidos"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setDni(resultado.getString("dni"));
                usuario.setCampoBaja("T".equals(resultado.getString("campobaja")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conexion);
        }
        return usuario;
    }

    /**
     * Actualiza sólo los nombres de archivo de avatar y carnet.
     */
    @Override
    public void updateAvatarCarnet(Usuario usuario) {
        String sql = "UPDATE usuarios " + "SET avatar = ?, carneconducir = ? " + "WHERE idusuario = ?";
        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getAvatar());
            ps.setString(2, usuario.getCarneConducir());
            ps.setShort(3, usuario.getIdUsuario());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean emailExiste(String email) {
        boolean existe = false;
        String sql = "SELECT 1 FROM usuarios WHERE email = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionFactory.getConnection();
            if (con == null) {
                return false;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            existe = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ignored) {
            }
            ConnectionFactory.closeConexion();
        }
        return existe;
    }

    @Override
    public void closeConnection() {

    }

    private void closeConnection(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
