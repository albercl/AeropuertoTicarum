# Aeropuerto Ticarum

## Ejecución

Como se indicaba en el enunciado, se ha usado Java 11 y Spring 2.7.8. Para ejecutar el proyecto se pueden usar los
siguientes comandos:

```bash
mvn install
mvn spring-boot:run
```

## Api REST

Para la API he usado como parámetro base el nombre de la aerolínea. Esto no es del todo necesario, pero podría servir
como base para incluir más aerolíneas en este servicio a futuro. Es por esto que algunos endpoints piden el nombre de la
aerolínea, pero no se le da utilidad.

### Seguridad

El servicio usa basic auth para autenticar al usuario. Las credenciales son en memoria:

```text
username: user
password: pass
```

### Swagger

Se puede acceder a la documentación de la API en el endpoint `/swagger-ui.html`. Se requiere usar las credenciales
anteriores.

### Endpoints

Hay algunas diferencias con respecto a la especificación del enunciado debido a que me parece que son más correctas en
cuanto a uso de los métodos HTTP:

* `PUT /{aerolinea}/vuelo/{idVuelo}`: En el enunciado se pide que se use `PUT` para actualizar un vuelo. Sin embargo, he
  decidido usar `PATCH` ya que es más correcto. `PUT` es para actualizar un recurso completo, mientras que `PATCH` es
  para actualizar un recurso parcialmente.
* `POST /{aerolinea}/salidas/{idVuelo}/despegue`: En el enunciado se pide usar `PUT`, pero para este tipo de operaciones
  es más correcto usar `POST`.