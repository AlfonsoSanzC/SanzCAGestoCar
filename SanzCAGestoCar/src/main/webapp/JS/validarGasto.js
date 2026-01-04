// Referencia a introducirGasto.jsp
// Establece fecha actual por defecto cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    const fechaInput = document.getElementById('fechaGasto');
    if (!fechaInput.value) {
        const today = new Date().toISOString().split('T')[0];
        fechaInput.value = today;
    }
});

// Función para mostrar mensajes de error
function mostrarError(mensaje) {
    // Elimina mensaje anterior si existe
    const mensajeAnterior = document.getElementById('mensajeValidacion');
    if (mensajeAnterior) {
        mensajeAnterior.remove();
    }
    
    // Crea nuevo mensaje de error
    const mensajeDiv = document.createElement('div');
    mensajeDiv.id = 'mensajeValidacion';
    mensajeDiv.className = 'alert alert-danger mt-3';
    mensajeDiv.innerHTML = '<i class="fas fa-exclamation-triangle me-2"></i>' + mensaje;
    
    // Inserta el mensaje antes del formulario
    const form = document.querySelector('form[action*="GastoController"]');
    if (form) {
        form.parentNode.insertBefore(mensajeDiv, form);
        
        mensajeDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        
        // Auto-ocultar después de 5 segundos
        setTimeout(function() {
            if (mensajeDiv) {
                mensajeDiv.style.transition = 'opacity 0.5s';
                mensajeDiv.style.opacity = '0';
                setTimeout(function() {
                    if (mensajeDiv && mensajeDiv.parentNode) {
                        mensajeDiv.remove();
                    }
                }, 500);
            }
        }, 5000);
    }
}

// Validación del formulario antes del envío
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[action*="GastoController"]');
    
    if (form) {
        form.addEventListener('submit', function(e) {
            const importe = document.getElementById('importe').value;
            const concepto = document.getElementById('concepto').value.trim();
            const descripcion = document.getElementById('descripcion').value.trim();
            const vehiculo = document.getElementById('idVehiculo').value;
            const fechaGasto = document.getElementById('fechaGasto').value;
            
            // Valida vehículo seleccionado
            if (!vehiculo) {
                mostrarError('Debes seleccionar un vehículo.');
                e.preventDefault();
                return;
            }
            
            // Valida concepto
            if (!concepto) {
                mostrarError('El concepto es obligatorio.');
                e.preventDefault();
                return;
            }
            
            // Valida descripción
            if (!descripcion) {
                mostrarError('La descripción es obligatoria.');
                e.preventDefault();
                return;
            }
            
            // Valida fecha
            if (!fechaGasto) {
                mostrarError('La fecha del gasto es obligatoria.');
                e.preventDefault();
                return;
            }
            
            // Valida importe
            if (!importe || parseFloat(importe) <= 0) {
                mostrarError('El importe debe ser mayor que 0.');
                e.preventDefault();
                return;
            }
            
            // Valida que el importe no supere el límite de la base de datos
            if (parseFloat(importe) > 9999.99) {
                mostrarError('El importe no puede superar 9999.99€.');
                e.preventDefault();
                return;
            }
            
            // Valida que el importe sea un número válido
            if (isNaN(parseFloat(importe))) {
                mostrarError('El importe debe ser un número válido.');
                e.preventDefault();
                return;
            }
        });
    }
}); 