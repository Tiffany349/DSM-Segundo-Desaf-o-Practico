# Odiseo - Aplicación móvil de agencia de viajes
<p align="center">
<strong>Aplicación móvil para la gestión de destinos turísticos</strong>
</p>

---

## Información del proyecto
| Dato | Información |
|---|---|
| **Nombre del proyecto** | Odiseo |
| **Estudiante** | Tiffany Nahomy Benítez Reyes |
| **Tecnología principal** | Android Studio + Kotlin |
| **Base de datos** | Firebase Firestore |
| **Autenticación** | Firebase Authentication |
| **Almacenamiento de imágenes** | Cloudinary |
| **Carga de imágenes** | Glide |

---

##  Descripción
Odiseo es una aplicación móvil desarrollada en Android Studio utilizando Kotlin, diseñada para una agencia de viajes.
La aplicación permite a los usuarios registrarse e iniciar sesión, consultar un catálogo de destinos turísticos y administrar los destinos mediante operaciones de crear, consultar, actualizar y eliminar.
La información de los destinos se almacena en Firebase Firestore, mientras que las imágenes seleccionadas desde la galería se almacenan en Cloudinary.
Para mostrar las imágenes dentro de la aplicación se utiliza Glide.

---

##  Objetivo
Desarrollar una aplicación móvil funcional para una agencia de viajes que permita administrar un catálogo de destinos turísticos mediante un sistema CRUD conectado a Firebase.
La aplicación busca facilitar la administración de destinos y presentar la información de una manera sencilla, organizada y visualmente agradable.

---

##  Tecnologías utilizadas
- Android Studio
- Kotlin
- Firebase Authentication
- Firebase Firestore
- Cloudinary
- Glide
- RecyclerView
- CardView
- Material Design
- Git
- GitHub

---

##  Autenticación
La aplicación utiliza Firebase Authentication para administrar el acceso de los usuarios.

### Registro
Los usuarios pueden crear una cuenta utilizando:
- Correo electrónico
- Contraseña

### Inicio de sesión
Los usuarios registrados pueden iniciar sesión utilizando sus credenciales.
Si los datos son incorrectos, la aplicación muestra un mensaje de error.

### Cierre de sesión
Desde el menú principal el usuario puede cerrar sesión.
Al cerrar sesión, la aplicación finaliza la sesión de Firebase y vuelve a la pantalla de inicio de sesión.

---

##  Menú principal
Después de iniciar sesión, el usuario puede acceder a las siguientes opciones:
- Ver destinos
- Agregar destino
- Editar destinos
- Cerrar sesión

---

##  Gestión de destinos
La aplicación implementa un sistema CRUD completo para los destinos turísticos.

###  Crear destino
El usuario puede registrar un nuevo destino proporcionando:
- Nombre
- País
- Precio
- Descripción
- Imagen
  El país se selecciona mediante un Spinner.

###  Consultar destinos
Los destinos registrados se muestran mediante un RecyclerView utilizando tarjetas.
Cada tarjeta muestra:
- Imagen del destino
- Nombre
- País
- Precio
- Descripción
  Las imágenes son cargadas utilizando Glide.

###  Actualizar destino
La aplicación permite modificar cualquier información de un destino existente:
- Nombre
- País
- Precio
- Descripción
- Imagen
  También es posible cambiar la imagen asociada al destino.
  Cuando se selecciona una nueva imagen, esta se carga en Cloudinary y la nueva URL se actualiza en Firestore.

###  Eliminar destino
La aplicación permite eliminar destinos registrados.
Antes de realizar la eliminación se muestra una ventana de confirmación para evitar que el usuario elimine un destino accidentalmente.

---

##  Validaciones
La aplicación cuenta con validaciones para garantizar que la información ingresada sea correcta.

### Campos obligatorios
Los campos del destino no pueden quedar vacíos.

### Precio
El precio:
- Debe ser numérico.
- Debe ser mayor que 0.

### Descripción
La descripción debe contener como mínimo 20 caracteres.

### Imagen
Es obligatorio seleccionar una imagen antes de guardar el destino.

### Mensajes de error
Cuando existe algún problema, la aplicación muestra un mensaje al usuario indicando la situación.

---

##  Gestión de imágenes
Para almacenar las imágenes se utiliza Cloudinary como alternativa a Firebase Storage.
El proceso funciona de la siguiente manera:
1. El usuario selecciona una imagen desde la galería.
2. La aplicación obtiene la imagen seleccionada.
3. La imagen se carga en Cloudinary.
4. Cloudinary devuelve una URL segura.
5. La URL se guarda en el documento correspondiente de Firestore.
6. Glide utiliza la URL para mostrar la imagen dentro de la aplicación.

---

##  Base de datos
La aplicación utiliza Firebase Firestore.
La colección principal utilizada es: `destinos`

Cada documento contiene los siguientes campos:
- `nombre`
- `pais`
- `precio`
- `descripcion`
- `imagenUrl`

Ejemplo de un destino:
- **nombre:** Playa El Tunco
- **pais:** El Salvador
- **precio:** 150
- **descripcion:** Paquete turístico a Playa El Tunco con alojamiento y actividades.
- **imagenUrl:** URL de la imagen almacenada en Cloudinary

