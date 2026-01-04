<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Modificar Veh&iacute;culo | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.css"/>
    </head>
    <body class="modificar-vehiculo min-h-screen flex flex-col">
        <c:import url="../INC/nav.jsp"/>
        
        <div class="container flex-1">
            <h1 class="page-title">
                <i class="fas fa-edit mr-3"></i>
                Modificar Veh&iacute;culo
            </h1>
            
            <div class="modificar-tabs">
                <div class="tab-buttons">
                    <button class="tab-button active" data-tab="tab-seleccionar">
                        <i class="fas fa-list mr-2"></i>
                        Seleccionar Veh&iacute;culo
                    </button>
                    <button class="tab-button" data-tab="tab-modificar" disabled>
                        <i class="fas fa-edit mr-2"></i>
                        Modificar Datos
                    </button>
                </div>
                
                <!-- TAB SELECCIONAR VEH&Iacute;CULO -->
                <div id="tab-seleccionar" class="tab-content active">
                    <div class="selection-header">
                        <h2>Selecciona el veh&iacute;culo que deseas modificar</h2>
                        <p class="text-muted">Haz clic en "Modificar" para editar los datos del veh&iacute;culo</p>
                    </div>
                    
                    <div id="vehiculos-container" class="vehiculos-grid">
                        <div class="loading-container">
                            <div class="spinner"></div>
                            <p>Cargando tus veh&iacute;culos...</p>
                        </div>
                    </div>
                </div>
                
                <!-- TAB MODIFICAR VEH&Iacute;CULO -->
                <div id="tab-modificar" class="tab-content">
                    <div class="modificar-header">
                        <h2>Modificar datos del veh&iacute;culo</h2>
                        <p class="text-muted">Actualiza los campos que desees cambiar</p>
                    </div>
                    
                    <form id="formModificarVehiculo" class="modificar-form">
                        <input type="hidden" id="idVehiculoModificar" name="idVehiculo" />
                        
                        <div class="form-grid">
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">*Marca</label>
                                    <input type="text" id="marca" name="marca" class="form-input" placeholder="Ej: Toyota"/>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">*Modelo</label>
                                    <input type="text" id="modelo" name="modelo" class="form-input" placeholder="Ej: Corolla"/>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">*Motor</label>
                                    <select id="motor" name="motor" class="form-input">
                                        <option value="GASOLINA">Gasolina</option>
                                        <option value="GASOIL">Gasoil</option>
                                        <option value="ELECTRICO">El&eacute;ctrico</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">*Cilindrada</label>
                                    <input type="text" id="cilindrada" name="cilindrada" class="form-input" placeholder="1600"/>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">*Matr&iacute;cula</label>
                                    <input type="text" id="matricula" name="matricula" class="form-input" placeholder="1234ABC" style="text-transform: uppercase;"/>
                                    <div id="matriculaValidation" class="matricula-validation"></div>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">*Caballos</label>
                                    <input type="text" id="caballos" name="caballos" class="form-input" placeholder="120"/>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">*Color</label>
                                    <input type="color" id="color" name="color" class="form-input-color"/>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">*Fecha Matriculaci&oacute;n</label>
                                    <input type="date" id="fechaCompra" name="fechaCompra" class="form-input"/>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">Fecha Venta (opcional)</label>
                                    <input type="date" id="fechaVenta" name="fechaVenta" class="form-input"/>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">*Precio Compra</label>
                                    <input type="text" id="precioCompra" name="precioCompra" class="form-input" placeholder="15000.00" />
                                    <div id="precioCompraValidation" class="price-validation"></div>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">Precio Venta (opcional)</label>
                                    <input type="text" id="precioVenta" name="precioVenta" class="form-input" placeholder="12000.00" />
                                    <div id="precioVentaValidation" class="price-validation"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div id="mensajeModificar" class="mensaje-resultado" style="display: none;"></div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn-modificar">
                                <i class="fas fa-save mr-2"></i>
                                GUARDAR CAMBIOS
                            </button>
                            <button type="button" id="btnCancelar" class="btn-cancelar">
                                <i class="fas fa-times mr-2"></i>
                                CANCELAR
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Bot&oacute;n volver -->
            <div class="back-actions">
                <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                    <button type="submit" class="btn-volver">
                        <i class="fas fa-arrow-left mr-2"></i>
                        VOLVER AL MEN&Uacute;
                    </button>
                </form>
            </div>
        </div>
        
        <c:import url="../INC/footer.inc"/>
        <input type="hidden" id="contexto" value="${applicationScope.contexto}"/>
        
        <script src="${applicationScope.contexto}/JS/modificarVehiculo.js"></script>
    </body>
</html> 