# Aether

Plataforma de streaming de películas y series, app nativa para **Android TV y Google TV**.

- Paquete: `com.aether.tv` (**no cambiar nunca**, es lo que permite actualizar sobre versiones anteriores)
- Versión actual: `1.0.0` (se define en `app/build.gradle.kts`: `verMajor/verMinor/verPatch`)
- Stack: Kotlin, Jetpack Compose for TV, minSdk 24

## Identidad visual
Fondo negro profundo `#07080C`, violeta `#8B7CFF`, halo `#E9E6FF`. Logo: una "A" cuyo travesaño es una órbita. Fuentes del logo en `res/drawable/` (ícono, banner) y `ui/AetherLogo.kt` (vector en Compose).

## Estructura
- `ui/`: tema, componentes con foco para control remoto, Splash, Home, Ajustes
- `data/`: modelos y `ContentRepository` (hoy vacío; el backend se enchufa acá sin tocar la UI)
- `update/`: actualizaciones desde GitHub Releases

## Compilar
Requiere JDK 17 y Android SDK. Primera vez: `gradle wrapper --gradle-version 8.9` y subí el wrapper al repo.

```
./gradlew :app:assembleDebug -PgithubOwner=TU_USUARIO
```

## Firma y releases (importante)
1. Creá UNA keystore y guardala en un lugar seguro: **si la perdés, no podrás publicar actualizaciones**.
   `keytool -genkeypair -v -keystore aether.jks -alias aether -keyalg RSA -keysize 2048 -validity 10000`
2. En GitHub, `Settings > Secrets and variables > Actions`:
   `AETHER_KEYSTORE_BASE64` (`base64 -w0 aether.jks`), `AETHER_KEYSTORE_PASSWORD`, `AETHER_KEY_ALIAS`, `AETHER_KEY_PASSWORD`.
3. Para publicar: subí la versión en `app/build.gradle.kts`, commit, y
   `git tag v1.0.0 && git push origin v1.0.0`. El workflow compila, firma y crea el Release con `Aether-v1.0.0.apk`.
4. La app consulta `releases/latest` en Ajustes y ofrece instalar la nueva versión.

Local: copiá `keystore.properties` (ignorado por git) con `storeFile`, `storePassword`, `keyAlias`, `keyPassword`.
