# BasketShotTracker

**Alumno:** Nikolay Kovalev  
**Comisión:** ACN4AV

BasketShotTracker es una aplicación Android para registrar entrenamientos de básquet y consultar el rendimiento de los tiros realizados.

## Funciones principales

- Registro de tiros acertados y fallados.
- Entrenamientos por tipo de tiro: juntos, libres, campo y triples.
- Cálculo de porcentaje de aciertos, racha máxima y tiempo promedio por tiro.
- Historial de entrenamientos y visualización de estadísticas.
- Registro e inicio de sesión con Firebase Authentication.
- Perfil de usuario almacenado en Firestore.
- Copia de las sesiones finalizadas en Firestore.
- Almacenamiento local de entrenamientos con SharedPreferences.
- Imagen de perfil cargada desde una URL mediante Glide, con imagen local de respaldo.

## Tecnologías utilizadas

- Java
- Android Studio
- XML
- Firebase Authentication
- Cloud Firestore
- SharedPreferences
- Glide

## Cómo ejecutar el proyecto

1. Clonar el repositorio.
2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar a que Gradle sincronice las dependencias.
4. Verificar que `app/google-services.json` esté disponible para la configuración de Firebase.
5. Ejecutar la aplicación en un emulador o dispositivo con Android 7.0 (API 24) o superior.

También se puede generar el APK de depuración desde la raíz del proyecto:

```bash
./gradlew assembleDebug
```

En Windows:

```powershell
.\gradlew.bat assembleDebug
```

## Repositorio final

https://github.com/kovalev-nikolay/final-am-acn4av-kovalev