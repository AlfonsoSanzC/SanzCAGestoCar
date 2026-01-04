<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Modificar Datos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:import url="../INC/nav.jsp"/>
        <main class="flex-grow-1">
            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="card shadow-lg border-0">
                            <div class="card-header bg-dark text-white text-center py-4">
                                <h1 class="h3 mb-0">
                                    <i class="fas fa-user-edit me-2"></i>
                                    Modificar Datos
                                </h1>
                                <p class="mb-0 mt-2 text-light">Actualiza tu informaci&oacute;n personal</p>
                            </div>
                            <div class="card-body p-4">
                                <!-- Mensajes -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">
                                        <i class="fas fa-exclamation-triangle me-2"></i>
                                        ${error}
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty exito}">
                                    <div class="alert alert-success">
                                        <i class="fas fa-check-circle me-2"></i>
                                        ${exito}
                                    </div>
                                </c:if>
                                
                                <form id="formModificarDatos" action="${applicationScope.contexto}/ModificarDatosController" method="post" enctype="multipart/form-data">
                                    <input type="hidden" name="accion" value="guardarCambios"/>
                                    
                                    <!-- Datos Personales -->
                                    <div class="mb-4">
                                        <h5 class="text-dark mb-3">
                                            <i class="fas fa-user me-2"></i>
                                            Datos Personales
                                        </h5>
                                        
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Nombre <span class="text-danger">*</span></label>
                                                <input type="text" name="nombre" class="form-control" 
                                                       value="${sessionScope.usuario.nombre}" placeholder="Tu nombre">
                                            </div>
                                            
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Apellidos <span class="text-danger">*</span></label>
                                                <input type="text" name="apellidos" class="form-control" 
                                                       value="${sessionScope.usuario.apellidos}" placeholder="Tus apellidos">
                                            </div>
                                        </div>
                                        
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Email</label>
                                                <input type="email" name="email" class="form-control" 
                                                       value="${sessionScope.usuario.email}" readonly>
                                                <small class="text-muted">El email no se puede modificar</small>
                                            </div>
                                            
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">DNI <span class="text-danger">*</span></label>
                                                <input type="text" name="dni" class="form-control" 
                                                       value="${sessionScope.usuario.dni}" placeholder="12345678A" maxlength="9">
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Cambiar Im&aacute;genes -->
                                    <div class="mb-4">
                                        <h5 class="text-dark mb-3">
                                            <i class="fas fa-image me-2"></i>
                                            Cambiar Im&aacute;genes
                                        </h5>
                                        <p class="text-muted small mb-3">Deja estos campos vac&iacute;os si no quieres cambiar las im&aacute;genes (m&aacute;ximo 100KB cada una)</p>
                                        
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Avatar (opcional)</label>
                                                <input type="file" name="avatar" class="form-control" accept="image/png, image/jpeg">
                                                <small class="text-muted">Actual: ${sessionScope.usuario.avatar}</small>
                                            </div>
                                            
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Carnet de Conducir (opcional)</label>
                                                <input type="file" name="carneconducir" class="form-control" accept="image/png, image/jpeg">
                                                <small class="text-muted">Actual: ${sessionScope.usuario.carneConducir}</small>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Cambiar Contrase&ntilde;a -->
                                    <div class="mb-4">
                                        <h5 class="text-dark mb-3">
                                            <i class="fas fa-lock me-2"></i>
                                            Cambiar Contrase&ntilde;a
                                        </h5>
                                        <p class="text-muted small mb-3">Deja estos campos vac&iacute;os si no quieres cambiar la contrase&ntilde;a</p>
                                        
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Contrase&ntilde;a Actual</label>
                                            <input type="password" name="contrasenaActual" class="form-control" 
                                                   placeholder="Contrase&ntilde;a actual">
                                        </div>
                                        
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Nueva Contrase&ntilde;a</label>
                                                <input type="password" name="nuevaContrasena" class="form-control" 
                                                       placeholder="Nueva contrase&ntilde;a (opcional)">
                                            </div>
                                            
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-bold">Confirmar Nueva Contrase&ntilde;a</label>
                                                <input type="password" name="confirmarContrasena" class="form-control" 
                                                       placeholder="Repite la nueva contrase&ntilde;a">
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Botones -->
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                        <button type="submit" class="btn btn-success btn-lg px-4">
                                            <i class="fas fa-save me-2"></i>
                                            Guardar Cambios
                                        </button>
                                    </div>
                                </form>
                                
                                <!-- Formulario separado para cancelar -->
                                <div class="text-center mt-3">
                                    <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-secondary btn-lg px-4">
                                            <i class="fas fa-times me-2"></i>
                                            Cancelar
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <c:import url="../INC/footer.inc"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 