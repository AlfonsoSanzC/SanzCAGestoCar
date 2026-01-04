<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Dar de Baja Veh&iacute;culos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <meta name="contexto" content="${contexto}">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:import url="../INC/nav.jsp"/>
        <main class="flex-grow-1">
            <div class="container py-4">
                <h1>Dar de Baja Veh&iacute;culos</h1>
                <p>Selecciona los veh&iacute;culos que deseas dar de baja</p>

                <c:choose>
                    <c:when test="${empty vehiculos}">
                        <div class="alert alert-info">
                            <p>No tienes veh&iacute;culos activos</p>
                            <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post">
                                <button type="submit" class="btn btn-secondary">Volver al Men&uacute;</button>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="mb-3 d-flex gap-2 align-items-center">
                            <button type="button" id="selectAll" class="btn btn-primary btn-sm">Seleccionar Todos</button>
                            <button type="button" id="deselectAll" class="btn btn-outline-primary btn-sm">Deseleccionar Todos</button>
                            <span id="selectedCount" class="ms-2">0 veh&iacute;culos seleccionados</span>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-bordered align-middle" id="tablaVehiculos">
                                <thead class="table-light">
                                    <tr>
                                        <th style="width: 60px; text-align: center;">Seleccionar</th>
                                        <th>Marca</th>
                                        <th>Modelo</th>
                                        <th style="text-align: center;">Matr&iacute;cula</th>
                                        <th style="text-align: center;">Motor</th>
                                        <th style="text-align: right;">Cilindrada</th>
                                        <th style="text-align: center;">Fecha Compra</th>
                                        <th style="text-align: right;">Precio</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="vehiculo" items="${vehiculos}">
                                        <tr>
                                            <td style="text-align: center;">
                                                <input type="checkbox" class="form-check-input vehiculo-check" style="width: 20px; height: 20px; margin: 0 auto; display: block;" value="${vehiculo.idVehiculo}" id="vehiculo_${vehiculo.idVehiculo}">
                                            </td>
                                            <td>${vehiculo.marca}</td>
                                            <td>${vehiculo.modelo}</td>
                                            <td style="text-align: center;">${vehiculo.matricula}</td>
                                            <td style="text-align: center;">
                                                <c:choose>
                                                    <c:when test="${vehiculo.motor == 'ELECTRICO'}">El&eacute;ctrico</c:when>
                                                    <c:when test="${vehiculo.motor == 'GASOLINA'}">Gasolina</c:when>
                                                    <c:when test="${vehiculo.motor == 'GASOIL'}">Gasoil</c:when>
                                                    <c:otherwise>${vehiculo.motor}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align: right;">${vehiculo.cilindrada} cc</td>
                                            <td style="text-align: center;"><fmt:formatDate value="${vehiculo.fechaCompra}" pattern="dd/MM/yyyy"/></td>
                                            <td style="text-align: right;"><fmt:formatNumber value="${vehiculo.precioCompra}" type="currency" currencySymbol="€"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="mt-4 d-flex gap-2">
                            <button type="button" id="eliminarSeleccionados" class="btn btn-danger" disabled data-bs-toggle="modal" data-bs-target="#confirmModal">Dar de Baja Seleccionados</button>
                            <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                                <button type="submit" class="btn btn-secondary">Volver al Men&uacute;</button>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <!-- Modal Bootstrap -->
        <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="confirmModalLabel">Confirmar Baja</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
              </div>
              <div class="modal-body">
                &iquest;Est&aacute;s seguro de que deseas dar de baja <span id="cantidadVehiculos">0</span> veh&iacute;culos?
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" id="confirmarBtn">Confirmar</button>
              </div>
            </div>
          </div>
        </div>

        <div id="mensajeDiv" class="mensaje"></div>
        <c:import url="../INC/footer.inc"/>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${contexto}/JS/eliminarVehiculos.js"></script>
    </body>
</html> 