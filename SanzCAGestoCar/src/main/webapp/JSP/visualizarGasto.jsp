<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Visualizar Gastos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <meta name="contexto" content="${contexto}">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:import url="../INC/nav.jsp"/>
        <main class="flex-grow-1">
            <div class="container py-4">
                <h1>Visualizar Gastos</h1>
                <p>Selecciona un veh&iacute;culo para ver sus gastos ordenados por fecha</p>

                <!-- Mostrar mensajes de error -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <!-- Formulario para seleccionar veh&iacute;culo -->
                <form action="${contexto}/VisualizarGastoController" method="post">
                    <div class="mb-4">
                        <label for="selectVehiculo" class="form-label">Seleccionar Veh&iacute;culo:</label>
                        <div class="d-flex gap-2" style="max-width: 600px;">
                            <select name="idVehiculo" id="selectVehiculo" class="form-select">
                                <option value="">-- Selecciona un veh&iacute;culo --</option>
                                <c:forEach var="vehiculo" items="${vehiculos}">
                                    <option value="${vehiculo.idVehiculo}" 
                                            <c:if test="${vehiculo.idVehiculo == vehiculoSeleccionado}">selected</c:if>>
                                        ${vehiculo.marca} ${vehiculo.modelo} - ${vehiculo.matricula}
                                        <c:if test="${vehiculo.fechaVenta != null}"> (Vendido)</c:if>
                                    </option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-primary">Ver Gastos</button>
                        </div>
                    </div>
                </form>

                <!-- Mostrar gastos si hay veh&iacute;culo seleccionado -->
                <c:if test="${not empty gastos}">
                    <div class="mt-4">
                        <h3>Gastos de ${nombreVehiculo}</h3>
                        <p class="text-muted">Total de gastos: ${gastos.size()}</p>
                        
                        <div class="table-responsive">
                            <table class="table table-bordered align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Fecha</th>
                                        <th>Concepto</th>
                                        <th>Descripci&oacute;n</th>
                                        <th style="text-align: right;">Importe</th>
                                        <th>Establecimiento</th>
                                        <th>Kil&oacute;metros</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="gasto" items="${gastos}">
                                        <tr>
                                            <td><fmt:formatDate value="${gasto.fechaGasto}" pattern="dd/MM/yyyy"/></td>
                                            <td>${gasto.concepto}</td>
                                            <td>${gasto.descripcion}</td>
                                            <td style="text-align: right;">
                                                <fmt:formatNumber value="${gasto.importe}" type="currency" currencySymbol="€"/>
                                            </td>
                                            <td>${gasto.establecimiento}</td>
                                            <td>${gasto.kilometros}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <!-- Mensaje cuando se selecciona veh&iacute;culo pero no hay gastos -->
                <c:if test="${not empty vehiculoSeleccionado and empty gastos}">
                    <div class="alert alert-info mt-4">
                        <p>El veh&iacute;culo seleccionado no tiene gastos registrados.</p>
                    </div>
                </c:if>

                <!-- Bot&oacute;n volver -->
                <div class="mt-4">
                    <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-secondary">Volver al Men&uacute;</button>
                    </form>
                </div>
            </div>
        </main>

        <c:import url="../INC/footer.inc"/>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 