# infra-api-gateway

This project is an API Gateway for the Ticketly microservices platform, built with Spring Cloud Gateway and WebFlux. It routes requests to backend services, handles CORS, and secures endpoints using OAuth2 JWT tokens (Keycloak).

## Features

- **Routing**: Forwards requests to event, order, and payment services.
- **Security**: OAuth2 JWT validation via Keycloak.
- **CORS**: Configured for local and production frontends.
- **Logging**: Detailed logging for debugging and development.

## Requirements

- Java 17+
- Maven
- Keycloak (for OAuth2/JWT)
- Backend microservices (event-seating, event-query, order, payment)

## Configuration

Configuration is managed via `application.yml`. Key environment variables:

- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_JWK_SET_URI`
- `EVENT_SEATING_SERVICE_URI`
- `EVENT_QUERY_SERVICE_URI`
- `ORDER_SERVICE_URI`
- `PAYMENT_SERVICE_URI`

## Running Locally

1. Clone the repository.
2. Set required environment variables or update `application.yml`.
3. Build and run:

   ```bash
   mvn clean package
   java -jar target/infra-api-gateway-*.jar
   ```

4. The gateway will start on port `8088` by default.

## Endpoints

- `/api/event-seating/**` → Event Seating Service
- `/api/event-query/**` → Event Query Service
- `/api/order/**` → Order Service
- `/api/payment/**` → Payment Service

## Security

- `/api/event-query/**` is public.
- All other routes require a valid JWT.

## License

MIT
