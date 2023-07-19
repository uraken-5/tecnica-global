
# Descripción del Programa de Gestión de Usuarios con Autenticación Basada en Tokens

Este programa es una aplicación desarrollada en **Spring Boot** para la gestión de usuarios con autenticación basada en tokens. Su objetivo principal es permitir a los usuarios registrarse (*sign-up*) y autenticarse (*login*) para acceder a ciertos recursos protegidos dentro de la aplicación.

## Registro de Usuarios

Cuando un usuario desea registrarse, envía una solicitud HTTP al endpoint `/sign-up` proporcionando su información personal, como nombre, correo electrónico, contraseña y detalles de teléfono. La información recibida es validada y luego se almacena en una base de datos. Si el correo electrónico ya está registrado previamente, se produce una excepción para evitar registros duplicados.

## Autenticación de Usuarios

Una vez registrado, el usuario puede iniciar sesión en la aplicación enviando un token de autenticación en el encabezado `Authorization` al endpoint `/login`. El servidor verifica la validez del token y también comprueba si el token no ha sido revocado. Si el token es válido y no está revocado, el servidor actualiza el token del usuario y genera un nuevo token para la sesión actual. Luego, se devuelve una respuesta con los detalles del usuario y el nuevo token de sesión.

## Uso de ModelMapper

La aplicación utiliza la biblioteca `ModelMapper` para mapear automáticamente los datos entre las clases de Entidad (por ejemplo, `User`) y las clases de Transferencia de Datos (por ejemplo, `UserDto`). Esto simplifica el proceso de conversión de datos entre las distintas capas de la aplicación.

## Resumen

En resumen, el programa proporciona una API RESTful que permite a los usuarios registrarse, autenticarse y obtener acceso a recursos protegidos mediante el uso de tokens. La autenticación basada en tokens garantiza la seguridad de las comunicaciones y permite a los usuarios realizar diversas acciones en la aplicación según su estado de autenticación y autorización.

## Imágenes

A continuación, puedes agregar imágenes relacionadas con el programa:

![Imagen 1](url_imagen_1.png)

![Imagen 2](url_imagen_2.png)

