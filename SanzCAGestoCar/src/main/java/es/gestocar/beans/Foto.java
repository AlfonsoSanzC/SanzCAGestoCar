package es.gestocar.beans;

import java.io.Serializable;

/**
 *
 * @author alfon
 */
public class Foto implements Serializable{
    
    private Short idFoto;
    private byte[] foto;
    private Short idVehiculo;
    private String imagen;
    private Vehiculo vehiculo;

    public Short getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Short idFoto) {
        this.idFoto = idFoto;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Short getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Short idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    
}
