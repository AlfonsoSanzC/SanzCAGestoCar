<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Estad&iacute;sticas Gastos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:import url="../INC/nav.jsp"/>
        <main class="flex-grow-1">
            <div class="container py-4">
                <h1 class="mb-4">Estad&iacute;sticas de Gastos</h1>
                
                <!-- Total General -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <div class="card bg-primary text-white">
                            <div class="card-body text-center">
                                <h5 class="card-title">Total General</h5>
                                <h2><fmt:formatNumber value="${totalGastos}" type="currency" currencySymbol="€"/></h2>
                                <p class="mb-0">Todos los veh&iacute;culos</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card bg-info text-white">
                            <div class="card-body">
                                <h5 class="card-title">Totales por Concepto</h5>
                                <div style="max-height: 150px; overflow-y: auto;">
                                    <c:forEach var="entry" items="${totalesPorConcepto}">
                                        <div class="d-flex justify-content-between">
                                            <span>${entry.key}:</span>
                                            <strong><fmt:formatNumber value="${entry.value}" type="currency" currencySymbol="€"/></strong>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Formularios de Consulta -->
                <div class="row">
                    <!-- Gastos por Concepto -->
                    <div class="col-md-6 mb-3">
                        <div class="card">
                            <div class="card-header"><h6>Gastos por Concepto</h6></div>
                            <div class="card-body">
                                <form action="${contexto}/TodoGastoController" method="post">
                                    <input type="hidden" name="accion" value="gastosPorConcepto">
                                    <select name="concepto" class="form-select mb-2">
                                        <option value="">-- Selecciona concepto --</option>
                                        <c:forEach var="concepto" items="${conceptos}">
                                            <option value="${concepto}" <c:if test="${concepto == conceptoSeleccionado}">selected</c:if>>${concepto}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-sm">Consultar</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Gastos por A&ntilde;o -->
                    <div class="col-md-6 mb-3">
                        <div class="card">
                            <div class="card-header"><h6>Gastos por A&ntilde;o</h6></div>
                            <div class="card-body">
                                <form action="${contexto}/TodoGastoController" method="post">
                                    <input type="hidden" name="accion" value="gastosPorAno">
                                    <select name="ano" class="form-select mb-2">
                                        <option value="">-- Selecciona a&ntilde;o --</option>
                                        <c:forEach var="ano" items="${anos}">
                                            <option value="${ano}" <c:if test="${ano == anoSeleccionado}">selected</c:if>>${ano}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-sm">Consultar</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Gastos por Concepto y A&ntilde;o -->
                    <div class="col-md-6 mb-3">
                        <div class="card">
                            <div class="card-header"><h6>Gastos por Concepto y A&ntilde;o</h6></div>
                            <div class="card-body">
                                <form action="${contexto}/TodoGastoController" method="post">
                                    <input type="hidden" name="accion" value="gastosConceptoAno">
                                    <select name="concepto" class="form-select mb-2">
                                        <option value="">-- Concepto --</option>
                                        <c:forEach var="concepto" items="${conceptos}">
                                            <option value="${concepto}" <c:if test="${concepto == conceptoAnoSeleccionado}">selected</c:if>>${concepto}</option>
                                        </c:forEach>
                                    </select>
                                    <select name="ano" class="form-select mb-2">
                                        <option value="">-- A&ntilde;o --</option>
                                        <c:forEach var="ano" items="${anos}">
                                            <option value="${ano}" <c:if test="${ano == anoConceptoSeleccionado}">selected</c:if>>${ano}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-sm">Consultar</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Gastos que superan importe -->
                    <div class="col-md-6 mb-3">
                        <div class="card">
                            <div class="card-header"><h6>Gastos > Importe</h6></div>
                            <div class="card-body">
                                <form action="${contexto}/TodoGastoController" method="post">
                                    <input type="hidden" name="accion" value="gastosSuperanImporte">
                                    <input type="number" name="importe" class="form-control mb-2" step="0.01" min="0" placeholder="Importe m&iacute;nimo" value="${importeMinimo}">
                                    <button type="submit" class="btn btn-primary btn-sm">Consultar</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Resultados -->
                <c:if test="${not empty gastosConcepto}">
                    <div class="alert alert-success">
                        <strong>Concepto "${conceptoSeleccionado}":</strong> 
                        <fmt:formatNumber value="${totalConcepto}" type="currency" currencySymbol="€"/> 
                        (${gastosConcepto.size()} gastos)
                    </div>
                </c:if>

                <c:if test="${not empty gastosAno}">
                    <div class="alert alert-warning">
                        <strong>A&ntilde;o ${anoSeleccionado}:</strong> 
                        <fmt:formatNumber value="${totalAno}" type="currency" currencySymbol="€"/> 
                        (${gastosAno.size()} gastos)
                    </div>
                </c:if>

                <c:if test="${not empty gastosConceptoAno}">
                    <div class="alert alert-info">
                        <strong>"${conceptoAnoSeleccionado}" en ${anoConceptoSeleccionado}:</strong> 
                        <fmt:formatNumber value="${totalConceptoAno}" type="currency" currencySymbol="€"/> 
                        (${gastosConceptoAno.size()} gastos)
                    </div>
                </c:if>

                <c:if test="${not empty gastosImporte}">
                    <div class="alert alert-danger">
                        <strong>Gastos > <fmt:formatNumber value="${importeMinimo}" type="currency" currencySymbol="€"/>:</strong> 
                        <fmt:formatNumber value="${totalImporte}" type="currency" currencySymbol="€"/> 
                        (${gastosImporte.size()} gastos)
                    </div>
                </c:if>

                <!-- Bot&oacute;n volver -->
                <div class="mt-4 text-center">
                    <form action="${contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-secondary">Volver al Men&uacute;</button>
                    </form>
                </div>
            </div>
        </main>

        <c:import url="../INC/footer.inc"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 