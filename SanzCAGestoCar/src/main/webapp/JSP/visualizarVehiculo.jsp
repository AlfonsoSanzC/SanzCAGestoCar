<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Mis Veh&iacute;culos | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
    </head>
    <body class="visualizar-vehiculos">
        <c:import url="../INC/nav.jsp"/>
        <main>
            <div class="vehiculos-container">
                <div class="vehiculos-header">
                    <h1 class="vehiculos-title">
                        <i class="fas fa-car mr-3"></i>
                        Mis Veh&iacute;culos
                    </h1>
                    <p class="vehiculos-subtitle">Gestiona tu flota de veh&iacute;culos</p>
                </div>

                <c:choose>
                    <c:when test="${empty vehiculos}">
                        <div class="vehiculos-empty">
                            <div class="empty-icon">
                                <i class="fas fa-car-side"></i>
                            </div>
                            <h3>No tienes veh&iacute;culos registrados</h3>
                            <p>Comienza agregando tu primer veh&iacute;culo</p>
                            <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post">
                                <button type="submit" class="btn-primary">
                                    <i class="fas fa-plus mr-2"></i>
                                    Agregar Veh&iacute;culo
                                </button>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="vehiculos-stats">
                            <div class="stat-card">
                                <i class="fas fa-car"></i>
                                <div>
                                    <h3>${vehiculos.size()}</h3>
                                    <p>Veh&iacute;culos</p>
                                </div>
                            </div>
                        </div>

                        <div class="vehiculos-grid">
                            <c:forEach var="vehiculo" items="${vehiculos}">
                                <div class="vehiculo-card">
                                    <div class="vehiculo-header">
                                        <h3>${vehiculo.marca} ${vehiculo.modelo}</h3>
                                        <span class="vehiculo-matricula">${vehiculo.matricula}</span>
                                    </div>
                                    
                                    <div class="vehiculo-details">
                                        <div class="detail-row">
                                            <span class="detail-label">Motor:</span>
                                            <span class="detail-value motor-${vehiculo.motor.name().toLowerCase()}">
                                                <i class="fas fa-cog mr-1"></i>
                                                ${vehiculo.motor}
                                            </span>
                                        </div>
                                        
                                        <div class="detail-row">
                                            <span class="detail-label">Cilindrada:</span>
                                            <span class="detail-value">${vehiculo.cilindrada} cc</span>
                                        </div>
                                        
                                        <div class="detail-row">
                                            <span class="detail-label">Caballos:</span>
                                            <span class="detail-value">${vehiculo.caballos} CV</span>
                                        </div>
                                        
                                        <div class="detail-row">
                                            <span class="detail-label">Color:</span>
                                            <span class="detail-value">
                                                <span class="color-sample" style="background-color: ${vehiculo.color}"></span>
                                                ${vehiculo.color}
                                            </span>
                                        </div>
                                        
                                        <div class="detail-row">
                                            <span class="detail-label">Fecha Compra:</span>
                                            <span class="detail-value">
                                                <fmt:formatDate value="${vehiculo.fechaCompra}" pattern="dd/MM/yyyy"/>
                                            </span>
                                        </div>
                                        
                                        <c:if test="${not empty vehiculo.fechaVenta}">
                                            <div class="detail-row">
                                                <span class="detail-label">Fecha Venta:</span>
                                                <span class="detail-value">
                                                    <fmt:formatDate value="${vehiculo.fechaVenta}" pattern="dd/MM/yyyy"/>
                                                </span>
                                            </div>
                                        </c:if>
                                        
                                        <div class="detail-row">
                                            <span class="detail-label">Precio Compra:</span>
                                            <span class="detail-value precio">
                                                <fmt:formatNumber value="${vehiculo.precioCompra}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </div>
                                        
                                        <c:if test="${not empty vehiculo.precioVenta and vehiculo.precioVenta > 0}">
                                            <div class="detail-row">
                                                <span class="detail-label">Precio Venta:</span>
                                                <span class="detail-value precio">
                                                    <fmt:formatNumber value="${vehiculo.precioVenta}" type="currency" currencySymbol="€"/>
                                                </span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <div class="vehiculos-actions">
                            <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post">
                                <button type="submit" class="btn-secondary">
                                    <i class="fas fa-arrow-left mr-2"></i>
                                    Volver al Men&uacute;
                                </button>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
        <c:import url="../INC/footer.inc"/>
    </body>
</html> 