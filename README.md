# Laboratorio 5 — Pregunta 1 — Champions Predictor

Aplicación web desarrollada en **Java 21 y Spring Boot 3.5.7** para la gestión de partidos de demostración de Champions League y registro de predicciones. Implementa arquitectura de **tres capas**, PostgreSQL, Docker Compose y despliegue en AWS EC2.

**Repositorio:** https://github.com/VascoBustamante/champions-predictor-lab5  
**Imagen publicada:** https://hub.docker.com/r/vascobustamante/champions-predictor (`1.0`)  
**Demo en AWS (mientras EC2 esté encendida y conserve su IP):** http://3.147.28.41:8080

> Los seis encuentros son **datos ficticios de prueba**, obtenidos de una API simulada. No representan un calendario oficial de UEFA.

## Arquitectura y flujo de datos

| Capa | Componentes | Responsabilidad |
|---|---|---|
| Presentación | `WebController`, `ApiController`, Thymeleaf, HTML/CSS | Lista partidos, recibe formularios y ofrece endpoints JSON. |
| Lógica de negocio | `PartidoService`, `PrediccionService` | Importa partidos al arrancar, valida marcador/ganador y registra predicciones. |
| Acceso a datos | `PartidoRepository`, `PrediccionRepository`, Spring Data JPA, PostgreSQL | Almacena y recupera partidos y predicciones. |

**Carga inicial:** contenedor `mock-api` (nginx) → `GET /partidos.json` → `PartidoService` (`ApplicationRunner`) → `PartidoRepository` → PostgreSQL. Al reiniciar, el servicio evita duplicados mediante `externalId`.

**Predicción:** navegador → `POST /predicciones` → `WebController` → `PrediccionService` → validación (el ganador/empate debe coincidir con los goles) → `PrediccionRepository` → PostgreSQL → confirmación.

**Consulta:** navegador → `GET /predicciones` → `WebController` → `PrediccionService` → repositorio → PostgreSQL → plantilla `predicciones.html`.

La base de datos reside en el volumen Docker `postgres_data`. `docker compose down` no elimina los datos; **`docker compose down -v` sí los elimina**.

## Requisitos

- Java JDK 21 e IntelliJ IDEA (opcional para ejecución Docker).
- Docker Desktop con Docker Compose v2 para Windows.
- Cuenta en Docker Hub y AWS solo para publicación y despliegue.

## Ejecución local (Windows / IntelliJ)

Abrir la carpeta que contiene `docker-compose.yml` y ejecutar en PowerShell:

```powershell
docker compose up --build -d
docker compose ps
docker compose logs --tail=50 app
```

Visitar: `http://localhost:8080`, `http://localhost:8080/predicciones` y `http://localhost:8080/api/partidos`.

**Importante:** En el equipo utilizado para este laboratorio, Windows bloqueó el puerto 5433. Se cambió **solo el puerto publicado de PostgreSQL** en `docker-compose.yml` a:

```yaml
ports:
  - "127.0.0.1:5435:5432"
```

Si ocurre el mismo error en otro equipo, aplicar este cambio. No afecta la conexión entre contenedores, que continúa utilizando `db:5432`. Si se ejecuta **Java fuera de Docker**, configurar la URL de base de datos a `jdbc:postgresql://localhost:5435/championsdb` y mantener la API en `http://localhost:8091/partidos.json`. Antes, detener el servicio `app` de Docker (`docker compose stop app`) y dejar `db` y `mock-api` encendidos. Puede configurarse mediante la variable `DB_URL`.

Para detener sin eliminar el volumen:

```powershell
docker compose down
```

## Verificación funcional

1. Ir a `/` y comprobar que aparecen seis partidos de prueba.
2. Abrir `/api/partidos` y comprobar la respuesta JSON.
3. Registrar una predicción coherente, por ejemplo resultado LOCAL, 2-1.
4. Abrir `/predicciones` y comprobar que aparece.
5. Comprobar las tablas y la persistencia con los siguientes comandos:

```powershell
docker compose exec db psql -U champions -d championsdb -c "SELECT COUNT(*) FROM partidos;"
docker compose exec db psql -U champions -d championsdb -c "SELECT COUNT(*) FROM predicciones;"
docker compose restart
docker compose exec db psql -U champions -d championsdb -c "SELECT COUNT(*) FROM predicciones;"
```

En la ejecución local de prueba se verificaron **6 partidos y 1 predicción conservada tras el reinicio**. En AWS se verificó el listado de partidos y los tres contenedores activos; el registro/consulta de una predicción en AWS debe demostrarse como evidencia final.

## Docker Hub

Imagen publicada: `vascobustamante/champions-predictor:1.0`.

Para reconstruir y publicar una nueva versión desde Windows:

```powershell
docker login
docker build -t vascobustamante/champions-predictor:1.0 .
docker push vascobustamante/champions-predictor:1.0
```

## Despliegue en AWS EC2

- Instancia EC2 Ubuntu 24.04 LTS, `t3.small`, 20 GiB de volumen gp3.
- Grupo de seguridad: TCP 22 restringido a IP autorizada, TCP 8080 habilitado para demostración; no abrir PostgreSQL al exterior.
- Instalar Docker Engine y Compose plugin siguiendo https://docs.docker.com/engine/install/ubuntu/ .
- Conectar por SSH (desde Windows; utilizar el nombre real de la clave descargada):

```powershell
ssh -i "$env:USERPROFILE\Downloads\key.pem" ubuntu@3.147.28.41
```

- En Ubuntu:

```bash
git clone https://github.com/VascoBustamante/champions-predictor-lab5.git
cd champions-predictor-lab5
cp .env.example .env
nano .env
chmod 600 .env
sudo docker compose -f docker-compose.prod.yml pull
sudo docker compose -f docker-compose.prod.yml up -d
sudo docker compose -f docker-compose.prod.yml ps
sudo docker compose -f docker-compose.prod.yml logs --tail=50 app
```

`.env` debe contener `DB_USER=champions`, una `DB_PASSWORD` **segura y privada** y `APP_IMAGE=vascobustamante/champions-predictor:1.0`. **No subir `.env` al repositorio.** La configuración de producción descarga la imagen de Docker Hub y utiliza el volumen `postgres_data` para PostgreSQL.

**Acceso:** http://3.147.28.41:8080 mientras la IP pública siga asignada. Tras detener/iniciar EC2, la IP puede cambiar si no está reservada. El servicio de demostración utiliza HTTP; para producción real se recomienda HTTPS y controles adicionales.

## Evidencias y entrega

- Captura de la portada en AWS mostrando los seis partidos.
- Captura del formulario y de `/predicciones` tras guardar un pronóstico en AWS.
- Captura de `/api/partidos` (JSON).
- Captura de `sudo docker compose -f docker-compose.prod.yml ps` con `app`, `db` y `mock-api`.
- Captura de la imagen publicada en Docker Hub.
- Captura del estado `Running` de la instancia EC2.
- Diagrama de arquitectura y explicación del flujo de datos.
- URL de este repositorio y video explicativo de **máximo cinco minutos**.

Al terminar la demostración, revisar el gasto de AWS y detener o eliminar los recursos que ya no sean necesarios.
