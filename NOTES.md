# Notes

## Architecture Overview

The project follows a **modular Clean Architecture** with separation into:
- **Data Layer**:
  - `VenueRemoteDataSource` communicates with the remote API using Ktor.
  - `FavoriteDao` manages local Room database access.
- **Domain Layer**:
  - Business logic is encapsulated in Use Cases (`GetVenuesForLocationUseCase`, `ToggleFavoriteUseCase`, etc.).
  - Entities and repository interfaces are defined here.
- **Presentation Layer**:
  - Compose UI with ViewModel-backed state management.
  - Venue listing UI reacting to location updates and favorites state.

## Technologies Used
- **Jetpack Compose** for UI
- **Koin** for Dependency Injection
- **Ktor** for Networking
- **Room** for Local Database (with KSP)
- **Kotlinx Serialization** for JSON parsing
- **Turbine**, **MockK**, **JUnit5** for testing
- **Gradle Version Catalogs** and **KTS** scripting

---

## Known Limitations and Trade-offs

- **Favorites Management**:
  - Current implementation checks favorites by loading the entire list (`getAllFavorites()`).
  - Acceptable for small datasets.
  - Would not scale efficiently; ideally `SELECT EXISTS` queries or in-memory caching should be added if needed.

- **MockEngine Duplication in Tests**:
  - Some unit tests recreate MockEngine/HttpClient setup individually.
  - For simplicity and scope, no base test setup abstraction was made.

- **Room Query Approach**:
  - `Flow<List<FavoriteEntity>>` is returned instead of more optimized structures (`Set<String>`, etc.).
  - Fine for assignment size, but optimization would be required for large datasets.

- **No Pagination on Favorites**:
  - Full favorites list is fetched without pagination.
  - Reasonable for the expected low number of entries.

- **Allowing Main Thread Queries in Tests**:
  - Main-thread queries are allowed for faster and simpler test execution.
  - Would need stricter thread handling in a production-grade application.

---

## Testing Strategy

- **Unit Tests** for each layer:
  - Repositories
  - UseCases
  - ViewModels
- **Integration Tests** with Room in-memory database.
- **RemoteDataSource Tests** using Ktor's MockEngine to simulate API responses.

---

## Project Setup

1. Requires **Android Studio Hedgehog (or newer)**.
2. Clone the repository and open it.
3. Build using Gradle — no manual steps needed.
4. Run tests with `./gradlew test` or directly from the IDE.
