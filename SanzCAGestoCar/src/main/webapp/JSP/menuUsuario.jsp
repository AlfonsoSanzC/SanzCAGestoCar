<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/>
<!DOCTYPE html>
<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"/>
<html lang="es">
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.css"/>
        <script src="https://cdn.tailwindcss.com"></script>
        <jsp:include page="/INC/cabecera.jsp">
            <jsp:param name="titulo" value="Registro | SanzCAGestoCar"/>
            <jsp:param name="estilo" value="${estilo}"/>
        </jsp:include>
    </head>
    <body class="menuUsuario bg-white min-h-screen flex flex-col">
        <c:import url="../INC/nav.jsp"/>
        <div class="flex-1 w-full max-w-7xl mx-auto py-8 px-3">
            <!-- PERFIL USUARIO -->
            <div class="mb-8">
                <div class="bg-gradient-to-r from-gray-900 to-black rounded-2xl p-6 text-white shadow-xl">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center space-x-4">
                            <div class="bg-white bg-opacity-20 rounded-full p-4">
                                <i class="fas fa-user text-2xl"></i>
                            </div>
                            <div>
                                <h2 class="text-xl font-bold">Perfil de Usuario</h2>
                                <p class="text-gray-300 text-sm">Gestiona tu informaci&oacute;n personal</p>
                            </div>
                        </div>
                        <form action="${applicationScope.contexto}/ModificarDatosController" method="post">
                            <input type="hidden" name="accion" value="modificarDatos"/>
                            <button type="submit" class="profile-btn">
                                <i class="fas fa-user-edit mr-2"></i>
                                Editar Datos
                                <i class="fas fa-arrow-right ml-2"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

                <!-- COLUMNA 1: REGISTRAR VEHÍCULO -->
                <div class="bg-white border-2 border-gray-200 rounded-2xl p-6 shadow-xl hover:shadow-2xl transition-shadow duration-300">
                    <div class="text-center mb-6">
                        <div class="bg-black text-white rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-3">
                            <i class="fas fa-plus-circle text-2xl"></i>
                        </div>
                        <h2 class="text-2xl font-bold text-black">Registrar Veh&iacute;culo</h2>
                        <p class="text-gray-600 text-sm mt-1">A&ntilde;ade un nuevo veh&iacute;culo</p>
                    </div>
                    
                    <form id="formCrearVehiculo" action="${applicationScope.contexto}/CrearVehiculoController" method="post" class="space-y-4">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="form-group">
                                <label class="form-label">*Marca</label>
                                <input type="text" name="marca" class="form-input" placeholder="Ej: Toyota"/>
                            </div>
                            <div class="form-group">
                                <label class="form-label">*Modelo</label>
                                <input type="text" name="modelo" class="form-input" placeholder="Ej: Corolla"/>
                            </div>
                        </div>
                        
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="form-group">
                                <label class="form-label">*Motor</label>
                                <select name="motor" class="form-input">
                                    <option value="GASOLINA">Gasolina</option>
                                    <option value="GASOIL">Gasoil</option>
                                    <option value="ELECTRICO">El&eacute;ctrico</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="form-label">*Cilindrada</label>
                                <input type="text" name="cilindrada" class="form-input" placeholder="1600"/>
                            </div>
                        </div>
                        
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="form-group">
                                <label class="form-label">*Matr&iacute;cula</label>
                                <input type="text" name="matricula" class="form-input" placeholder="1234ABC" style="text-transform: uppercase;"/>
                                <div id="matriculaValidation" class="matricula-validation"></div>
                            </div>
                            <div class="form-group">
                                <label class="form-label">*Caballos</label>
                                <input type="text" name="caballos" class="form-input" placeholder="120"/>
                            </div>
                        </div>
                        
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="form-group">
                                <label class="form-label">*Color</label>
                                <input type="color" name="color" class="w-full h-12 rounded-lg border-2 border-gray-300 cursor-pointer"/>
                            </div>
                            <div class="form-group">
                                <label class="form-label">*Fecha Matriculaci&oacute;n</label>
                                <input type="date" name="fechaCompra" class="form-input"/>
                            </div>
                        </div>
                        
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="form-group">
                                <label class="form-label">Fecha Venta (opcional)</label>
                                <input type="date" name="fechaVenta" class="form-input"/>
                            </div>
                            <div class="form-group">
                                <label class="form-label">*Precio Compra</label>
                                <input type="text" id="precioCompra" name="precioCompra" class="form-input" placeholder="15000.00" />
                                <div id="precioCompraValidation" class="price-validation"></div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label">Precio Venta (opcional)</label>
                            <input type="text" id="precioVenta" name="precioVenta" class="form-input" placeholder="12000.00" />
                            <div id="precioVentaValidation" class="price-validation"></div>
                        </div>
                        
                        <div id="mensajeVehiculo" style="display: none;"></div>
                        
                        <button type="submit" name="boton" value="crearVehiculo" class="create-vehicle-btn">
                            <i class="fas fa-car mr-2"></i>
                            CREAR VEH&Iacute;CULO
                        </button>
                    </form>
                </div>

                <!-- COLUMNA 2: GESTIÓN DE VEHÍCULOS -->
                <div class="bg-white border-2 border-gray-200 rounded-2xl p-6 shadow-xl hover:shadow-2xl transition-shadow duration-300">
                    <div class="text-center mb-6">
                        <div class="bg-black text-white rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-3">
                            <i class="fas fa-car text-2xl"></i>
                        </div>
                        <h2 class="text-2xl font-bold text-black">Gesti&oacute;n de Veh&iacute;culos</h2>
                        <p class="text-gray-600 text-sm mt-1">Administra tus veh&iacute;culos</p>
                    </div>
                    
                    <div class="space-y-3">
                        <form action="${applicationScope.contexto}/VisualizarVehiculoController" method="post">
                            <button type="submit" class="menu-option-btn">
                                <i class="fas fa-list mr-3"></i>
                                <span>Ver mis Veh&iacute;culos</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/ModificarVehiculoController" method="post">
                            <button type="submit" class="menu-option-btn">
                                <i class="fas fa-edit mr-3"></i>
                                <span>Modificar Veh&iacute;culo</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/EliminarVehiculoController" method="post">
                            <button type="submit" class="menu-option-btn">
                                <i class="fas fa-trash mr-3"></i>
                                <span>Eliminar Veh&iacute;culo</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/ImagenesVehiculoController" method="post">
                            <button type="submit" class="menu-option-btn" disabled>
                                <i class="fas fa-images mr-3"></i>
                                <span>Subir Im&aacute;genes</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                    </div>
                </div>

                <!-- COLUMNA 3: CONTROL DE GASTOS -->
                <div class="bg-white border-2 border-gray-200 rounded-2xl p-6 shadow-xl hover:shadow-2xl transition-shadow duration-300">
                    <div class="text-center mb-6">
                        <div class="bg-black text-white rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-3">
                            <i class="fas fa-calculator text-2xl"></i>
                        </div>
                        <h2 class="text-2xl font-bold text-black">Control de Gastos</h2>
                        <p class="text-gray-600 text-sm mt-1">Gestiona tus finanzas</p>
                    </div>
                    
                    <div class="space-y-3">
                        <form action="${applicationScope.contexto}/GastoController" method="post">
                            <button type="submit" name="boton" value="insertarGasto" class="menu-option-btn">
                                <i class="fas fa-plus-circle mr-3"></i>
                                <span>Añadir Gasto</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/VisualizarGastoController" method="post">
                            <button type="submit" name="boton" value="visualizarGasto" class="menu-option-btn">
                                <i class="fas fa-eye mr-3"></i>
                                <span>Ver Gastos</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/TodoGastoController" method="post">
                            <button type="submit" name="accion" value="estadisticas" class="menu-option-btn">
                                <i class="fas fa-chart-pie mr-3"></i>
                                <span>Estad&iacute;sticas</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                        
                        <form action="${applicationScope.contexto}/BuscarGastoController" method="post">
                            <button type="submit" class="menu-option-btn">
                                <i class="fas fa-search mr-3"></i>
                                <span>Buscar Gastos</span>
                                <i class="fas fa-chevron-right ml-auto"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            
            <!-- BOTÓN CERRAR SESIÓN -->
            <div class="text-center mt-8">
                <form action="${applicationScope.contexto}/ReturnCerrarSesion" method="post">
                    <button type="submit" class="btn-salir" style="max-width: 300px; margin: 0 auto;">
                        <i class="fas fa-sign-out-alt mr-2"></i>
                        CERRAR SESIÓN
                    </button>
                </form>
            </div>
        </div>
        <c:import url="../INC/footer.inc"/>
        
        <input type="hidden" id="contexto" value="${applicationScope.contexto}"/>
        
        <script src="${applicationScope.contexto}/JS/validarMatriculaAjax.js"></script>
        <script src="${applicationScope.contexto}/JS/validarPrecios.js"></script>
    </body>
</html>
