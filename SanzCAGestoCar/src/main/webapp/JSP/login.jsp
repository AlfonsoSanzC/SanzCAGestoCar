<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Login | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
    </head>
    <body class="login">
        <c:import url="../INC/nav.jsp"/>
        <main>
            <div class="form-login-container">

                <form action="${applicationScope.contexto}/LoginController" method="post" class="form-login">
                    <label>E-mail
                        <input type="email" name="email" maxlength="50" placeholder="ejemplo@correo.com"/>
                    </label>

                    <label>Contrase&ntilde;a
                        <input type="password" name="password" maxlength="100"/>
                    </label>

                    <c:if test="${not empty requestScope.error}">
                        <div class="msg-error">${requestScope.error}</div>
                    </c:if>

                    <button type="submit" class="btn-login">
                        ENTRAR
                    </button>
                </form>

                <form action="${applicationScope.contexto}/Return" method="post" style="text-align:center;display:block">
                    <button type="submit" class="btn-salir" style="text-align:center;display:block">SALIR</button>
                </form>
            </div>
        </main>

        <c:import url="../INC/footer.inc"/>
    </body>
</html>