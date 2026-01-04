<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Registro | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
        <script src="${applicationScope.contexto}/JS/validarEmail.js"></script>
    </head>
    <body class="registro">
        <c:import url="../INC/nav.jsp"/>
        <main>
            <div class="form-registro-container">
                <form action="${applicationScope.contexto}/RegistroController" method="post" class="form-registro" enctype="multipart/form-data">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" maxlength="20" placeholder="Introduce tu nombre" value="${param.nombre}">

                    <label for="apellidos">Apellidos:</label>
                    <input type="text" id="apellidos" name="apellidos" maxlength="30" placeholder="Introduce tus apellidos" value="${param.apellidos}">

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" maxlength="50" placeholder="ejemplo@correo.com" value="${param.email}">
                    
                    <div id="mensajeEmail" class="msg-error" style="display: none;"></div>

                    <label for="password">Contrase&ntilde;a :</label>
                    <input type="password" id="password" name="password" maxlength="100">

                    <label for="confirmarPassword">Confirmar Contrase&ntilde;a :</label>
                    <input type="password" id="confirmarPassword" name="confirmarPassword" maxlength="100">

                    <div id="mensajePassword" class="msg-error" style="display: none;"></div>

                    <label for="dni">DNI :</label>
                    <input type="text" id="dni" name="dni" maxlength="9" placeholder="Introduce tu DNI" value="${param.dni}">

                    <label>Avatar (opcional): <input type="file" name="avatar" accept="image/png, image/jpeg"/></label>
                    <label>Carnet de Conducir (opcional): <input type="file" name="carneconducir" accept="image/png, image/jpeg"/></label>
                    <!-- Mensaje de error, se muestra solo si existe -->
                    <c:if test="${not empty mensajeError}">
                        <div class="msg-error">${mensajeError}</div>
                    </c:if>

                    <button type="submit" class="btn-registro" name="boton" value="crearCuenta">CREAR CUENTA</button>
                </form>
                <form action="${applicationScope.contexto}/Return" method="post" style="display: inline;">
                <button type="submit" class="btn-salir">SALIR</button>
            </form>
            </div>
        </main>
        <c:import url="../INC/footer.inc"/>
        <c:if test="${not empty usuarioCreado}">
            <div id="notif-exito" class="notif-exito">Usuario registrado con &eacute;xito</div>
            <script>
                window.onload = function () {
                    var notif = document.getElementById('notif-exito');
                    notif.style.display = 'block';
                    setTimeout(function () {
                        notif.style.display = 'none';
                    }, 3000);
                }
            </script>
        </c:if>
    </body>
</html>
