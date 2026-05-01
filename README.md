# Logistics Dispatch System

## Overview
This project is a backend system for managing logistics operations, designed as a production-style Spring Boot application with a focus on scalability, security, and clean architecture.

The system simulates real-world dispatch operations where users with different roles (ADMIN, DISPATCHER, DRIVER) can manage orders, assign drivers, and track delivery workflow.


### Key features of the project include:
- Secure authentication using JWT (access + refresh token rotation)
- Role-based access control using Spring Security
- Full refresh token lifecycle management with database persistence
- Order management system (create, assign, update status)
- Driver assignment workflow
- AOP-based business auditing system for tracking domain actions (user logs)
- Security event logging (login, logout, refresh, unauthorized access, access denied)
- DTO + Mapper-based architecture to separate domain and API layers
- Centralized exception handling and security error processing
- Clean-layered architecture (Controller → Service → Repository)

The project is designed as a foundation for a real-world logistics backend that can later be extended into microservices, event-driven architecture, or integrated with external automation tools (e.g. n8n workflows).

## Building

### Requirements
- Java 17+
- Gradle 8+
- PostgreSQL
- Spring Boot 4.x

### Run locally
- Build the project:
  ./gradlew clean build

- Run the application:
  ./gradlew bootRun

### Environment variables
Create .env or configure:

DB_URL=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=

## Troubleshooting
1. JWT errors
Ensure JWT_SECRET is set and valid
Check token expiration time
2. Access denied (403)
Verify user roles contain ROLE_ADMIN, ROLE_DRIVER, etc.
Check @PreAuthorize annotations
3. Refresh token issues
Ensure refresh token exists in DB
Check expiration and rotation logic
4. Logs not appearing
Ensure @EnableAsync is enabled
Check thread pool configuration
Verify AOP aspect is scanned

## Release Notes
Can be found in [RELEASE_NOTES](RELEASE_NOTES.md).

## Authors
* Oleksandr Karmazyn  - [AveEg0](https://github.com/AveEg0)

## Contributing
Please, follow [Contributing](CONTRIBUTING.md) page.

## Code of Conduct
Please, follow [Code of Conduct](CODE_OF_CONDUCT.md) page.

## License
This project is Apache License 2.0 - see the [LICENSE](LICENSE) file for details
