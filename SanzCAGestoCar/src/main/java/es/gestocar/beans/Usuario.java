package es.gestocar.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author alfon
 */
public class Usuario implements Serializable{
    
    private Short idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String dni;
    private Boolean campoBaja;
    private String avatar;
    private String carneConducir;
    private List<Vehiculo> vehiculos;

    public Short getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Short idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }



    public Boolean getCampoBaja() {
        return campoBaja;
    }

    public void setCampoBaja(Boolean campoBaja) {
        this.campoBaja = campoBaja;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCarneConducir() {
        return carneConducir;
    }

    public void setCarneConducir(String carneConducir) {
        this.carneConducir = carneConducir;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    
}
