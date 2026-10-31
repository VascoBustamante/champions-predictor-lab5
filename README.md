# Laboratorio 5 — Pregunta 1 — Champions Predictor

Aplicación Java 21 / Spring Boot 3.5.7, en arquitectura de tres capas, con API simulada, PostgreSQL, Docker y AWS.

## Arquitectura

- **Presentación**: `WebController` + plantillas Thymeleaf (`templates`) y CSS.
- **Negocio**: `PartidoService` importa partidos al iniciar; `PrediccionService` valida el resultado y guarda pronósticos.
- **Acceso a datos**: repositorios Spring Data JPA, entidades Partido y Prediccion, PostgreSQL.

Flujo de datos: `nginx (API simulada JSON) -> PartidoService (GET al iniciar) -> PartidoRepository -> PostgreSQL`. El usuario interactúa con `Thymeleaf -> WebController -> PrediccionService -> PrediccionRepository -> PostgreSQL`. Las tablas se crean mediante JPA (`ddl-auto=update`, adecuado para laboratorio).

Los partidos del JSON son **inventados para demostración**, no un calendario oficial de la UEFA.

## Requisitos

- IntelliJ IDEA, JDK 21 y Docker Desktop con Docker Compose v2.
- Opcional: Maven instalado localmente (para compilar fuera de Docker).

## Ejecución local completa (PowerShell, desde la carpeta del proyecto)

```powershell
docker compose up --build -d
docker compose ps
docker compose logs -f app
```

Abrir `http://localhost:8080` y `http://localhost:8080/predicciones`.

Para detener sin borrar datos: `docker compose down`. Para reiniciar: `docker compose up -d`.

**ADVERTENCIA**: `docker compose down -v` borra las predicciones de la base de datos.

## Ejecutar Java desde IntelliJ (modo desarrollo)

Iniciar solo PostgreSQL y API:

```powershell
docker compose up -d db mock-api
```

Abrir proyecto con `File > Open` seleccionando la carpeta, esperar la sincronización Maven, configurar SDK 21 e iniciar `ChampionsApplication.java` con el botón de reproducción. Usa `localhost:5433` y `localhost:8091` por defecto. No iniciar simultáneamente el servicio `app` de Compose porque ocupará el puerto 8080.

## Verificación de requisitos

1. `http://localhost:8091/partidos.json` muestra API simulada (solo en local).
2. `http://localhost:8080/api/partidos` muestra partidos importados a PostgreSQL.
3. En la portada, escoger un partido, resultado y goles coherentes (p.ej. LOCAL, 2-1), guardar.
4. `http://localhost:8080/predicciones` lista los pronósticos.
5. `docker compose restart app`; las predicciones persisten.
6. Base de datos: `docker compose exec db psql -U champions -d championsdb -c "SELECT * FROM predicciones;"` (cambiar usuario si corresponde).

## Docker Hub

```powershell
docker login
docker build -t TU_USUARIO_DOCKERHUB/champions-predictor:1.0 .
docker push TU_USUARIO_DOCKERHUB/champions-predictor:1.0
```

## AWS EC2

Crear instancia Ubuntu, abrir el puerto 22 solo desde tu IP y 8080 para la demostración, instalar Docker Engine y Docker Compose Plugin siguiendo https://docs.docker.com/engine/install/ubuntu/ . Clonar repositorio y configurar `.env` basado en `.env.example` con contraseña fuerte y el nombre real de la imagen publicada. Ejecutar:

```bash
docker login
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d
docker compose -f docker-compose.prod.yml ps
```

Abrir `http://IP_PUBLICA:8080`. El archivo `docker-compose.prod.yml` utiliza la imagen remota; no compila en la instancia. El usuario de Docker Hub debe haber publicado la imagen antes.

Seguridad para demostración: no subir `.env`, no exponer PostgreSQL, limitar SSH a IP de confianza y eliminar recursos de AWS al finalizar para evitar cargos. En un entorno real usar HTTPS con dominio y proxy inverso; este laboratorio usa HTTP por simplicidad.

## Evidencias sugeridas

- UI de partidos; predicción guardada; lista de predicciones; respuesta de `/api/partidos`.
- Diagrama de arquitectura; `docker compose ps`; Docker Hub con imagen publicada.
- EC2 ejecutándose; web funcionando a través de IP pública; volumen `postgres_data`.
- Video de hasta cinco minutos mostrando estos puntos.
