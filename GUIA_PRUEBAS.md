# Guía Rápida de Pruebas

## 🚀 Iniciar la Aplicación

```bash
cd C:\Users\Lenovo\Desktop\bilioteca-unfv-api
.\gradlew bootRun
```

Espera a que veas: `Started BibliotecaApiApplication`

---

## 📋 Orden Sugerido de Pruebas

### 1️⃣ Verificar que la Vista de Disponibilidad Funciona

```bash
curl http://localhost:8080/api/reportes/disponibilidad
```

**Nota:** La aplicación usa `context-path=/api`, por lo que las rutas incluyen automáticamente el prefijo `/api`.

**Respuesta esperada:** JSON con lista de materiales y su disponibilidad

---

### 2️⃣ Ver Disponibilidad de un Material Específico

```bash
curl http://localhost:8080/api/reportes/disponibilidad/1
```

**Respuesta esperada:** JSON con un solo material (el ID 1)

---

### 3️⃣ Ver Todos los Préstamos Activos

```bash
curl http://localhost:8080/api/reportes/prestamos-activos
```

**Respuesta esperada:** JSON con lista de préstamos que no han sido devueltos

---

### 4️⃣ Filtrar Solo Préstamos Vencidos

```bash
curl "http://localhost:8080/api/reportes/prestamos-activos?estado=VENCIDO"
```

**Respuesta esperada:** JSON con préstamos cuya fecha de devolución pactada ya pasó

---

### 5️⃣ Verificar Disponibilidad con Stored Procedure

```bash
curl http://localhost:8080/api/reportes/verificar-disponibilidad/1
```

**Respuesta esperada:**
```json
{
  "materialId": 1,
  "ejemplaresDisponibles": 3
}
```

---

### 6️⃣ Registrar una Devolución

**Primero, identifica un préstamo activo:**
```bash
curl http://localhost:8080/api/reportes/prestamos-activos
```

**Luego, registra la devolución (usa un ID real del paso anterior):**
```bash
curl -X POST http://localhost:8080/api/reportes/devolucion ^
  -H "Content-Type: application/json" ^
  -d "{\"prestamoId\": 1}"
```

**Nota para PowerShell:** Si usas PowerShell en lugar de CMD, usa este formato:
```powershell
$body = @{
    prestamoId = 1
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/reportes/devolucion" `
  -Body $body `
  -ContentType "application/json"
```

---

### 7️⃣ Verificar que la Devolución se Registró

**Verifica que el préstamo ya no aparece en activos:**
```bash
curl http://localhost:8080/api/reportes/prestamos-activos
```

**Verifica que la disponibilidad aumentó:**
```bash
curl http://localhost:8080/api/reportes/disponibilidad/1
```

---

## 🧪 Prueba del Trigger Automático

El trigger `trg_actualizar_estado_ejemplar` se activa automáticamente al crear un préstamo desde tu aplicación. 

**Para probarlo:**

1. Crea un préstamo usando tu API existente (no el módulo de reportes)
2. Verifica en la BD que el estado del ejemplar cambió a 'PRESTADO':
   ```sql
   SELECT estado FROM ejemplares WHERE id = {ejemplar_id};
   ```

---

## 🔍 Debugging

Si algo no funciona:

### Ver logs de la aplicación
Los logs aparecerán en la consola donde ejecutaste `gradlew bootRun`

### Verificar conexión a BD
Revisa `application.properties` para asegurar que la configuración de Oracle es correcta:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Verificar que los objetos de BD existen
```sql
-- Ver views
SELECT view_name FROM user_views WHERE view_name LIKE 'VW_%';

-- Ver triggers
SELECT trigger_name FROM user_triggers;

-- Ver procedures
SELECT object_name FROM user_procedures WHERE object_type = 'PROCEDURE';
```

---

## 📊 Ejemplos de Respuestas

### Disponibilidad Material
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

### Préstamos Activos
```json
[
  {
    "id": 1,
    "usuario": "Juan Pérez García",
    "titulo": "Cien años de soledad",
    "fechaPrestamo": "2025-11-15T10:30:00",
    "fechaDevolucionPactada": "2025-11-22",
    "estado": "VIGENTE"
  }
]
```

### Devolución Exitosa
```json
{
  "mensaje": "Devolución registrada exitosamente",
  "prestamoId": "1"
}
```

---

## ✅ Checklist de Verificación

- [ ] Aplicación iniciada correctamente
- [ ] Endpoint de disponibilidad general funciona
- [ ] Endpoint de disponibilidad específica funciona
- [ ] Endpoint de préstamos activos funciona
- [ ] Filtro por estado (VENCIDO/VIGENTE) funciona
- [ ] Endpoint de verificar disponibilidad (SP) funciona
- [ ] Endpoint de registrar devolución (SP) funciona
- [ ] El trigger actualiza el estado automáticamente al crear préstamos

---

## 🆘 Problemas Comunes

### Error 404 - Not Found
- Verifica que la URL sea correcta
- Asegúrate de que la aplicación esté corriendo
- Revisa que el puerto sea 8080 (o el configurado en application.properties)

### Error 500 - Internal Server Error
- Revisa los logs de la aplicación
- Verifica que las vistas y procedures existan en Oracle
- Confirma que la conexión a la BD funciona

### Respuesta vacía []
- Es normal si no hay datos en las tablas
- Inserta datos de prueba en las tablas

### Error al registrar devolución
- Verifica que el ID del préstamo exista
- Asegúrate de que el préstamo no esté ya devuelto
- Revisa que el stored procedure funcione en SQL Developer primero

