# Spring Boot Application Package Structure

Use a **domain-driven, modular layout**: organize packages by **business modules**, not by technical layers.

### Recommended Example Package Structure

```
yjh.ontongsal.projectname/
├── Application                      # Main Spring Boot entrypoint class
├── shared/                          # Cross-cutting concerns and shared utilities
│   ├── exception/                   # Internal exception handling and error response models
│   ├── exposed/                     # Database persistence configurations and Exposed components
│   ├── logging/                     # Global logging aspects and interceptors
│   ├── response/                    # Standardized API response wrappers
│   ├── transaction/                 # Programmatic transaction management tools
│   ├── AppException.kt              # Global base exception class (Module's public API)
│   ├── ErrorCode.kt                 # Application-wide error code interface (Module's public API)
│   └── TransactionRunner.kt         # Functional transaction executor utility (Module's public API)
├── item/                            # Item module (bounded context)
│   ├── application/                 # Business logic layer
│   │   ├── ItemQueryPort.kt         # interfaces(port)
│   │   ├── ItemService.kt           # service logic
│   ├── domain/                      # Domain models
│   │   ├── Item.kt                  # Domain model
│   │   ├── ItemErrorCode.kt         # Domain model
│   │   ├── ItemStatus.kt            # Domain model
│   ├── infrastructure/              # Infrastructure layer
│   │   ├── InMemoryItemAdapter.kt   # Persistence implementation layer
│   │   ├── ExposedItemAdapter.kt    # Persistence implementation layer
│   │   ├── ItemTable.kt             # DB entities
│   ├── presentation/                # REST API or EventConsumer or @Scheduling layer
│   │   ├── ItemController.kt        # REST controllers
│   │   ├── ItemResponse.kt          # Response payload DTOs
│   │   ├── ItemRequest.kt           # Request payload DTOs
│   ├── ItemAPI.kt                   # Module's public API (facade)
│   └── ItemSnapshot.kt              # Module's public DTO (facade)
│
├── order/                           # Order module
└── inventory/                       # Inventory Module

```

Explanation of the above package structure:

- **Application.kt**: The main Spring Boot entry point class annotated with @SpringBootApplication. Contains the main()
  method that bootstraps the application.

- **shared/**
  **: Contains cross-cutting concerns and utilities shared across multiple modules. Under Spring Modulith rules, its
  sub-packages are encapsulated internally while the root files are exposed as the public API:
    -
- **{module}/** (e.g., item/, order/, inventory/): Each business module represents an encapsulated bounded context
  designed around Ports and Adapters (Hexagonal Architecture) containing:

    - **domain/**:Core business logic layer containing pure Kotlin objects with no external framework or database
      library dependencies:
        - **models**: Domain model classes representing business concepts and encapsulating foundational business rules.
        - **error codes**: Domain-specific error representations implementing the global ErrorCode interface.
        - **enums**: Domain-specific enums representing internal state machines or entity types.

    - **application/**: Business logic coordination and use-case execution layer:
        - **interfaces/ports**: Outbound port interfaces defining data access or integration contracts required by the
          application layer.
        - **services**: Service implementations that coordinate domain models and query ports to fulfill user stories or
          business actions.

    - **infrastructure/**: Technical implementation layer handling framework-specific details and integration
      boundaries:
        - **adapters**: Outbound adapter implementations that satisfy the port interfaces using specific tech stacks.
          These should use internal visibility to prevent cross-module leaks.
        - **entities/tables**: Database schema definitions or table mappings tailored strictly to the underlying
          ORM/persistence engine.

    - **presentation/**: Inbound delivery layer handling system entry points from HTTP clients, message brokers, or
      runtime triggers:
        - **controllers**: REST controller classes handling HTTP requests, performing input validation, and delegating
          workflow to services.
        - **dtos**: Dedicated request and response payload data classes acting as contract definitions for presentation
          boundaries.

    - **{Module}API.kt**: A facade class serving as the module's public entrypoint, delegating calls internally while
      hiding encapsulated module complexities behind Kotlin's visibility modifiers.
    - **{Module}Snapshot.kt**: A public Data Transfer Object (DTO) used to safely expose immutable snapshots of internal
      module state to foreign domain boundaries.

### Naming Conventions

| Type                     | Convention           | Example                                     |
|--------------------------|----------------------|---------------------------------------------|
| **Domain Models**        | `*Domain name`       | `Item`, `Inventory`                         |
| **Domain Enums**         | `*Status / *Type`    | `ItemStatus`, `OrderStatus`                 |
| **Domain Error/Enum**    | `*ErrorCode`         | `ItemErrorCode`, `InventoryErrorCode`       |
| **Application Ports**    | `*Port / *QueryPort` | `ItemQueryPort`, `ItemCommandPort`          |
| **Application Services** | `*Service`           | `ItemService`, `InventoryService`           |
| **Infra Adapters**       | `*Adapter`           | `ExposedItemAdapter`, `InMemoryItemAdapter` |
| **DB Tables/Entities**   | `*Table / *Entity`   | `ItemTable`, `OrderTable`                   |
| **Controllers**          | `*Controller`        | `ItemController`, `OrderController`         |
| **HTTP Request DTOs**    | `*Request`           | `ItemRequest`, `OrderRequest`               |
| **HTTP Response DTOs**   | `*Response`          | `ItemResponse`, `OrderResponse`             |
| **Module API**           | `*API`               | `ItemAPI`, `OrderAPI`                       |
| **Module Public DTOs**   | `*Snapshot / *Event` | `ItemSnapshot`, `OrderCreatedEvent`         |

