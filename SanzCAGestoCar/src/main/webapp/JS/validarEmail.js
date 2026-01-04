// Referencia a registro.jsp
function mostrarNotificacion() {
    const notif = document.getElementById('notif-exito');
    if (notif) {
        notif.style.display = 'block';
        setTimeout(() => {
            notif.style.display = 'none';
        }, 3000);
    }
}

function validarEmail() {
    const email = document.getElementById('email').value.trim();
    const mensajeEmail = document.getElementById('mensajeEmail');
    
    if (email === '') {
        mensajeEmail.style.display = 'none';
        return true;
    }
    
    const formatoEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formatoEmail.test(email)) {
        mensajeEmail.textContent = 'Formato de email inválido. Debe incluir @ y un dominio válido (ej: usuario@gmail.com)';
        mensajeEmail.style.display = 'block';
        return false;
    }
    
    mensajeEmail.style.display = 'none';
    return true;
}

// Validación de contraseñas coincidentes
document.addEventListener('DOMContentLoaded', function() {
    const password = document.getElementById('password');
    const confirmarPassword = document.getElementById('confirmarPassword');
    const mensajePassword = document.getElementById('mensajePassword');
    const email = document.getElementById('email');
    const formulario = document.querySelector('.form-registro');

    function validarPasswords() {
        const pass1 = password.value;
        const pass2 = confirmarPassword.value;
        
        if (pass2 === '') {
            mensajePassword.style.display = 'none';
            return true; 
        }
        
        if (pass1 !== pass2) {
            mensajePassword.textContent = 'Las contraseñas no coinciden';
            mensajePassword.style.display = 'block';
            return false;
        } else {
            mensajePassword.style.display = 'none';
            return true;
        }
    }

    // Valida email cuando se escriba
    if (email) {
        email.addEventListener('input', validarEmail);
        email.addEventListener('blur', validarEmail);
    }

    // Valida cuando se escriba en cualquiera de los campos
    if (password) password.addEventListener('input', validarPasswords);
    if (confirmarPassword) confirmarPassword.addEventListener('input', validarPasswords);

    // Valida antes de enviar el formulario
    if (formulario) {
        formulario.addEventListener('submit', function(e) {
            let esValido = true;
            
            // Valida email
            if (!validarEmail()) {
                esValido = false;
            }
            
            // Valida contraseñas
            if (!validarPasswords()) {
                esValido = false;
            }
            
            if (!esValido) {
                e.preventDefault();
                // Enfoca el primer campo con error
                if (document.getElementById('mensajeEmail').style.display === 'block') {
                    email.focus();
                } else if (document.getElementById('mensajePassword').style.display === 'block') {
                    confirmarPassword.focus();
                }
            }
        });
    }
}); 

