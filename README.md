# Music Metadata Service

## Technologies Used

- Kotlin
- Spring Boot
- PostgreSQL
- Jooq
- Flyway
- Testcontainers
- OpenAPI

## API Endpoints

`POST /tracks/create`: Creates a track for a specified artist

`GET /tracks?artist={artistId}`: Gets all tracks for a specified artist

`PATCH /artist/{artistId}`: Updates artist name and adds it to an alias

`GET /artist/artist-of-the-day`: Gets an artist of the day using a fair enough rotation

More details available at http://localhost:8080/swagger-ui/index.html#/

## Scheduled Job

This service also consists of scheduled job to update `Artist of the Day` daily at `00:00`

## Testing

Includes

- Unit Tests for all classes
- Integration tests for repository implementations
- Mock MVC tests for controllers 