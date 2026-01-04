// Referencia a eliminarVehiculo.jsp
document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('.vehiculo-check');
    const selectAllBtn = document.getElementById('selectAll');
    const deselectAllBtn = document.getElementById('deselectAll');
    const eliminarBtn = document.getElementById('eliminarSeleccionados');
    const selectedCount = document.getElementById('selectedCount');
    const cantidadSpan = document.getElementById('cantidadVehiculos');
    const mensajeDiv = document.getElementById('mensajeDiv');
    const confirmarBtn = document.getElementById('confirmarBtn');
    
    // Obtenemos el contexto
    let contexto = '';
    if (document.querySelector('meta[name="contexto"]')) {
        contexto = document.querySelector('meta[name="contexto"]').getAttribute('content');
    } else if (document.getElementById('contexto')) {
        contexto = document.getElementById('contexto').value;
    } else {
        contexto = window.location.pathname.split('/')[1];
        if (contexto) {
            contexto = '/' + contexto;
        }
    }

    // Actualiza contador y estado del botón
    function actualizarContador() {
        const seleccionados = document.querySelectorAll('.vehiculo-check:checked');
        selectedCount.textContent = seleccionados.length + ' vehículos seleccionados';
        eliminarBtn.disabled = seleccionados.length === 0;

        checkboxes.forEach(checkbox => {
            const row = checkbox.closest('tr');
            if (checkbox.checked) {
                row.classList.add('selected-row');
            } else {
                row.classList.remove('selected-row');
            }
        });
    }

    // Selecciona todos
    selectAllBtn.addEventListener('click', function() {
        checkboxes.forEach(checkbox => {
            checkbox.checked = true;
        });
        actualizarContador();
    });

    // Deselecciona todos
    deselectAllBtn.addEventListener('click', function() {
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
        actualizarContador();
    });

    // Actualiza cantidad en el modal al abrirlo
    eliminarBtn.addEventListener('click', function() {
        const seleccionados = document.querySelectorAll('.vehiculo-check:checked');
        cantidadSpan.textContent = seleccionados.length;
    });

    // Confirma eliminación
    confirmarBtn.addEventListener('click', function() {
        const seleccionados = document.querySelectorAll('.vehiculo-check:checked');
        const vehiculosIds = Array.from(seleccionados).map(checkbox => checkbox.value);
        
        if (vehiculosIds.length === 0) {
            return;
        }

        confirmarBtn.disabled = true;
        confirmarBtn.textContent = 'Procesando...';

        const params = new URLSearchParams();
        params.append('accion', 'eliminarVehiculos');
        vehiculosIds.forEach(id => {
            params.append('vehiculosIds[]', id);
        });

        const url = contexto + '/EliminarVehiculoController';

        fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
        .then(response => {
            return response.text();
        })
        .then(text => {
            // Cierra modal Bootstrap
            const modal = bootstrap.Modal.getInstance(document.getElementById('confirmModal'));
            if (modal) modal.hide();
            
            try {
                const data = JSON.parse(text);
                
                if (data.ok) {
                    mensajeDiv.textContent = data.mensaje;
                    mensajeDiv.className = 'mensaje success';
                    mensajeDiv.style.display = 'block';
                    
                    // Borra las filas eliminadas
                    seleccionados.forEach(checkbox => {
                        const row = checkbox.closest('tr');
                        if(row) row.remove();
                    });
                    
                    // Verifica si quedan vehículos
                    if (document.querySelectorAll('.vehiculo-check').length === 0) {
                        setTimeout(() => {
                            window.location.reload();
                        }, 2000);
                    }
                } else {
                    mensajeDiv.textContent = data.mensaje || 'Error desconocido al dar de baja los vehículos';
                    mensajeDiv.className = 'mensaje error';
                    mensajeDiv.style.display = 'block';
                }
            } catch (parseError) {
                if (text.toLowerCase().includes('login') || text.toLowerCase().includes('<!doctype')) {
                    mensajeDiv.textContent = 'Tu sesión ha expirado. Redirigiendo al login...';
                    setTimeout(() => {
                        window.location.href = contexto + '/FrontController?accion=login';
                    }, 2000);
                } else {
                    mensajeDiv.textContent = 'Error del servidor';
                }
                mensajeDiv.className = 'mensaje error';
                mensajeDiv.style.display = 'block';
            }
            
            mensajeDiv.scrollIntoView({ behavior: 'smooth' });
            setTimeout(() => {
                mensajeDiv.style.display = 'none';
            }, 5000);
        })
        .catch(error => {
            mensajeDiv.textContent = 'Error de conexión al dar de baja los vehículos';
            mensajeDiv.className = 'mensaje error';
            mensajeDiv.style.display = 'block';
        })
        .finally(() => {
            confirmarBtn.disabled = false;
            confirmarBtn.textContent = 'Confirmar';
        });
    });

    // Actualiza contador al cambiar selección
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', actualizarContador);
    });

    // Inicializa contador
    actualizarContador();
}); 