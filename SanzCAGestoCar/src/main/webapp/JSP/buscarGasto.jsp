<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Buscar Gastos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:import url="../INC/nav.jsp"/>
        <main class="flex-grow-1">
            <div class="container py-4">
                <h1 class="mb-4">Buscar y Gestionar Gastos</h1>
                
                <!-- Mensajes -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <c:if test="${not empty exito}">
                    <div class="alert alert-success">${exito}</div>
                </c:if>
                <c:if test="${not empty mensaje}">
                    <div class="alert alert-info">${mensaje}</div>
                </c:if>

                <!-- Formulario de B&uacute;squeda -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">Criterios de B&uacute;squeda</h5>
                    </div>
                    <div class="card-body">
                        <form action="${contexto}/BuscarGastoController" method="post">
                            <input type="hidden" name="accion" value="buscar">
                            <div class="row">
                                <!-- Fechas -->
                                <div class="col-md-3 mb-3">
                                    <label class="form-label">Fecha Desde</label>
                                    <input type="date" name="fechaDesde" class="form-control" value="${fechaDesde}">
                                </div>
                                <div class="col-md-3 mb-3">
                                    <label class="form-label">Fecha Hasta</label>
                                    <input type="date" name="fechaHasta" class="form-control" value="${fechaHasta}">
                                </div>
                                
                                <!-- Concepto -->
                                <div class="col-md-3 mb-3">
                                    <label class="form-label">Concepto</label>
                                    <input type="text" name="concepto" class="form-control" 
                                           list="conceptos-list" value="${conceptoBusqueda}" 
                                           placeholder="Buscar por concepto">
                                    <datalist id="conceptos-list">
                                        <c:forEach var="concepto" items="${conceptos}">
                                            <option value="${concepto}">
                                        </c:forEach>
                                    </datalist>
                                </div>
                                
                                <!-- Descripci&oacute;n -->
                                <div class="col-md-3 mb-3">
                                    <label class="form-label">Descripci&oacute;n</label>
                                    <input type="text" name="descripcion" class="form-control" 
                                           value="${descripcionBusqueda}" placeholder="Buscar en descripci&oacute;n">
                                </div>
                                
                                <!-- Establecimiento -->
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">Establecimiento</label>
                                    <input type="text" name="establecimiento" class="form-control" 
                                           value="${establecimientoBusqueda}" placeholder="Buscar por establecimiento">
                                </div>
                                
                                <!-- Importes -->
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">Importe M&iacute;nimo (&euro;)</label>
                                    <input type="number" name="importeMin" class="form-control" 
                                           step="0.01" min="0" value="${importeMin}">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label">Importe M&aacute;ximo (&euro;)</label>
                                    <input type="number" name="importeMax" class="form-control" 
                                           step="0.01" min="0" value="${importeMax}">
                                </div>
                            </div>
                            
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Buscar Gastos
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Resultados de B&uacute;squeda -->
                <c:if test="${not empty gastos}">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">Resultados (${gastos.size()} gastos encontrados)</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Fecha</th>
                                            <th>Concepto</th>
                                            <th>Descripci&oacute;n</th>
                                            <th>Importe</th>
                                            <th>Establecimiento</th>
                                            <th>Km</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="gasto" items="${gastos}">
                                            <tr>
                                                <td><fmt:formatDate value="${gasto.fechaGasto}" pattern="dd/MM/yyyy"/></td>
                                                <td>${gasto.concepto}</td>
                                                <td>${gasto.descripcion}</td>
                                                <td><fmt:formatNumber value="${gasto.importe}" type="currency" currencySymbol="&euro;"/></td>
                                                <td>${gasto.establecimiento}</td>
                                                <td>${gasto.kilometros}</td>
                                                <td>
                                                    <form action="${contexto}/BuscarGastoController" method="post" style="display: inline;">
                                                        <input type="hidden" name="accion" value="cargarModificar">
                                                        <input type="hidden" name="idGasto" value="${gasto.idGasto}">
                                                        <button type="submit" class="btn btn-sm btn-warning" title="Modificar">
                                                            <i class="fas fa-edit"></i>
                                                        </button>
                                                    </form>
                                                    <button type="button" class="btn btn-sm btn-danger" 
                                                            onclick="confirmarEliminacion(${gasto.idGasto}, '${gasto.concepto}')" 
                                                            title="Eliminar">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Formulario de Modificaci&oacute;n -->
                <c:if test="${not empty gastoModificar}">
                    <div class="card mt-4">
                        <div class="card-header">
                            <h5 class="mb-0">Modificar Gasto</h5>
                        </div>
                        <div class="card-body">
                            <form action="${contexto}/BuscarGastoController" method="post">
                                <input type="hidden" name="accion" value="modificar">
                                <input type="hidden" name="idGasto" value="${gastoModificar.idGasto}">
                                
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Concepto <span class="text-danger">*</span></label>
                                        <input type="text" name="concepto" class="form-control" 
                                               value="${gastoModificar.concepto}" maxlength="40">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Fecha <span class="text-danger">*</span></label>
                                        <input type="date" name="fechaGasto" class="form-control" 
                                               value="<fmt:formatDate value='${gastoModificar.fechaGasto}' pattern='yyyy-MM-dd'/>">
                                    </div>
                                    <div class="col-md-12 mb-3">
                                        <label class="form-label">Descripci&oacute;n <span class="text-danger">*</span></label>
                                        <textarea name="descripcion" class="form-control" rows="2" 
                                                  maxlength="100">${gastoModificar.descripcion}</textarea>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Importe (€) <span class="text-danger">*</span></label>
                                        <input type="number" name="importe" class="form-control" 
                                               step="0.01" min="0.01" value="${gastoModificar.importe}">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Establecimiento</label>
                                        <input type="text" name="establecimiento" class="form-control" 
                                               value="${gastoModificar.establecimiento}" maxlength="100">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Kil&oacute;metros</label>
                                        <input type="text" name="kilometros" class="form-control" 
                                               value="${gastoModificar.kilometros}" maxlength="7">
                                    </div>
                                </div>
                                
                                <div class="text-center">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-save"></i> Guardar Cambios
                                    </button>
                                    <button type="submit" name="accion" value="cancelar" class="btn btn-secondary">
                                        <i class="fas fa-times"></i> Cancelar
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:if>

                <!-- Bot&oacute;n volver -->
                <div class="mt-4 text-center">
                    <form action="${contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Volver al Men&uacute;
                        </button>
                    </form>
                </div>
            </div>
        </main>

        <!-- Modal de Confirmaci&oacute;n de Eliminaci&oacute;n -->
        <div class="modal fade" id="modalEliminar" tabindex="-1" aria-labelledby="modalEliminarLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalEliminarLabel">Confirmar Eliminaci&oacute;n</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>&iquest;Est&aacute;s seguro de que deseas eliminar este gasto?</p>
                        <p><strong>Concepto:</strong> <span id="conceptoEliminar"></span></p>
                        <p class="text-danger"><small>Esta acci&oacute;n no se puede deshacer.</small></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <form id="formEliminar" action="${contexto}/BuscarGastoController" method="post" style="display: inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="idGasto" id="idGastoEliminar">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i> Eliminar
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <c:import url="../INC/footer.inc"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function confirmarEliminacion(idGasto, concepto) {
                document.getElementById('idGastoEliminar').value = idGasto;
                document.getElementById('conceptoEliminar').textContent = concepto;
                new bootstrap.Modal(document.getElementById('modalEliminar')).show();
            }
        </script>
    </body>
</html> 