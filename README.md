# E-Commerce Web Application

> ⚙️ **This project is an extension of the following repository:**  
> [https://github.com/iamachrafeaz/Atelier-2](https://github.com/iamachrafeaz/Atelier-2)


This is a Java EE-based e-commerce web application that provides a complete solution for managing products, users, shopping carts, and orders.

## Technologies Used

- **Java EE (Jakarta EE)**
- **JSF (Jakarta Faces)** - For web interface
- **JPA (Hibernate)** - For database management
- **Maven** - For project management and dependencies
- **Lombok** - For reducing boilerplate code
- **Jakarta Validation** - For input validation

## Features

- User Authentication and Authorization

  - User registration
  - User login/logout
  - Session management

- Product Management

  - Product listing
  - Product details view
  - Inventory management

- Shopping Cart

  - Add/remove items
  - Update quantities
  - Cart persistence

- Order Management
  - Order creation
  - Order history
  - Order status tracking

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── ma/
│   │       └── fstt/
│   │           ├── bean/       # JSF managed beans
│   │           ├── model/      # JPA entities
│   │           ├── repository/ # Data access layer
│   │           ├── service/    # Business logic layer
│   │           └── utils/      # Utility classes
│   ├── resources/
│   │   └── META-INF/
│   │       ├── beans.xml      # CDI configuration
│   │       └── persistence.xml # JPA configuration
│   └── webapp/
│       ├── auth/              # Authentication pages
│       ├── cart/              # Shopping cart pages
│       ├── order/             # Order management pages
│       ├── product/           # Product pages
│       └── WEB-INF/
└── test/                      # Test directory
```

## Prerequisites

- Java 23 or higher
- Maven 3.x
- A Java EE compatible application server (e.g., WildFly, GlassFish)
- A compatible database server

## Building the Project

To build the project, run:

```bash
mvn clean install
```

This will create a WAR file in the `target` directory.

## Development

### Setting Up the Development Environment

1. Clone the repository
2. Import the project as a Maven project in your IDE
3. Configure your application server
4. Set up your database and update `persistence.xml` accordingly

### Key Components

- **Models**: Located in `ma.fstt.model`, these are JPA entities representing the database structure
- **Repositories**: Handle data access operations
- **Services**: Contain business logic and transaction management
- **Beans**: JSF managed beans that handle web interface logic
- **Views**: XHTML pages in the webapp directory

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
