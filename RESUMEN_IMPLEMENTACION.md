# Resumen de Implementación - API de Reportes

## 📁 Estructura Creada

```
src/main/java/com/unfv/biblioteca/bibliotecaapi/reportes/
├── controller/
│   └── ReportesController.java          # Endpoints REST
├── service/
│   └── ReportesService.java             # Lógica de negocio
├── repository/
│   └── ReportesRepository.java          # Acceso a BD (Views y SPs)
├── dto/
│   ├── DisponibilidadMaterialDTO.java   # DTO para vista disponibilidad
│   ├── PrestamoActivoDTO.java           # DTO para vista préstamos activos
│   ├── DevolucionRequestDTO.java        # DTO para request de devolución
│   └── DisponibilidadResponseDTO.java   # DTO para response de verificación
└── README.md                            # Documentación de endpoints
```

## 🎯 Endpoints Implementados

### 1. Views Expuestas
- `GET /api/reportes/disponibilidad` - Lista disponibilidad de todos los materiales
- `GET /api/reportes/disponibilidad/{id}` - Disponibilidad de un material específico
- `GET /api/reportes/prestamos-activos` - Lista préstamos activos (con filtro opcional por estado)

### 2. Stored Procedures Expuestos
- `POST /api/reportes/devolucion` - Registrar devolución (llama a `sp_registrar_devolucion`)
- `GET /api/reportes/verificar-disponibilidad/{id}` - Verificar disponibilidad (llama a `sp_verificar_disponibilidad`)

## 🔧 Componentes Principales

### ReportesController
- Controlador REST que maneja las peticiones HTTP
- Validación de parámetros
- Manejo de excepciones con respuestas apropiadas

### ReportesService
- Capa de lógica de negocio
- Validaciones adicionales
- Transacciones manejadas con `@Transactional`

### ReportesRepository
- Ejecuta queries nativas para acceder a las vistas
- Utiliza `StoredProcedureQuery` para llamar a los procedimientos almacenados
- Maneja la conversión de tipos de Oracle a Java

## 🗄️ Objetos de BD Utilizados

### Views
1. **vw_disponibilidad_material**
   - Muestra materiales con conteo de ejemplares
   - Columnas: id, titulo, isbn, total_ejemplares, ejemplares_disponibles, ejemplares_prestados

2. **vw_prestamos_activos**
   - Muestra préstamos sin devolver
   - Calcula estado (VIGENTE/VENCIDO) dinámicamente
   - Columnas: id, usuario, titulo, fecha_prestamo, fecha_devolucion_pactada, estado

### Triggers
- **trg_actualizar_estado_ejemplar**: Se ejecuta automáticamente al insertar un préstamo

### Stored Procedures
1. **sp_registrar_devolucion**: Registra devolución y actualiza estado del ejemplar
2. **sp_verificar_disponibilidad**: Cuenta ejemplares disponibles de un material

## 🧪 Cómo Probar

### Con cURL

```bash
# 1. Ver disponibilidad general
curl http://localhost:8080/api/reportes/disponibilidad

# 2. Ver disponibilidad de material específico
curl http://localhost:8080/api/reportes/disponibilidad/1

# 3. Ver préstamos activos
curl http://localhost:8080/api/reportes/prestamos-activos

# 4. Ver solo préstamos vencidos
curl "http://localhost:8080/api/reportes/prestamos-activos?estado=VENCIDO"

# 5. Registrar devolución
curl -X POST http://localhost:8080/api/reportes/devolucion \
  -H "Content-Type: application/json" \
  -d '{"prestamoId": 1}'

# 6. Verificar disponibilidad
curl http://localhost:8080/api/reportes/verificar-disponibilidad/1
```

### Con Postman
1. Importar la colección o crear requests manualmente
2. Usar la base URL: `http://localhost:8080/api/reportes`
3. Seguir los ejemplos del README.md

## ⚠️ Notas Importantes

1. **Nombres de Tablas**: El código usa JPA que mapea a los nombres de tabla definidos en las entidades:
   - `materiales` (no `MATERIAL`)
   - `ejemplares` (no `EJEMPLAR`)
   - `prestamos` (no `PRESTAMO`)
   - `usuarios` (no `USUARIO`)

2. **Triggers Automáticos**: El trigger `trg_actualizar_estado_ejemplar` se ejecuta automáticamente, no requiere invocación desde la API.

3. **Transacciones**: Los stored procedures manejan sus propias transacciones con `COMMIT`.

4. **Conversión de Tipos**: El repositorio maneja la conversión entre tipos de Oracle (TIMESTAMP, DATE, NUMBER) y tipos de Java (LocalDateTime, LocalDate, Long, Integer).

## 🚀 Próximos Pasos

1. Iniciar la aplicación: `./gradlew bootRun`
2. Probar los endpoints con cURL o Postman
3. Verificar logs para depuración si es necesario
4. Agregar seguridad (autenticación/autorización) si se requiere
5. Documentar con Swagger/OpenAPI si se desea

## 📝 Dependencias Utilizadas

- Spring Boot Web
- Spring Data JPA
- Jakarta Persistence API
- Lombok (para reducir código boilerplate)
- Oracle JDBC Driver (configurado en application.properties)

