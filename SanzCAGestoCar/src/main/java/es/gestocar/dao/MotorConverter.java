
package es.gestocar.dao;

import es.gestocar.beans.Vehiculo;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * 
 * Convierte valores String a enum Motor 
 * 
 * Este converter se utiliza para transformar los valores de motor almacenados en la base de datos
 * (que pueden estar en diferentes formatos como "gasolina", "GASOLINA", etc.) al enum Motor
 * correspondiente (GASOLINA, GASOIL, ELECTRICO).
 * 
 * @author alfon
 */
public class MotorConverter implements Converter {
    
    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            String strValue = (String) value;
            try {
                return Vehiculo.Motor.valueOf(strValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ConversionException("Invalid value for Motor: " + strValue);
            }
        }

        throw new ConversionException("Can't convert value '" + value + "' to type " + type);
    }
}
