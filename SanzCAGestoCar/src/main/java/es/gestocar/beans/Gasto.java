package es.gestocar.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author alfon
 */
public class Gasto implements Serializable{
    
    private Short idGasto;
    private Short idVehiculo;
    private Vehiculo vehiculo; 
    private String concepto;
    private Date fechaGasto;
    private String descripcion;
    private Double importe;
    private String establecimiento;
    private String kilometros;

    public Short getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Short idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Short getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Short idGasto) {
        this.idGasto = idGasto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Date getFechaGasto() {
        return fechaGasto;
    }

    public void setFechaGasto(Date fechaGasto) {
        this.fechaGasto = fechaGasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getKilometros() {
        return kilometros;
    }

    public void setKilometros(String kilometros) {
        this.kilometros = kilometros;
    }
    
    
 
}
