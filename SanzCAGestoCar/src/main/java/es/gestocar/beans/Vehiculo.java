package es.gestocar.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *
 * @author alfon
 */
public class Vehiculo implements Serializable{
    
    private Short idVehiculo;
    private Short usuarioId; 
    private String marca;
    private String modelo;
    private Vehiculo vehiculo;
    public enum Motor {GASOLINA, GASOIL, ELECTRICO}
    private String matricula; 
    private String cilindrada;
    private String caballos;
    private String color;    
    private Date fechaCompra;
    private Date fechaVenta;
    private Double precioCompra;
    private Double precioVenta;
    private List<Foto> fotos;
    private List<Gasto> gastos;
    public Motor motor;

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }


    public Short getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Short idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Short getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Short usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(String cilindrada) {
        this.cilindrada = cilindrada;
    }

    public String getCaballos() {
        return caballos;
    }

    public void setCaballos(String caballos) {
        this.caballos = caballos;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }
    
    
}