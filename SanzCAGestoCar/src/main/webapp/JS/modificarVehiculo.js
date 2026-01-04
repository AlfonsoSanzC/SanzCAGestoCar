// Referencia a modificarVehiculo.jsp
document.addEventListener('DOMContentLoaded', function () {
    const contexto = document.getElementById('contexto').value;
    const vehiculosContainer = document.getElementById('vehiculos-container');
    const formModificar = document.getElementById('formModificarVehiculo');
    const mensajeModificar = document.getElementById('mensajeModificar');
    const btnCancelar = document.getElementById('btnCancelar');

    // Referencias a tabs
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    const tabModificar = document.querySelector('[data-tab="tab-modificar"]');

    // Referencias a campos de validación
    const precioCompraInput = document.getElementById('precioCompra');
    const precioVentaInput = document.getElementById('precioVenta');
    const precioCompraValidation = document.getElementById('precioCompraValidation');
    const precioVentaValidation = document.getElementById('precioVentaValidation');
    const matriculaInput = document.getElementById('matricula');
    const matriculaValidation = document.getElementById('matriculaValidation');

    let vehiculoSeleccionado = null;

    // Función para cambiar de tab
    function cambiarTab(targetTab) {
        tabButtons.forEach(btn => btn.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        document.querySelector(`[data-tab="${targetTab}"]`).classList.add('active');
        document.getElementById(targetTab).classList.add('active');
    }

    // Event listeners para tabs
    tabButtons.forEach(button => {
        button.addEventListener('click', function () {
            if (!this.disabled) {
                const target = this.getAttribute('data-tab');
                cambiarTab(target);
            }
        });
    });

    // Función para mostrar mensaje
    function mostrarMensaje(mensaje, tipo) {
        mensajeModificar.textContent = mensaje;
        mensajeModificar.className = `mensaje-resultado ${tipo}`;
        mensajeModificar.style.display = 'block';

        setTimeout(() => {
            mensajeModificar.style.display = 'none';
        }, 5000);
    }

    // Función para formatear fecha
    function formatearFecha(fecha) {
        if (!fecha)
            return '-';
        const date = new Date(fecha);
        return date.toLocaleDateString('es-ES');
    }

    // Función para formatear precio
    function formatearPrecio(precio) {
        if (!precio)
            return '-';
        return parseFloat(precio).toFixed(2) + ' €';
    }

    // Función para convertir fecha a formato input
    function fechaToInputFormat(fecha) {
        if (!fecha)
            return '';
        const date = new Date(fecha);
        return date.toISOString().split('T')[0];
    }

    // Carga vehículos del usuario
    function cargarVehiculos() {
        fetch(`${contexto}/ModificarVehiculoController`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'accion=listarVehiculos'
        })
                .then(response => response.json())
                .then(vehiculos => {
                    if (vehiculos.length === 0) {
                        vehiculosContainer.innerHTML = `
                    <div class="loading-container">
                        <i class="fas fa-car" style="font-size: 3rem; color: #6c757d; margin-bottom: 1rem;"></i>
                        <h3>No tienes vehículos registrados</h3>
                        <p>Primero debes registrar un vehículo para poder modificarlo.</p>
                    </div>
                `;
                        return;
                    }

                    let html = '';
                    vehiculos.forEach(vehiculo => {
                        const fechaCompra = formatearFecha(vehiculo.fechaCompra);
                        const fechaVenta = formatearFecha(vehiculo.fechaVenta);
                        const precioCompra = formatearPrecio(vehiculo.precioCompra);
                        const precioVenta = formatearPrecio(vehiculo.precioVenta);

                        html += `
                    <div class="vehiculo-card-modificar" data-id="${vehiculo.idVehiculo}">
                        <div class="vehiculo-info">
                            <h3>${vehiculo.marca} ${vehiculo.modelo}</h3>
                            <div class="vehiculo-details">
                                <div class="detail-item">
                                    <div class="detail-label">Matrícula:</div>
                                    <div class="detail-value">${vehiculo.matricula}</div>
                                </div>
                                <div class="detail-item">
                                    <div class="detail-label">Motor:</div>
                                    <div class="detail-value">${vehiculo.motor}</div>
                                </div>
                                <div class="detail-item">
                                    <div class="detail-label">Cilindrada:</div>
                                    <div class="detail-value">${vehiculo.cilindrada}</div>
                                </div>
                                <div class="detail-item">
                                    <div class="detail-label">Caballos:</div>
                                    <div class="detail-value">${vehiculo.caballos}</div>
                                </div>
                                <div class="detail-item">
                                    <div class="detail-label">Fecha Compra:</div>
                                    <div class="detail-value">${fechaCompra}</div>
                                </div>
                                <div class="detail-item">
                                    <div class="detail-label">Precio Compra:</div>
                                    <div class="detail-value">${precioCompra}</div>
                                </div>
                            </div>
                            <button class="btn-modificar-vehiculo" onclick="seleccionarVehiculo(${vehiculo.idVehiculo})">
                                <i class="fas fa-edit mr-2"></i>
                                Modificar
                            </button>
                        </div>
                    </div>
                `;
                    });

                    vehiculosContainer.innerHTML = html;
                })
                .catch(error => {
                    console.error('Error:', error);
                    vehiculosContainer.innerHTML = `
                <div class="loading-container">
                    <i class="fas fa-exclamation-triangle" style="font-size: 3rem; color: #dc3545; margin-bottom: 1rem;"></i>
                    <h3>Error al cargar vehículos</h3>
                    <p>No se pudieron cargar tus vehículos. Inténtalo de nuevo.</p>
                </div>
            `;
                });
    }

    // Función global para seleccionar vehículo
    window.seleccionarVehiculo = function (idVehiculo) {
        fetch(`${contexto}/ModificarVehiculoController`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `accion=obtenerVehiculo&idVehiculo=${idVehiculo}`
        })
                .then(response => response.json())
                .then(vehiculo => {
                    if (vehiculo.ok === false) {
                        mostrarMensaje(vehiculo.mensaje, 'error');
                        return;
                    }

                    vehiculoSeleccionado = vehiculo;

                    // Llena formulario con datos del vehículo
                    document.getElementById('idVehiculoModificar').value = vehiculo.idVehiculo;
                    document.getElementById('marca').value = vehiculo.marca || '';
                    document.getElementById('modelo').value = vehiculo.modelo || '';
                    document.getElementById('motor').value = vehiculo.motor || 'GASOLINA';
                    document.getElementById('cilindrada').value = vehiculo.cilindrada || '';
                    document.getElementById('matricula').value = vehiculo.matricula || '';
                    document.getElementById('caballos').value = vehiculo.caballos || '';
                    document.getElementById('color').value = vehiculo.color || '#000000';
                    document.getElementById('fechaCompra').value = fechaToInputFormat(vehiculo.fechaCompra);
                    document.getElementById('fechaVenta').value = fechaToInputFormat(vehiculo.fechaVenta);
                    document.getElementById('precioCompra').value = vehiculo.precioCompra ? vehiculo.precioCompra.toFixed(2) : '';
                    document.getElementById('precioVenta').value = vehiculo.precioVenta ? vehiculo.precioVenta.toFixed(2) : '';

                    // Habilita y cambiar a tab de modificar
                    tabModificar.disabled = false;
                    cambiarTab('tab-modificar');

                    // Limpia validaciones
                    precioCompraValidation.textContent = '';
                    precioCompraValidation.className = 'price-validation';
                    precioVentaValidation.textContent = '';
                    precioVentaValidation.className = 'price-validation';
                    matriculaValidation.textContent = '';
                    matriculaValidation.className = 'matricula-validation';
                })
                .catch(error => {
                    console.error('Error:', error);
                    mostrarMensaje('Error al cargar los datos del vehículo', 'error');
                });
    };

    // Validación de precios, cogiendo la logica de crear vehiculo
    function validarPrecio(valor) {
        if (!valor || valor.trim() === '') {
            return null;
        }
        const numero = parseFloat(valor.replace(',', '.'));
        if (isNaN(numero)) {
            return false;
        }
        if (numero <= 1) {
            return false;
        }
        return true;
    }

    function mostrarValidacionPrecio(elemento, esValido, esOpcional = false, estaVacio = false) {
        if (estaVacio && esOpcional) {
            elemento.textContent = '';
            elemento.className = 'price-validation';
            return;
        }

        if (esValido) {
            elemento.textContent = '✓ Precio válido';
            elemento.className = 'price-validation price-valid';
        } else {
            elemento.textContent = '✗ Debe ser un número mayor que 1';
            elemento.className = 'price-validation price-invalid';
    }
    }

    // Event listeners para validación de precios
    precioCompraInput.addEventListener('input', function () {
        const valor = this.value.trim();
        const resultado = validarPrecio(valor);

        if (resultado === null) {
            mostrarValidacionPrecio(precioCompraValidation, false);
            precioCompraValidation.textContent = '✗ Campo obligatorio';
        } else {
            mostrarValidacionPrecio(precioCompraValidation, resultado);
        }
    });

    precioVentaInput.addEventListener('input', function () {
        const valor = this.value.trim();
        const resultado = validarPrecio(valor);

        if (resultado === null) {
            mostrarValidacionPrecio(precioVentaValidation, true, true, true);
        } else {
            mostrarValidacionPrecio(precioVentaValidation, resultado);
        }
    });

    // Formatea precios al perder el foco
    [precioCompraInput, precioVentaInput].forEach(input => {
        input.addEventListener('keypress', function (e) {
            const char = String.fromCharCode(e.which);
            if (!/[0-9.,]/.test(char) && e.which !== 8 && e.which !== 0) {
                e.preventDefault();
            }
        });

        input.addEventListener('blur', function () {
            const valor = this.value.trim();
            if (valor && !isNaN(parseFloat(valor.replace(',', '.')))) {
                const numero = parseFloat(valor.replace(',', '.'));
                this.value = numero.toFixed(2);
            }
        });
    });

    // Validación de matrícula en tiempo real
    matriculaInput.addEventListener('input', function () {
        const matricula = this.value.trim().toUpperCase();
        this.value = matricula;

        if (matricula.length < 7) {
            matriculaValidation.textContent = '';
            matriculaValidation.className = 'matricula-validation';
            return;
        }

        // Valida formato de matrícula española
        const formatoMatricula = /^[0-9]{4}[BCDFGHJKLMNPQRSTVWXYZ]{3}$/;
        if (!formatoMatricula.test(matricula)) {
            matriculaValidation.textContent = '✗ Formato inválido (ej: 1234ABC)';
            matriculaValidation.className = 'matricula-validation matricula-invalid';
            return;
        }

        // Si la matrícula no cambió, no valida duplicados
        if (vehiculoSeleccionado && vehiculoSeleccionado.matricula === matricula) {
            matriculaValidation.textContent = '✓ Matrícula válida';
            matriculaValidation.className = 'matricula-validation matricula-valid';
            return;
        }

        // Valida que no existe otra matrícula igual
        matriculaValidation.textContent = '⏳ Validando...';
        matriculaValidation.className = 'matricula-validation matricula-loading';

        fetch(`${contexto}/CrearVehiculoController`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `accion=validarMatricula&matricula=${encodeURIComponent(matricula)}`
        })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        matriculaValidation.textContent = '✗ Error al validar';
                        matriculaValidation.className = 'matricula-validation matricula-invalid';
                    } else if (data.existe) {
                        matriculaValidation.textContent = '✗ Matrícula ya registrada';
                        matriculaValidation.className = 'matricula-validation matricula-invalid';
                    } else {
                        matriculaValidation.textContent = '✓ Matrícula disponible';
                        matriculaValidation.className = 'matricula-validation matricula-valid';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    matriculaValidation.textContent = '✗ Error al validar';
                    matriculaValidation.className = 'matricula-validation matricula-invalid';
                });
    });

    // Maneja envío del formulario
    formModificar.addEventListener('submit', function (e) {
        e.preventDefault();

        // Valida todos los campos
        let esValido = true;

        // Valida precios
        const precioCompra = precioCompraInput.value.trim();
        const precioVenta = precioVentaInput.value.trim();

        const resultadoCompra = validarPrecio(precioCompra);
        if (resultadoCompra !== true) {
            if (resultadoCompra === null) {
                mostrarValidacionPrecio(precioCompraValidation, false);
                precioCompraValidation.textContent = '✗ Campo obligatorio';
            } else {
                mostrarValidacionPrecio(precioCompraValidation, false);
            }
            esValido = false;
        }

        if (precioVenta) {
            const resultadoVenta = validarPrecio(precioVenta);
            if (resultadoVenta !== true) {
                mostrarValidacionPrecio(precioVentaValidation, false);
                esValido = false;
            }
        }

        // Valida matrícula
        const matricula = matriculaInput.value.trim();
        if (!matricula || matricula.length < 7) {
            matriculaValidation.textContent = '✗ Matrícula requerida';
            matriculaValidation.className = 'matricula-validation matricula-invalid';
            esValido = false;
        }

        if (!esValido) {
            const primerError = document.querySelector('.price-invalid, .matricula-invalid');
            if (primerError) {
                primerError.scrollIntoView({behavior: 'smooth', block: 'center'});
            }
            return;
        }

        // Envia formulario
        const formData = new FormData(formModificar);
        formData.append('accion', 'modificarVehiculo');

        const params = new URLSearchParams();
        for (const [key, value] of formData.entries()) {
            params.append(key, value);
        }

        fetch(`${contexto}/ModificarVehiculoController`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
                .then(response => response.json())
                .then(data => {
                    if (data.ok) {
                        mostrarMensaje(data.mensaje, 'success');
                        setTimeout(() => {
                            // Volver a cargar vehículos y cambiar a tab de selección
                            cargarVehiculos();
                            tabModificar.disabled = true;
                            cambiarTab('tab-seleccionar');
                        }, 2000);
                    } else {
                        mostrarMensaje(data.mensaje, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    mostrarMensaje('Error al modificar el vehículo', 'error');
                });
    });

    // Botón cancelar
    btnCancelar.addEventListener('click', function () {
        tabModificar.disabled = true;
        cambiarTab('tab-seleccionar');
    });

    cargarVehiculos();
}); 