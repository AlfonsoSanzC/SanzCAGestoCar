<h1 align="center">#GestoCar</h1>
GestoCar es una aplicación web desarrollada en Java que permite gestionar los gastos asociados a varios vehículos de diferentes usuarios. La aplicación está diseñada para ser modular, extensible y fácil de usar, siguiendo buenas prácticas de arquitectura y desarrollo.

## Las tecnologías utilizadas son:

* Java 

* HTML/CSS

* JDK 11

*  Java 7 EE

*  ## Entorno de desarrollo

* Tomcat 9

* Netbeans 19

* ## Construido con 🛠️

  Las herramientas que utilicé para crear este proyecto:

* [Maven](https://maven.apache.org/) - Gestor de dependencias y herramienta de construcción.

## Descripción
GestoCar permite a los usuarios registrados llevar un control detallado de sus vehículos y de todos los gastos relacionados con ellos (combustible, mantenimiento, reparaciones, etc.). El administrador de la aplicación puede gestionar tanto usuarios como vehículos.

## 🚀 Funcionalidades principales

### 👥 Gestión de usuarios

- 📝 Registro de nuevos usuarios con validación **AJAX**.
- 🔐 Inicio de sesión con contraseñas cifradas en **MD5**.
- ⚙️ Modificación y activación/desactivación de usuarios (por el **administrador**).

### 🚗 Gestión de vehículos

- ➕ Alta, ✏️ modificación y 🗑️ baja lógica de vehículos.
- 📸 Subida de fotos asociadas a los vehículos.
- 🔍 Visualización completa de la información de cada vehículo.

### 💸 Gestión de gastos

- ➕ Alta de nuevos gastos con uso de **datalist** para conceptos.
- 📄 Visualización de gastos con **paginación AJAX**.
- 🔎 Filtros avanzados de búsqueda:
  - Fecha
  - Concepto
  - Importe
  - Establecimiento
  - Descripción
- 📊 Visualización de **totales** por:
  - Concepto
  - Año
  - Importe
- ✏️ Modificación y 🗑️ eliminación de gastos con confirmación.

---

## 🛠️ Aspectos técnicos

- 🏛️ Arquitectura basada en los patrones **MVC**, **Factory** y **DTO**.
- 🐘 Uso de **Apache Maven** para la gestión del proyecto.
- 🎩 Captura automática de datos de formularios con **BeanUtils**.
- 🎨 Vistas desarrolladas con:
  - **JSP estándar**
  - **JSTL**
  - **jQuery/JavaScript**
- 📱 Interfaz **responsive** y moderna.
- ⚡ Implementación de **AJAX** para una experiencia de usuario fluida.
- 🖼️ Soporte de subida y gestión de imágenes (**avatars** y documentos).

---

## 🗄️ Base de datos

La aplicación gestiona las siguientes tablas:

- 📋 `usuarios`
- 🚗 `vehiculos`
- 🖼️ `fotos`
- 💸 `gastos`

---

## 👤 Usuario administrador por defecto

| Email 📧                      | Contraseña 🔑 |
|-------------------------------|---------------|
| admin@iesalbarregas.es         | admin         |

## Autores

<h4>Alfonso Sanz Carmona, 2ºDAW B.</h4>

<sub>(https://github.com/AlfonSanzC)</sub> 