---

##  Estructura principal de la aplicación
<pre>
Odiseo
├── MainActivity
│   └── Inicio de sesión
├── RegistroActivity
│   └── Registro de usuarios
├── InicioActivity
│   └── Menú principal
├── AgregarDestinoActivity
│   └── Registro de destinos
├── ListaDestinosActivity
│   └── Catálogo de destinos
├── EditarDestinosActivity
│   └── Administración de destinos
└── EditarDestinoActivity
    └── Edición de destinos
</pre>
---
##  Funcionamiento general
<pre>
Inicio
│
├── Iniciar sesión
│       │
│       └── Menú principal
│              │
│              ├── Ver destinos
│              │
│              ├── Agregar destino
│              │
│              ├── Editar destinos
│              │
│              └── Cerrar sesión
│
└── Registrarse
        │
        └── Crear cuenta
</pre>
---
##  Operaciones CRUD
La aplicación implementa las cuatro operaciones principales de un CRUD:

| Operación | Función |
|---|---|
| **Create** | Registrar un nuevo destino |
| **Read** | Consultar y mostrar destinos |
| **Update** | Editar un destino |
| **Delete** | Eliminar un destino |

Todas estas operaciones están conectadas con Firebase Firestore.

---

##  Diseño de la aplicación
La aplicación utiliza una interfaz basada en Material Design.
Se implementó una paleta de colores personalizada basada principalmente en tonos rosados y colores complementarios.
La aplicación también cuenta con soporte para:
- Modo claro
- Modo oscuro
- Diseño adaptable
- Tarjetas para mostrar destinos
- Imágenes de destinos
- Botones para las principales acciones
- Icono personalizado
- Nombre personalizado de la aplicación

---

##  Pruebas realizadas
Durante el desarrollo se realizaron pruebas de:
- Registro de usuarios.
- Inicio de sesión.
- Cierre de sesión.
- Registro de destinos.
- Validación de campos vacíos.
- Validación del precio.
- Validación de la descripción.
- Validación de imagen obligatoria.
- Selección de país mediante Spinner.
- Selección de imágenes desde la galería.
- Carga de imágenes a Cloudinary.
- Guardado de información en Firestore.
- Visualización de destinos.
- Carga de imágenes mediante Glide.
- Edición de destinos.
- Cambio de imagen.
- Eliminación de destinos.
- Confirmación antes de eliminar.
- Navegación entre pantallas.
- Funcionamiento del modo oscuro.

---

##  Requisitos para ejecutar el proyecto
Para ejecutar el proyecto se necesita:
- Android Studio
- Kotlin
- JDK compatible con la configuración del proyecto
- Una cuenta de Firebase
- Firebase Authentication habilitado
- Firebase Firestore habilitado
- Configuración de Cloudinary
- Dispositivo Android o emulador

---

##  Configuración de Firebase
<pre>

El proyecto utiliza Firebase Authentication y Firebase Firestore.
Para configurar el proyecto con Firebase se debe agregar el archivo:
`google-services.json`
dentro de:
`app/`

También es necesario habilitar en Firebase:

Authentication
└── Sign-in method
└── Email/Password

Y crear:
`Firestore Database`
</pre>

---

##  Configuración de Cloudinary
El proyecto utiliza Cloudinary para almacenar las imágenes.
Configuración utilizada:
- **Cloud Name:** yptipn1x
- **Upload Preset:** odiseo_images

El Upload Preset utilizado permite realizar la carga de imágenes desde la aplicación.

---

##  Estructura del proyecto
<pre>

Odiseo
│
├── app
│   └── src
│       └── main
│           ├── java
│           │   └── com.example.odiseo
│           │
│           ├── res
│           │   ├── layout
│           │   ├── drawable
│           │   ├── mipmap
│           │   └── values
│           │
│           └── AndroidManifest.xml
│
├── gradle
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
</pre>

---

##  APK
<pre>

La versión funcional más reciente de la aplicación se encuentra disponible en el repositorio.
Ubicación:
APK/
└── Odiseo.apk
</pre>

---

##  Video de defensa
Video de demostración y defensa del proyecto:
**PENDIENTE: AGREGAR AQUÍ EL ENLACE DEL VIDEO**

El video presenta el funcionamiento general de la aplicación y las principales funcionalidades implementadas.

---

##  Repositorio de GitHub
Repositorio oficial del proyecto:
**PENDIENTE: AGREGAR AQUÍ EL ENLACE DEL REPOSITORIO**

---

##  Autora
**Tiffany Nahomy Benítez Reyes**

Proyecto académico desarrollado utilizando:
Android Studio + Kotlin + Firebase + Cloudinary

---

##  Conclusión
Odiseo permite administrar un catálogo de destinos turísticos desde una aplicación móvil, integrando autenticación de usuarios, almacenamiento de información en la nube, almacenamiento de imágenes y operaciones CRUD.
Mediante Firebase Firestore se administran los datos de los destinos, mientras que Cloudinary permite almacenar las imágenes y Glide facilita su visualización dentro de la aplicación.
El resultado es una aplicación funcional que permite gestionar destinos turísticos de manera sencilla, organizada y visual.