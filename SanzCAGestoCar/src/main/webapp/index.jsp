<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="estilo" value="/CSS/estilos.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INC/cabecera.jsp">
        <jsp:param name="titulo" value="Gestocar"/>
        <jsp:param name="estilo" value="${estilo}"/>
    </jsp:include>
</head>
<body class="index">
    <c:import url="INC/nav.jsp"/> 

    <main>
        <div class="hero-background">
            <div class="hero-content">
                <h1 class="hero-title">
                    Gestiona tu <span class="text-highlight">Flota</span><br>
                    de manera <span class="text-highlight">Inteligente</span>
                </h1>
                
                <p class="hero-subtitle">
                    Control total de tus vehículos, gastos y mantenimiento.<br>
                    Todo en una plataforma moderna y fácil de usar.
                </p>
                
                <div class="hero-features">
                    <div class="feature-item">
                        <i class="fas fa-tachometer-alt"></i>
                        <span>Control Total</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-chart-line"></i>
                        <span>Análisis Detallado</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-shield-alt"></i>
                        <span>Seguro & Confiable</span>
                    </div>
                </div>
                
                <div class="hero-actions">
                    <form action="${applicationScope.contexto}/FrontController" method="post">
                        <input type="hidden" name="accion" value="login"/>
                        <button class="hero-btn hero-btn-primary" type="submit">
                            <i class="fas fa-sign-in-alt"></i>
                            <span>Iniciar Sesión</span>
                            <i class="fas fa-arrow-right"></i>
                        </button>
                    </form>
                    <form action="${applicationScope.contexto}/FrontController" method="post">
                        <input type="hidden" name="accion" value="registro"/>
                        <button class="hero-btn hero-btn-secondary" type="submit">
                            <i class="fas fa-user-plus"></i>
                            <span>Crear Cuenta</span>
                            <i class="fas fa-arrow-right"></i>
                        </button>
                    </form>
                </div>
            </div>
            
            <div class="hero-decoration">
                <div class="floating-car">
                    <i class="fas fa-car"></i>
                </div>
                <div class="floating-chart">
                    <i class="fas fa-chart-pie"></i>
                </div>
                <div class="floating-gear">
                    <i class="fas fa-cog"></i>
                </div>
            </div>
        </div>
    </main>

    <c:import url="/INC/footer.inc"/>
</body>
</html>
