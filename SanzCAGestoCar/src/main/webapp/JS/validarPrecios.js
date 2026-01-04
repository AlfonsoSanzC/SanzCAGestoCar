// Referencia a menuUsuario.jsp
document.addEventListener('DOMContentLoaded', function () {
    const precioCompraInput = document.getElementById('precioCompra');
    const precioVentaInput = document.getElementById('precioVenta');
    const precioCompraValidation = document.getElementById('precioCompraValidation');
    const precioVentaValidation = document.getElementById('precioVentaValidation');
    const form = document.getElementById('formCrearVehiculo');

    // Función para validar precio
    function validarPrecio(valor) {
        // Verifica si está vacío
        if (!valor || valor.trim() === '') {
            return null;
        }

        // Verifica si es un número válido
        const numero = parseFloat(valor.replace(',', '.'));
        if (isNaN(numero)) {
            return false;
        }

        // Verifica si es mayor que 1
        if (numero <= 1) {
            return false;
        }

        return true;
    }

    // Muestra un mensaje de validación
    function mostrarValidacion(elemento, esValido, esOpcional = false, estaVacio = false) {
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

    // Valida precio de compra 
    precioCompraInput.addEventListener('input', function () {
        const valor = this.value.trim();
        const resultado = validarPrecio(valor);

        if (resultado === null) {
            mostrarValidacion(precioCompraValidation, false);
            precioCompraValidation.textContent = '✗ Campo obligatorio';
        } else {
            mostrarValidacion(precioCompraValidation, resultado);
        }
    });

    // Valida precio de venta 
    precioVentaInput.addEventListener('input', function () {
        const valor = this.value.trim();
        const resultado = validarPrecio(valor);

        if (resultado === null) {
            mostrarValidacion(precioVentaValidation, true, true, true);
        } else {
            mostrarValidacion(precioVentaValidation, resultado);
        }
    });

    // Valida antes de enviar el formulario
    form.addEventListener('submit', function (e) {
        const precioCompra = precioCompraInput.value.trim();
        const precioVenta = precioVentaInput.value.trim();

        let esValido = true;

        // Valida precio de compra 
        const resultadoCompra = validarPrecio(precioCompra);
        if (resultadoCompra !== true) {
            if (resultadoCompra === null) {
                mostrarValidacion(precioCompraValidation, false);
                precioCompraValidation.textContent = '✗ Campo obligatorio';
            } else {
                mostrarValidacion(precioCompraValidation, false);
            }
            esValido = false;
        }

        // Valida precio de venta 
        if (precioVenta) {
            const resultadoVenta = validarPrecio(precioVenta);
            if (resultadoVenta !== true) {
                mostrarValidacion(precioVentaValidation, false);
                esValido = false;
            }
        }

        if (!esValido) {
            e.preventDefault();
            const primerError = document.querySelector('.price-invalid');
            if (primerError) {
                primerError.scrollIntoView({behavior: 'smooth', block: 'center'});
            }
        }
    });

    // Permite solo números, puntos y comas en los campos de precio
    [precioCompraInput, precioVentaInput].forEach(input => {
        input.addEventListener('keypress', function (e) {
            const char = String.fromCharCode(e.which);
            if (!/[0-9.,]/.test(char) && e.which !== 8 && e.which !== 0) {
                e.preventDefault();
            }
        });

        // Formatea el precio al perder el foco
        input.addEventListener('blur', function () {
            const valor = this.value.trim();
            if (valor && !isNaN(parseFloat(valor.replace(',', '.')))) {
                const numero = parseFloat(valor.replace(',', '.'));
                this.value = numero.toFixed(2);
            }
        });
    });
}); 