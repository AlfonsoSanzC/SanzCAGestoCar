<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.include file="../INC/metas.inc"/>
<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/>
<!DOCTYPE html>
<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"/>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Panel de Administraci&oacute;n | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${applicationScope.estilo}"/>
        </jsp:include>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.css"/>
    </head>
    <body class="menu-admin min-h-screen flex flex-col">
        <c:import url="../INC/nav.jsp"/>
        
        <div class="container flex-1">
            <h1>Panel de Administraci&oacute;n</h1>
            
            <div class="tabs">
                <div class="tab-buttons">
                    <button class="tab-button active" data-tab="tab-usuarios">Usuarios</button>
                    <button class="tab-button" data-tab="tab-vehiculos">Veh&iacute;culos</button>
                </div>
                
                <!-- TAB USUARIOS -->
                <div id="tab-usuarios" class="tab-content active">
                    <h2>Lista de Usuarios</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Avatar</th>
                                    <th>Carnet</th>
                                    <th>Nombre</th>
                                    <th>Apellidos</th>
                                    <th>Email</th>
                                    <th>DNI</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="tabla-usuarios">
                                <tr>
                                    <td colspan="9" class="loading">
                                        <div class="spinner"></div>
                                        Cargando usuarios...
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <!-- TAB VEHICULOS -->
                <div id="tab-vehiculos" class="tab-content">
                    <h2>Lista de Veh&iacute;culos</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Propietario</th>
                                    <th>Marca</th>
                                    <th>Modelo</th>
                                    <th>Motor</th>
                                    <th>Matr&iacute;cula</th>
                                    <th>Cilindrada</th>
                                    <th>Caballos</th>
                                    <th>Color</th>
                                    <th>Fecha Compra</th>
                                    <th>Fecha Venta</th>
                                    <th>Precio Compra</th>
                                    <th>Precio Venta</th>
                                </tr>
                            </thead>
                            <tbody id="tabla-vehiculos">
                                <tr>
                                    <td colspan="12" class="loading">
                                        <div class="spinner"></div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- Bot&oacute;n para cerrar sesi&oacute;n -->
            <div class="admin-actions">
                <form action="${applicationScope.contexto}/ReturnCerrarSesion" method="post" style="display: inline;">
                    <button type="submit" class="btn-salir-admin">
                        <i class="fa fa-sign-out-alt"></i> Cerrar Sesi&oacute;n
                    </button>
                </form>
            </div>
        </div>
        
        <!-- Modal de confirmaci&oacute;n -->
        <div id="modalConfirmacion" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3><i class="fa fa-exclamation-triangle"></i> Confirmar Acci&oacute;n</h3>
                </div>
                <div class="modal-body">
                    <p>&iquest;Est&aacute;s seguro de que quieres <strong>desactivar</strong> este usuario?</p>
                    <p>El usuario no podr&aacute; acceder al sistema hasta que sea reactivado.</p>
                </div>
                <div class="modal-footer">
                    <button id="btnConfirmar" class="btn-confirmar">S&iacute;, Desactivar</button>
                    <button onclick="cerrarModal()" class="btn-cancelar">Cancelar</button>
                </div>
            </div>
        </div>
        
        <c:import url="../INC/footer.inc"/>
        <script>
            window.appContext = '${applicationScope.contexto}';
        </script>
        <script src="${applicationScope.contexto}/JS/listarUsuario.js"></script>
    </body>
</html>
