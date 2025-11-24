# API de Reportes - Endpoints para Views y Stored Procedures

Esta API expone endpoints REST para consultar las vistas y ejecutar los procedimientos almacenados creados en Oracle.

## Base URL
```
http://localhost:8080/api/reportes
```

**Nota:** La aplicación tiene configurado `server.servlet.context-path=/api`, por lo que todas las rutas inician con `/api`.

## Endpoints Disponibles

### 1. Obtener Disponibilidad de Todos los Materiales
**Endpoint:** `GET /api/reportes/disponibilidad`

**Descripción:** Consulta la vista `vw_disponibilidad_material` y retorna la disponibilidad de todos los materiales.

**Ejemplo de Request:**
```bash
curl -X GET http://localhost:8080/api/reportes/disponibilidad
```

**Ejemplo de Response:**
```json
[
  {
    "id": 1,
    "titulo": "Cien años de soledad",
    "isbn": "978-0307474728",
    "totalEjemplares": 5,
    "ejemplaresDisponibles": 3,
    "ejemplaresPrestados": 2
  },
  {
    "id": 2,
    "titulo": "Don Quijote de la Mancha",
    "isbn": "978-8424936464",
    "totalEjemplares": 3,
    "ejemplaresDisponibles": 1,
    "ejemplaresPrestados": 2
  }
]
```

---

### 2. Obtener Disponibilidad de un Material Específico
**Endpoint:** `GET /api/reportes/disponibilidad/{materialId}`

**Descripción:** Consulta la vista `vw_disponibilidad_material` filtrada por ID de material.

**Parámetros:**
- `materialId` (path): ID del material

**Ejemplo de Request:**
```bash
curl -X GET http://localhost:8080/api/reportes/disponibilidad/1
```

**Ejemplo de Response:**
```json
{
  "id": 1,
  "titulo": "Cien años de soledad",
  "isbn": "978-0307474728",
  "totalEjemplares": 5,
  "ejemplaresDisponibles": 3,
  "ejemplaresPrestados": 2
}
```

---

### 3. Obtener Préstamos Activos
**Endpoint:** `GET /api/reportes/prestamos-activos`

**Descripción:** Consulta la vista `vw_prestamos_activos` y retorna todos los préstamos activos.

**Parámetros opcionales:**
- `estado` (query): Filtrar por estado (VIGENTE o VENCIDO)

**Ejemplo de Request (todos):**
```bash
curl -X GET http://localhost:8080/api/reportes/prestamos-activos
```

**Ejemplo de Request (filtrado):**
```bash
curl -X GET "http://localhost:8080/api/reportes/prestamos-activos?estado=VENCIDO"
```

**Ejemplo de Response:**
```json
[
  {
    "id": 1,
    "usuario": "Juan Pérez García",
    "titulo": "Cien años de soledad",
    "fechaPrestamo": "2025-11-15T10:30:00",
    "fechaDevolucionPactada": "2025-11-22",
    "estado": "VIGENTE"
  },
  {
    "id": 2,
    "usuario": "María López Torres",
    "titulo": "Don Quijote de la Mancha",
    "fechaPrestamo": "2025-11-01T14:00:00",
    "fechaDevolucionPactada": "2025-11-08",
    "estado": "VENCIDO"
  }
]
```

---

### 4. Registrar Devolución de un Préstamo
**Endpoint:** `POST /api/reportes/devolucion`

**Descripción:** Ejecuta el stored procedure `sp_registrar_devolucion` para registrar la devolución de un préstamo y actualizar el estado del ejemplar a DISPONIBLE.

**Body (JSON):**
```json
{
  "prestamoId": 1,
  "fechaDevolucion": "2025-11-24T16:30:00"
}
```

**Nota:** Si no se envía `fechaDevolucion`, se usa la fecha/hora actual.

**Ejemplo de Request:**
```bash
curl -X POST http://localhost:8080/api/reportes/devolucion \
  -H "Content-Type: application/json" \
  -d '{
    "prestamoId": 1,
    "fechaDevolucion": "2025-11-24T16:30:00"
  }'
```

**Ejemplo de Response (éxito):**
```json
{
  "mensaje": "Devolución registrada exitosamente",
  "prestamoId": "1"
}
```

**Ejemplo de Response (error):**
```json
{
  "error": "ID de préstamo inválido"
}
```

---

### 5. Verificar Disponibilidad de un Material
**Endpoint:** `GET /api/reportes/verificar-disponibilidad/{materialId}`

**Descripción:** Ejecuta el stored procedure `sp_verificar_disponibilidad` para obtener el número de ejemplares disponibles de un material.

**Parámetros:**
- `materialId` (path): ID del material

**Ejemplo de Request:**
```bash
curl -X GET http://localhost:8080/api/reportes/verificar-disponibilidad/1
```

**Ejemplo de Response:**
```json
{
  "materialId": 1,
  "ejemplaresDisponibles": 3
}
```

---

## Objetos de Base de Datos Utilizados

### Views
1. **vw_disponibilidad_material**: Muestra la disponibilidad de materiales con conteo de ejemplares totales, disponibles y prestados.
2. **vw_prestamos_activos**: Muestra los préstamos activos con información del usuario, material y estado (VIGENTE/VENCIDO).

### Triggers
1. **trg_actualizar_estado_ejemplar**: Se activa automáticamente al insertar un préstamo, cambiando el estado del ejemplar a 'PRESTADO'.

### Stored Procedures
1. **sp_registrar_devolucion**: Registra la devolución de un préstamo y actualiza el estado del ejemplar a 'DISPONIBLE'.
2. **sp_verificar_disponibilidad**: Cuenta los ejemplares disponibles de un material específico.

---

## Códigos de Estado HTTP

- `200 OK`: Solicitud exitosa
- `400 Bad Request`: Parámetros inválidos
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error en el servidor

---

## Notas Importantes

1. El trigger `trg_actualizar_estado_ejemplar` se ejecuta automáticamente al crear un préstamo, no requiere invocación manual.
2. La vista `vw_prestamos_activos` calcula dinámicamente el estado (VIGENTE/VENCIDO) comparando la fecha de devolución pactada con la fecha actual.
3. Los procedimientos almacenados se ejecutan con `COMMIT` automático, por lo que los cambios son permanentes.

---

## Ejemplos con Postman

### Colección de Pruebas

1. **GET Disponibilidad General**
   - URL: `http://localhost:8080/api/reportes/disponibilidad`
   - Método: GET

2. **GET Disponibilidad Específica**
   - URL: `http://localhost:8080/api/reportes/disponibilidad/1`
   - Método: GET

3. **GET Préstamos Vencidos**
   - URL: `http://localhost:8080/api/reportes/prestamos-activos?estado=VENCIDO`
   - Método: GET

4. **POST Registrar Devolución**
   - URL: `http://localhost:8080/api/reportes/devolucion`
   - Método: POST
   - Headers: `Content-Type: application/json`
   - Body:
   ```json
   {
     "prestamoId": 1
   }
   ```

5. **GET Verificar Disponibilidad**
   - URL: `http://localhost:8080/api/reportes/verificar-disponibilidad/1`
   - Método: GET

