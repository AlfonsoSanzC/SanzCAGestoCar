<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="estilo" value="/CSS/estilos.css" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INC/cabecera.jsp">
        <jsp:param name="titulo" value="Introducir Gasto | SanzCAGestoCar"/>
        <jsp:param name="estilo" value="${estilo}"/>
    </jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.css"/>
</head>
<body class="introducir-gasto">
    <c:import url="../INC/nav.jsp"/>
    
    <main>
        <div class="gasto-form-container">
            <h1 class="gasto-form-title">
                <i class="fas fa-plus-circle"></i>
                Introducir Gasto
            </h1>
            
            <!-- Mensajes de error/&eacute;xito -->
            <c:if test="${not empty error}">
                <div class="gasto-alert gasto-alert-error">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty exito}">
                <div class="gasto-alert gasto-alert-success">
                    <i class="fas fa-check-circle"></i>
                    ${exito}
                </div>
            </c:if>
            
            <form action="${applicationScope.contexto}/GastoController" method="post">
                <!-- Selecci&oacute;n de veh&iacute;culo -->
                <div class="gasto-form-group">
                    <label for="idVehiculo">
                        <i class="fas fa-car"></i>
                        Seleccionar Veh&iacute;culo <span class="gasto-required">*</span>
                    </label>
                    <select id="idVehiculo" name="idVehiculo">
                        <option value="">-- Selecciona un veh&iacute;culo --</option>
                        <c:forEach var="vehiculo" items="${vehiculos}">
                            <option value="${vehiculo.idVehiculo}" 
                                    ${param.idVehiculo == vehiculo.idVehiculo ? 'selected' : ''}>
                                ${vehiculo.marca} ${vehiculo.modelo} - ${vehiculo.matricula}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <!-- Concepto con datalist -->
                <div class="gasto-form-group">
                    <label for="concepto">
                        <i class="fas fa-tag"></i>
                        Concepto <span class="gasto-required">*</span>
                    </label>
                    <input type="text" id="concepto" name="concepto" 
                           list="conceptos-list" 
                           value="${param.concepto}" 
                           maxlength="40">
                    <datalist id="conceptos-list">
                        <c:forEach var="concepto" items="${conceptos}">
                            <option value="${concepto}">
                        </c:forEach>
                    </datalist>
                </div>
                
                <!-- Fecha del gasto -->
                <div class="gasto-form-group">
                    <label for="fechaGasto">
                        <i class="fas fa-calendar"></i>
                        Fecha del Gasto <span class="gasto-required">*</span>
                    </label>
                    <input type="date" id="fechaGasto" name="fechaGasto" 
                           value="${param.fechaGasto}">
                </div>
                
                <!-- Descripci&oacute;n -->
                <div class="gasto-form-group">
                    <label for="descripcion">
                        <i class="fas fa-align-left"></i>
                        Descripci&oacute;n <span class="gasto-required">*</span>
                    </label>
                    <textarea id="descripcion" name="descripcion" 
                              maxlength="100">${param.descripcion}</textarea>
                </div>
                
                <!-- Importe -->
                <div class="gasto-form-group">
                    <label for="importe">
                        <i class="fas fa-euro-sign"></i>
                        Importe (€) <span class="gasto-required">*</span>
                    </label>
                    <input type="number" id="importe" name="importe" 
                           step="0.01" min="0.01"
                           value="${param.importe}">
                </div>
                
                <!-- Establecimiento (opcional) -->
                <div class="gasto-form-group">
                    <label for="establecimiento">
                        <i class="fas fa-store"></i>
                        Establecimiento
                    </label>
                    <input type="text" id="establecimiento" name="establecimiento" 
                           value="${param.establecimiento}" 
                           maxlength="100">
                </div>
                
                <!-- Kil&oacute;metros (opcional) -->
                <div class="gasto-form-group">
                    <label for="kilometros">
                        <i class="fas fa-tachometer-alt"></i>
                        Kil&oacute;metros
                    </label>
                    <input type="text" id="kilometros" name="kilometros" 
                           value="${param.kilometros}" 
                           maxlength="7">
                </div>
                
                <!-- Bot&oacute;n Guardar -->
                <div class="gasto-btn-container">
                    <button type="submit" name="boton" value="guardarGasto" class="gasto-btn gasto-btn-primary">
                        <i class="fas fa-save"></i>
                        Guardar Gasto
                    </button>
                </div>
            </form>
            
            <!-- Bot&oacute;n Volver (fuera del formulario principal) -->
            <div class="gasto-btn-container" style="margin-top: 15px;">
                <form action="${applicationScope.contexto}/ReturnMenuUsuario" method="post" style="display: inline;">
                    <button type="submit" class="gasto-btn gasto-btn-secondary">
                        <i class="fas fa-arrow-left"></i>
                        Volver al Men&uacute;
                    </button>
                </form>
            </div>
        </div>
    </main>
    
    <c:import url="../INC/footer.inc"/>
    
    <script src="${applicationScope.contexto}/JS/validarGasto.js"></script>
</body>
</html> 