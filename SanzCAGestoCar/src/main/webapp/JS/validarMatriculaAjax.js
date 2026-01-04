// Referencia a menuUsuario.jsp
let matriculaTimeout;
const matriculaInput = document.querySelector('input[name="matricula"]');
const matriculaValidation = document.getElementById('matriculaValidation');
const contexto = document.getElementById('contexto').value;

if (matriculaInput && matriculaValidation) {
    matriculaInput.addEventListener('input', function () {
        clearTimeout(matriculaTimeout);
        const matricula = this.value.trim();

        if (matricula.length === 0) {
            matriculaValidation.innerHTML = '';
            return;
        }

        // Validación básica de formato de matrícula (4 números + 3 letras)
        const formatoMatricula = /^[0-9]{4}[A-Z]{3}$/i;
        if (!formatoMatricula.test(matricula)) {
            matriculaValidation.innerHTML = '<i class="fas fa-times mr-1"></i>Formato incorrecto (ej: 1234ABC)';
            matriculaValidation.className = 'matricula-validation matricula-invalid';
            return;
        }

        matriculaValidation.innerHTML = '<i class="fas fa-spinner fa-spin mr-1"></i>Verificando...';
        matriculaValidation.className = 'matricula-validation';

        matriculaTimeout = setTimeout(() => {
            fetch(contexto + '/CrearVehiculoController', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: 'accion=validarMatricula&matricula=' + encodeURIComponent(matricula)
            })
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) {
                            matriculaValidation.innerHTML = '<i class="fas fa-exclamation-triangle mr-1"></i>' + data.error;
                            matriculaValidation.className = 'matricula-validation matricula-invalid';
                        } else if (!data.existe) {
                            matriculaValidation.innerHTML = '<i class="fas fa-check mr-1"></i>Matrícula disponible';
                            matriculaValidation.className = 'matricula-validation matricula-valid';
                        } else {
                            matriculaValidation.innerHTML = '<i class="fas fa-times mr-1"></i>Matrícula ya existe';
                            matriculaValidation.className = 'matricula-validation matricula-invalid';
                        }
                    })
                    .catch(() => {
                        matriculaValidation.innerHTML = '<i class="fas fa-exclamation-triangle mr-1"></i>Error al verificar';
                        matriculaValidation.className = 'matricula-validation matricula-invalid';
                    });
        }, 500);
    });
}

function validarFormulario(form, mensajeDiv) {
    // Validar campos obligatorios
    const camposObligatorios = 'marca,modelo,motor,matricula,cilindrada,caballos,color,fechaCompra,precioCompra'.split(',');
    for (let nombre of camposObligatorios) {
        const campo = form.querySelector(`[name="${nombre}"]`);
        if (!campo?.value.trim()) {
            mensajeDiv.innerHTML = '<div class="alert-error"><i class="fas fa-exclamation-triangle mr-2"></i>Por favor, completa todos los campos obligatorios marcados con *</div>';
            mensajeDiv.style.display = 'block';
            campo?.focus();
            return false;
        }
    }

    // Validar fecha de venta
    const fechaVenta = form.querySelector('[name="fechaVenta"]');
    if (fechaVenta?.value) {
        const hoy = new Date().toISOString().split('T')[0];
        if (fechaVenta.value > hoy) {
            mensajeDiv.innerHTML = '<div class="alert-error"><i class="fas fa-exclamation-triangle mr-2"></i>La fecha de venta no puede ser posterior al día de hoy</div>';
            mensajeDiv.style.display = 'block';
            fechaVenta.focus();
            return false;
        }
    }

    // Validar matrícula
    if (matriculaValidation?.classList.contains('matricula-invalid')) {
        mensajeDiv.innerHTML = '<div class="alert-error"><i class="fas fa-exclamation-triangle mr-2"></i>Por favor, corrige la matrícula antes de continuar</div>';
        mensajeDiv.style.display = 'block';
        matriculaInput?.focus();
        return false;
    }

    return true;
}

// Validación y envío del formulario
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formCrearVehiculo');
    const mensajeDiv = document.getElementById('mensajeVehiculo');

    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            mensajeDiv.style.display = 'none';

            if (!validarFormulario(form, mensajeDiv))
                return;

            // Enviar formulario via Ajax
            const formData = new FormData(form);
            formData.append('boton', 'crearVehiculo');
            const params = new URLSearchParams(formData).toString();

            fetch(contexto + '/CrearVehiculoController', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: params
            })
                    .then(response => response.json())
                    .then(data => {
                        const mensaje = data.ok
                                ? '<div class="alert-success"><i class="fas fa-check-circle mr-2"></i>Vehículo registrado correctamente</div>'
                                : '<div class="alert-error"><i class="fas fa-exclamation-triangle mr-2"></i>' + (data.mensaje || 'Error al crear el vehículo') + '</div>';

                        mensajeDiv.innerHTML = mensaje;
                        mensajeDiv.style.display = 'block';

                        if (data.ok) {
                            form.reset();
                            matriculaValidation.innerHTML = '';
                        }
                    })
                    .catch(() => {
                        mensajeDiv.innerHTML = '<div class="alert-error"><i class="fas fa-exclamation-triangle mr-2"></i>Error al crear el vehículo</div>';
                        mensajeDiv.style.display = 'block';
                    });
        });
    }
}); 