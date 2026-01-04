<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="../../INC/cabecera.jsp">
            <c:param name="titulo" value="Error" />
            <c:param name="estilo" value="${estilo}" />
        </c:import>
    </head>
    <body>
        <div id="principal" style="border-radius: 0px;">
            
            <a href="${contexto}/FrontController">
                <img src="${contexto}/IMG/errores/error404.png" alt="Imagen de error 404" style="width: 100%; height: 100%; margin: auto;"/></a> 
        </div>
    </body>
</html>
