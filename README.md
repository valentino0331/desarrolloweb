# EcoTechTrack – Plataforma Web para la Trazabilidad y Gestión Integral de RAEE

**Curso:** Desarrollo Web Integrado (100000ST61)  
**Universidad:** Universidad Tecnológica del Perú (UTP)  
**Tema:** Sistema de Gestión y Disposición Segura de Residuos de Aparatos Eléctricos y Electrónicos (RAEE)  
**Calificación Objetivo:** 20 / 20  

---

## 📑 Índice de Entregables e Informes Técnicos

1. 📄 **Avance de Portafolio 1 (APF1 - Unidad 1)**:
   - 📑 [INFORME_APF1.md](./ecotechtrack/INFORME_APF1.md)
   - 💻 Backend Spring Boot 3 con Arquitectura en Capas y CRUD básico.
2. 📄 **Avance de Portafolio 2 (APF2 - Unidad 2)**:
   - 📑 [INFORME_APF2_ECOTECHTRACK.md](./INFORME_APF2_ECOTECHTRACK.md)
   - 🔒 Persistencia relacional avanzada (JPA/Hibernate), consultas JPQL, `@Transactional`, Spring Security 6 y autenticación con tokens JWT.
   - 📮 Colección Postman: [ecotechtrack_postman_collection.json](./ecotechtrack/ecotechtrack_postman_collection.json)
3. 📄 **Avance de Portafolio 3 (APF3 - Unidad 3)**:
   - 📑 [INFORME_APF3_ECOTECHTRACK.md](./INFORME_APF3_ECOTECHTRACK.md)
   - 🌐 Integración completa Back-end (Spring Boot) + Front-end (Angular 18 SPA).
   - 🎨 Componentes autónomos, enrutamiento, formularios reactivos con validaciones, interceptor HTTP para JWT, guards y diseño responsivo SASS.

---

## 🚀 Inicio Rápido del Sistema Completo (Backend + Frontend)

Para levantar el ecosistema completo con un solo clic:

1. Ejecuta el archivo batch:
   ```cmd
   iniciar_sistema_completo.bat
   ```
2. El script iniciará automáticamente:
   - ☕ **Backend Spring Boot 3**: `http://localhost:8080/`
   - 🗄️ **Consola H2 en Memoria**: `http://localhost:8080/h2-console` (`jdbc:h2:mem:ecotechtrackdb`, usuario: `sa`, clave vacía)
   - 🅰️ **Frontend Angular 18**: `http://localhost:4200/` (se abrirá en tu navegador automáticamente)

---

## 🔑 Credenciales de Prueba Precargadas (RBAC)

El semillero `DataInitializer.java` carga automáticamente los siguientes usuarios con contraseñas cifradas en BCrypt:

| Perfil / Rol | Username | Contraseña | Permisos en la Plataforma |
| :--- | :--- | :--- | :--- |
| **🛡️ Administrador** | `admin` | `Admin123!` | Control total: Crear, editar, cambiar estados y **eliminar** residuos, registrar almacenes y operadores. |
| **🚚 Operador RAEE** | `operador` | `Operador123!` | Gestión operativa: Registrar nuevos residuos, editar especificaciones y cambiar fases de trazabilidad. |
| **👁️ Auditor / Lector** | `usuario` | `User123!` | Modo consulta: Visualizar inventario, estadísticas JPQL, fichas técnicas y líneas de tiempo. |

*(Nota: En la pantalla de login del frontend encontrarás botones de acceso rápido para autenticarte como cualquiera de estos perfiles con un solo clic).*

---

## 🏛️ Resumen de Arquitectura del Proyecto

```text
desarrolloweb-main/
├── ecotechtrack/                    # Backend Spring Boot 3 (API RESTful)
│   ├── src/main/java/               # Modelos, DTOs, Repositorios JPQL, Servicios y Seguridad JWT
│   ├── src/main/resources/          # application.properties (H2 / MySQL)
│   ├── src/test/java/               # 17 pruebas unitarias automatizadas (JUnit 5 + MockMvc)
│   ├── ecotechtrack_postman_collection.json # Colección de pruebas de endpoints y JWT
│   └── pom.xml                      # Dependencias de Spring Boot, Security, JJWT y JPA
├── ecotechtrack-frontend/           # Frontend Angular 18 (SPA)
│   ├── src/app/components/          # Vistas (Navbar, Login, Register, Dashboard, Residuos, Almacenes)
│   ├── src/app/services/            # Servicios HttpClient para consumo REST
│   ├── src/app/interceptors/        # Interceptor HTTP para inyección de token JWT
│   ├── src/app/guards/              # AuthGuard para protección de rutas
│   └── src/styles.scss              # Sistema de diseño y estilos responsivos en SASS
├── INFORME_APF2_ECOTECHTRACK.md     # Informe formal de Persistencia JPQL y Seguridad JWT (APF2)
├── INFORME_APF3_ECOTECHTRACK.md     # Informe formal de Integración Spring Boot + Angular (APF3)
├── iniciar_sistema_completo.bat     # Script de arranque simultáneo de Backend y Frontend
└── README.md                        # Documentación general del repositorio
```
