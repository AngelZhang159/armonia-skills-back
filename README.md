# Backend Armonia Skills

## Descripción

El backend **Armonia Skills** es una aplicación desarrollada con Spring Boot que sirve como la capa de servidor para la aplicación móvil [Armonia Skills](https://github.com/AngelZhang159/armonia-skills). Este backend ofrece una API REST que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los datos, así como otras funcionalidades esenciales para la interacción entre usuarios.

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Funcionalidades Principales](#funcionalidades-principales)
- [Requisitos](#requisitos)
- [Despliegue](#despliegue)
- [Autenticación](#autenticación)
- [Chat en Tiempo Real](#chat-en-tiempo-real)

## Descripción del Proyecto

Esta aplicación proporciona los endpoints necesarios para que la aplicación [Armonia Skills](https://github.com/AngelZhang159/armonia-skills) funcione correctamente. Los usuarios pueden realizar acciones como crear cuentas, realizar publicaciones, subir fotos y comunicarse en tiempo real con otros usuarios, entre otras funcionalidades.

## Requisitos

- **IDE recomendado:** IntelliJ IDEA
- **Base de datos:** H2 Embedded

## Despliegue

La aplicación se puede desplegar tanto en un entorno local como en un servidor remoto.

### Configuración

Para que la aplicación funcione, es necesario tener un servidor compatible con Spring Data JPA en funcionamiento, por defecto está configurado para usar un servidor H2 embebido. Puedes configurar la conexión a la base de datos local o remota editando el archivo `application.properties` que se encuentra en `src/main/resources/`.

Modifica los siguientes parámetros según tu configuración de base de datos:

```properties
spring.datasource.url=<mysql-url>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### Ejecución Local

1. Clona el proyecto: `git clone <URL_DEL_REPOSITORIO>`
2. Abre el proyecto en IntelliJ IDEA.
3. Ejecuta la aplicación directamente desde tu IDE.

### Despliegue en Servidor

Para desplegar la aplicación en un servidor, sigue estos pasos:

1. Descarga el archivo `.jar` en el apartado de "Releases" o realiza el build del proyecto para generar el archivo `.jar`:
   ```shell
   mvn clean package
   ```
2. Copia el archivo `.jar` generado al servidor.
3. Ejecuta el archivo `.jar` en el servidor con el siguiente comando:
   ```shell
   java -jar armoniaskills.jar
   ```
## Autenticación

La aplicación utiliza JWT (JSON Web Token) para la autenticación de usuarios. Al registrarse, se genera un ID único para el usuario. En el inicio de sesión, el servidor valida las credenciales del usuario y, si son correctas, genera un JWT que incluye el ID único del usuario. Este token es almacenado en las Shared Preferences del dispositivo y debe ser incluido en las cabeceras HTTP para todas las solicitudes posteriores. Si el token está ausente o es inválido, el servidor responderá con un error 401 UNAUTHORIZED y se redirigirá al usuario a la pantalla de inicio de sesión.

## Chat en Tiempo Real

![imagen](https://github.com/user-attachments/assets/16f92b94-ba31-499b-973b-6476481ee5ac)

La aplicación ofrece una funcionalidad de chat en tiempo real mediante WebSockets. Aquí te explicamos cómo funciona:

- **Conexión**: Al abrir el chat, se establece una conexión WebSocket con el servidor. El servidor guarda el ID de la conexión asociado al usuario.
- **Envío de Mensajes**: Cuando un usuario envía un mensaje, el servidor lo intercepta, lo guarda en la base de datos y verifica si el destinatario tiene una conexión activa. Si es así, el mensaje se reenvía instantáneamente. Si el destinatario no está conectado, el mensaje se envía a través de Firebase Cloud Messaging (FCM), permitiendo que el destinatario lo reciba incluso si la aplicación está cerrada.
