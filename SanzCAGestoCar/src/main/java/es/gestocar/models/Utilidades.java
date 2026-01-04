package es.gestocar.models;

import es.gestocar.beans.Gasto;
import es.gestocar.dao.IGastoDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author alfon
 */
public class Utilidades {

    /**
     * Convierte un String en un array de bytes necesario para almacenar una
     * imagen en binario
     *
     * @param imageName String que recibe
     * @return Array de bytes que representan la imagen
     * @throws IOException Excepción que puede lanzar
     */
    public static byte[] extractBytes(String imageName) throws IOException {

        File imgPath = new File(imageName);

        FileInputStream myStream = new FileInputStream(imgPath);

        return IOUtils.toByteArray(myStream);
    }

    /**
     * Convierte un objeto java.util.Date en un objeto java.sql.Date
     *
     * @param fecha Objeto java.util.Date
     * @return Objeto java.sql.Date
     */
    public static java.sql.Date utilDateToSqlDate(java.util.Date fecha) {
        return new java.sql.Date(fecha.getTime());
    }

    /**
     * Elimina los atributos de sesión
     *
     * @param request Objeto HttpServletRequest necesario para eliminar los
     * atributos
     */
    public static void limpiarSesion(HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") != null) {
            request.getSession().removeAttribute("usuario");
        }
        if (request.getSession().getAttribute("fotosEliminar") != null) {
            request.getSession().removeAttribute("fotosEliminar");
        }
        if (request.getSession().getAttribute("vehiculos") != null) {
            request.getSession().removeAttribute("vehiculos");
        }
    }

    /**
     * Comprueba que todos los campos del formulario estén rellenos
     *
     * @param campos Enumeration con los nombre de los controles del formulario
     * @param request Objeto HttpServletRequest necesario para obtener el valor
     * del campo
     * @return Objeto Boolean, TRUE si existe algún error y FALSE en caso
     * contrario
     */
    public static Boolean isFormCompleto(Enumeration<String> campos, HttpServletRequest request) {
        while (campos.hasMoreElements()) {
            String nombre = campos.nextElement();
            // Saltamos los campos de tipo file:
            if ("avatar".equals(nombre) || "carneconducir".equals(nombre)) {
                continue;
            }
            String valor = request.getParameter(nombre);
            if (valor == null || valor.trim().isEmpty()) {
                return Boolean.TRUE;  // ¡falta algo!
            }
        }
        return Boolean.FALSE; // todo OK
    }

    /**
     * Convierte una contraseña en texto plano a su hash MD5
     *
     * @param password Contraseña en texto plano
     * @return Hash MD5 de la contraseña en formato hexadecimal
     */
    public static String hashPassword(String password) {
        try {

            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            // Recorre cada byte del hash y lo convierte a hexadecimal
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    /**
     * Valida que los campos no estén vacíos o nulos
     *
     * @param campos Strings a validar
     * @return true si hay algún campo vacío/nulo, false si todos están
     * completos
     */
    public static boolean tieneVacios(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula el total de una lista de gastos
     *
     * @param gastos Lista de gastos
     * @return Total calculado
     */
    public static BigDecimal calcularTotalGastos(List<Gasto> gastos) {
        BigDecimal total = BigDecimal.ZERO;
        for (Gasto g : gastos) {
            total = total.add(BigDecimal.valueOf(g.getImporte()));
        }
        return total;
    }

    /**
     * Carga los datos iniciales necesarios para la página de estadísticas
     *
     * @param request Request donde se cargarán los atributos
     * @param gastoDAO DAO de gastos
     * @param usuarioId ID del usuario
     */
    public static void cargarDatosEstadisticas(HttpServletRequest request, IGastoDAO gastoDAO, int usuarioId) {
        // Total general del usuario
        Double totalGastos = gastoDAO.getTotalGastosByUsuarioId(usuarioId);
        request.setAttribute("totalGastos", totalGastos);

        // Conceptos únicos del usuario
        List<String> conceptos = gastoDAO.getConceptosUnicos(usuarioId);
        request.setAttribute("conceptos", conceptos);

        // Años únicos del usuario
        List<Integer> anos = gastoDAO.getAnosUnicos(usuarioId);
        request.setAttribute("anos", anos);

        // Totales por concepto del usuario
        Map<String, BigDecimal> totalesPorConcepto = new HashMap<>();
        for (String concepto : conceptos) {
            List<Gasto> gastos = gastoDAO.getGastosByConcepto(concepto, usuarioId);
            totalesPorConcepto.put(concepto, calcularTotalGastos(gastos));
        }
        request.setAttribute("totalesPorConcepto", totalesPorConcepto);
    }

    /**
     * Procesa la búsqueda de gastos por múltiples criterios
     *
     * @param request Request con los parámetros de búsqueda
     * @param gastoDAO DAO de gastos
     * @param usuarioId ID del usuario
     */
    public static void procesarBusquedaGastos(HttpServletRequest request, IGastoDAO gastoDAO, int usuarioId) {
        try {
            // Obtiene parámetros de búsqueda
            String fechaDesdeStr = request.getParameter("fechaDesde");
            String fechaHastaStr = request.getParameter("fechaHasta");
            String concepto = request.getParameter("concepto");
            String descripcion = request.getParameter("descripcion");
            String establecimiento = request.getParameter("establecimiento");
            String importeMinStr = request.getParameter("importeMin");
            String importeMaxStr = request.getParameter("importeMax");

            // Convierte fechas
            Date fechaDesde = null, fechaHasta = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (fechaDesdeStr != null && !fechaDesdeStr.trim().isEmpty()) {
                fechaDesde = sdf.parse(fechaDesdeStr);
            }
            if (fechaHastaStr != null && !fechaHastaStr.trim().isEmpty()) {
                fechaHasta = sdf.parse(fechaHastaStr);
            }

            // Convierte importes
            Double importeMin = null, importeMax = null;
            if (importeMinStr != null && !importeMinStr.trim().isEmpty()) {
                importeMin = Double.parseDouble(importeMinStr);
            }
            if (importeMaxStr != null && !importeMaxStr.trim().isEmpty()) {
                importeMax = Double.parseDouble(importeMaxStr);
            }

            // Realiza búsqueda
            List<Gasto> gastos = gastoDAO.buscarGastos(usuarioId, fechaDesde, fechaHasta, concepto, descripcion, establecimiento, importeMin, importeMax);

            request.setAttribute("gastos", gastos);
            request.setAttribute("fechaDesde", fechaDesdeStr);
            request.setAttribute("fechaHasta", fechaHastaStr);
            request.setAttribute("conceptoBusqueda", concepto);
            request.setAttribute("descripcionBusqueda", descripcion);
            request.setAttribute("establecimientoBusqueda", establecimiento);
            request.setAttribute("importeMin", importeMinStr);
            request.setAttribute("importeMax", importeMaxStr);

            if (gastos.isEmpty()) {
                request.setAttribute("mensaje", "No se encontraron gastos con los criterios especificados.");
            }

        } catch (ParseException | NumberFormatException ex) {
            request.setAttribute("error", "Error en los datos de búsqueda: " + ex.getMessage());
        }
    }

    /**
     * Procesa la eliminación de un gasto
     *
     * @param request Request con el ID del gasto a eliminar
     * @param gastoDAO DAO de gastos
     * @param usuarioId ID del usuario
     */
    public static void procesarEliminacionGasto(HttpServletRequest request, IGastoDAO gastoDAO, int usuarioId) {
        String idGastoStr = request.getParameter("idGasto");
        if (idGastoStr != null && !idGastoStr.trim().isEmpty()) {
            try {
                int idGasto = Integer.parseInt(idGastoStr);
                Boolean eliminado = gastoDAO.deleteGasto(idGasto);

                if (eliminado) {
                    request.setAttribute("exito", "Gasto eliminado correctamente.");
                } else {
                    request.setAttribute("error", "No se pudo eliminar el gasto.");
                }

                // Recarga búsqueda para actualizar resultados
                procesarBusquedaGastos(request, gastoDAO, usuarioId);

            } catch (NumberFormatException ex) {
                request.setAttribute("error", "ID de gasto inválido.");
            }
        }
    }

    /**
     * Carga los datos de un gasto para modificar
     *
     * @param request Request con el ID del gasto a cargar
     * @param gastoDAO DAO de gastos
     * @param usuarioId ID del usuario
     */
    public static void procesarCargarModificarGasto(HttpServletRequest request, IGastoDAO gastoDAO, int usuarioId) {
        String idGastoStr = request.getParameter("idGasto");
        if (idGastoStr != null && !idGastoStr.trim().isEmpty()) {
            try {
                int idGasto = Integer.parseInt(idGastoStr);
                Gasto gasto = gastoDAO.getGastoById(idGasto);

                if (gasto != null) {
                    request.setAttribute("gastoModificar", gasto);
                } else {
                    request.setAttribute("error", "Gasto no encontrado.");
                }

                procesarBusquedaGastos(request, gastoDAO, usuarioId);

            } catch (NumberFormatException ex) {
                request.setAttribute("error", "ID de gasto inválido.");
            }
        }
    }

    /**
     * Procesa la modificación de un gasto
     *
     * @param request Request con los datos del gasto a modificar
     * @param gastoDAO DAO de gastos
     * @param usuarioId ID del usuario
     */
    public static void procesarModificacionGasto(HttpServletRequest request, IGastoDAO gastoDAO, int usuarioId) {
        String idGastoStr = request.getParameter("idGasto");
        boolean procesarAccion = true;

        if (idGastoStr == null || idGastoStr.trim().isEmpty()) {
            procesarAccion = false;
        }

        if (procesarAccion) {
            try {
                // Valida campos obligatorios
                String concepto = request.getParameter("concepto");
                String descripcion = request.getParameter("descripcion");
                String fechaStr = request.getParameter("fechaGasto");
                String importeStr = request.getParameter("importe");

                if (tieneVacios(concepto, descripcion, fechaStr, importeStr)) {
                    request.setAttribute("error", "Todos los campos marcados son obligatorios.");
                    procesarCargarModificarGasto(request, gastoDAO, usuarioId);
                    procesarAccion = false;
                }

                if (procesarAccion) {
                    // Valida importe
                    double importe = Double.parseDouble(importeStr);
                    if (importe <= 0) {
                        request.setAttribute("error", "El importe debe ser mayor que 0.");
                        procesarCargarModificarGasto(request, gastoDAO, usuarioId);
                        procesarAccion = false;
                    } else if (importe > 9999.99) {
                        request.setAttribute("error", "El importe no puede superar 9999.99€.");
                        procesarCargarModificarGasto(request, gastoDAO, usuarioId);
                        procesarAccion = false;
                    }
                }

                if (procesarAccion) {
                    Gasto gasto = new Gasto();
                    gasto.setIdGasto(Short.parseShort(idGastoStr));
                    gasto.setConcepto(concepto);
                    gasto.setDescripcion(descripcion);
                    gasto.setFechaGasto(new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr));
                    gasto.setImporte(Double.parseDouble(importeStr));
                    gasto.setEstablecimiento(request.getParameter("establecimiento"));
                    gasto.setKilometros(request.getParameter("kilometros"));

                    Boolean actualizado = gastoDAO.updateGasto(gasto);

                    if (actualizado) {
                        request.setAttribute("exito", "Gasto modificado correctamente.");
                    } else {
                        request.setAttribute("error", "No se pudo modificar el gasto.");
                    }

                    procesarBusquedaGastos(request, gastoDAO, usuarioId);
                }

            } catch (ParseException | NumberFormatException ex) {
                request.setAttribute("error", "Error en los datos del gasto: " + ex.getMessage());
                procesarCargarModificarGasto(request, gastoDAO, usuarioId);
            }
        }
    }
}
