// JavaScript para menuAdmin.jsp

document.addEventListener('DOMContentLoaded', function () {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', function () {
            const targetTab = this.getAttribute('data-tab');

            // Borra clase active de todos los botones y contenidos
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            // Agrega la clase active al botón clickeado y su contenido
            this.classList.add('active');
            document.getElementById(targetTab).classList.add('active');

            // Carga datos según la pestaña
            if (targetTab === 'tab-usuarios') {
                cargarUsuarios();
            } else if (targetTab === 'tab-vehiculos') {
                cargarVehiculos();
            }
        });
    });

    cargarUsuarios();
});

function cargarUsuarios() {
    mostrarCargando('tabla-usuarios');

    fetch('AdminController', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'accion=listarUsuarios'
    })
            .then(response => response.json())
            .then(usuarios => {
                let html = '';
                usuarios.forEach(usuario => {
                    const estado = usuario.campoBaja ? 'Baja' : 'Alta';
                    const claseEstado = usuario.campoBaja ? 'estado-baja' : 'estado-alta';
                    const botonAccion = usuario.campoBaja
                            ? `<button class="btn-accion btn-activar" onclick="cambiarEstadoUsuario(${usuario.idUsuario}, true)">
                     <i class="fa fa-check"></i> Activar
                   </button>`
                            : `<button class="btn-accion btn-desactivar" onclick="mostrarModalConfirmacion(${usuario.idUsuario})">
                     <i class="fa fa-trash"></i> Desactivar
                   </button>`;

                    // Muestra imágenes del avatar y carnet como archivos públicos
                    // Avatar: archivo en carpeta pública
                    const avatarImg = usuario.avatar
                            ? `<img src="${window.appContext}/IMG/avatares/${usuario.avatar}" alt="Avatar" class="img-miniatura">`
                            : '<span class="sin-imagen">Sin imagen</span>';

                    // Carnet: archivo en carpeta pública también (cambiaremos la ubicación)
                    const carnetImg = usuario.carneConducir && usuario.carneConducir !== 'default-carnet.png'
                            ? `<img src="${window.appContext}/IMG/carnets/${usuario.carneConducir}" alt="Carnet" class="img-miniatura">`
                            : '<span class="sin-imagen">Sin imagen</span>';

                    html += `
                <tr>
                    <td>${usuario.idUsuario}</td>
                    <td>${avatarImg}</td>
                    <td>${carnetImg}</td>
                    <td>${usuario.nombre}</td>
                    <td>${usuario.apellidos}</td>
                    <td>${usuario.email}</td>
                    <td>${usuario.dni}</td>
                    <td><span class="${claseEstado}">${estado}</span></td>
                    <td>${botonAccion}</td>
                </tr>
            `;
                });
                document.getElementById('tabla-usuarios').innerHTML = html;
            })
            .catch(error => {
                console.error('Error:', error);
                mostrarToast('Error al cargar usuarios', 'error');
                document.getElementById('tabla-usuarios').innerHTML = '<tr><td colspan="9">Error al cargar usuarios</td></tr>';
            });
}

function cargarVehiculos() {
    mostrarCargando('tabla-vehiculos');

    fetch('AdminController', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'accion=listarVehiculos'
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(text => {
                try {
                    const response = JSON.parse(text);

                    // Verificar si es un objeto de error
                    if (response.ok === false) {
                        throw new Error(response.mensaje || 'Error del servidor');
                    }

                    // Verifica si es un array,(lista de vehículos) o un objeto de error
                    if (!Array.isArray(response)) {
                        console.error('La respuesta no es un array:', response);
                        throw new Error('Formato de respuesta inválido');
                    }

                    const vehiculos = response;
                    let html = '';
                    if (vehiculos.length === 0) {
                        html = '<tr><td colspan="12" style="text-align: center; padding: 20px;">No hay vehículos registrados</td></tr>';
                    } else {
                        vehiculos.forEach(vehiculo => {
                            const fechaCompra = vehiculo.fechacompra ? formatearFecha(vehiculo.fechacompra) : '-';
                            const fechaVenta = vehiculo.fechaventa ? formatearFecha(vehiculo.fechaventa) : '-';
                            const precioCompra = vehiculo.preciocompra ? formatearPrecio(vehiculo.preciocompra) : '-';
                            const precioVenta = vehiculo.precioventa ? formatearPrecio(vehiculo.precioventa) : '-';

                            html += `
                        <tr>
                            <td>${vehiculo.propietario}</td>
                            <td>${vehiculo.marca || '-'}</td>
                            <td>${vehiculo.modelo || '-'}</td>
                            <td>${vehiculo.motor || '-'}</td>
                            <td>${vehiculo.matricula || '-'}</td>
                            <td>${vehiculo.cilindrada || '-'}</td>
                            <td>${vehiculo.caballos || '-'}</td>
                            <td>${vehiculo.color || '-'}</td>
                            <td>${fechaCompra}</td>
                            <td>${fechaVenta}</td>
                            <td>${precioCompra}</td>
                            <td>${precioVenta}</td>
                        </tr>
                    `;
                        });
                    }
                    document.getElementById('tabla-vehiculos').innerHTML = html;
                } catch (parseError) {
                    console.error('Error parsing JSON:', parseError);
                    throw new Error('Error al procesar la respuesta del servidor');
                }
            })
            .catch(error => {
                console.error('Error completo:', error);
                mostrarToast('Error al cargar vehículos: ' + error.message, 'error');
                document.getElementById('tabla-vehiculos').innerHTML = '<tr><td colspan="12">Error al cargar vehículos: ' + error.message + '</td></tr>';
            });
}

function mostrarModalConfirmacion(idUsuario) {
    const modal = document.getElementById('modalConfirmacion');
    modal.style.display = 'block';

    document.getElementById('btnConfirmar').onclick = function () {
        cambiarEstadoUsuario(idUsuario, false);
        cerrarModal();
    };
}

function cerrarModal() {
    document.getElementById('modalConfirmacion').style.display = 'none';
}

function cambiarEstadoUsuario(idUsuario, activar) {
    fetch('AdminController', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `accion=cambiarEstadoUsuario&idUsuario=${idUsuario}&activar=${activar}`
    })
            .then(response => response.json())
            .then(data => {
                if (data.ok) {
                    mostrarToast(data.mensaje, 'success');
                    cargarUsuarios();
                } else {
                    mostrarToast(data.mensaje || 'Error al cambiar estado', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                mostrarToast('Error de conexión', 'error');
            });
}

function mostrarToast(mensaje, tipo) {
    const toast = document.createElement('div');
    toast.className = `toast ${tipo}`;
    toast.textContent = mensaje;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000);
}

function mostrarCargando(idTabla) {
    const colspan = idTabla === 'tabla-usuarios' ? '9' : '12';
    document.getElementById(idTabla).innerHTML = `<tr><td colspan="${colspan}" class="loading"><div class="spinner"></div>Cargando...</td></tr>`;
}

function formatearFecha(fecha) {
    if (!fecha)
        return '-';
    return new Date(fecha).toLocaleDateString('es-ES');
}

function formatearPrecio(precio) {
    if (!precio)
        return '-';
    return parseFloat(precio).toFixed(2) + ' €';
}

window.onclick = function (event) {
    const modal = document.getElementById('modalConfirmacion');
    if (event.target == modal) {
        cerrarModal();
    }
} 