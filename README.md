# Pricing Feeds Web Application

## Overview

This project is a web application designed to manage pricing feeds from retail stores. It allows users to upload, search, and edit pricing records stored in a PostgreSQL database. The application leverages Java 17, Spring Boot v3.x, and other modern technologies to provide robust functionality and maintainability.

## Technologies Used

- **Java**: 17
- **Spring Boot**: v3.x
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Additional Technologies**:
  - Swagger Open API for API documentation
  - Spring Batch for CSV file processing
  - Spring AOP for centralized logging
  - Spring Actuator for monitoring and management

## Functional Requirements

1. **CSV File Upload and Processing**:
   - Upload CSV files containing pricing data including Store ID, SKU, Product Name, Price, and Date.
   - Persist the data into the PostgreSQL database.

2. **Search and Edit Pricing Records**:
   - Search for pricing records based on various criteria.
   - Edit and save changes to existing records.

## Non-Functional Requirements

1. **Scalability**: The application is designed to handle large volumes of data and can scale to support a retail chain with up to 3000 stores across multiple countries.
2. **Performance**: Optimized for fast data processing and quick response times.
3. **Security**: Implements standard security practices to protect data and access.
4. **Maintainability**: Codebase is modular and well-documented for easy maintenance and upgrades.
5. **Reliability**: Includes fault tolerance mechanisms and error handling to ensure high availability.

## Solution Architecture

- **Context Diagram**: [Link to Context Diagram](#)
- **Solution Architecture Diagram**: [Link to Solution Architecture Diagram](#)

## Design Decisions

1. **Spring Boot**: Chosen for its ease of setup and wide range of features.
2. **PostgreSQL**: Selected for its reliability and support for complex queries.
3. **Spring Batch**: Used for efficient processing of CSV files.
4. **Swagger**: Implemented for API documentation and ease of use.
5. **Spring AOP**: Utilized for centralized logging and cross-cutting concerns.

## Assumptions

1. The application is deployed in a secure environment.
2. Users have proper permissions to upload and manage pricing data.
3. Data integrity is ensured through validation checks.

## Getting Started

### Prerequisites

- **Java 17**: Ensure Java 17 is installed on your machine.
- **Maven**: Make sure Maven is installed for building the project.

### Build and Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/javasaroj/pricing-feeds.git
   cd pricing-feeds

2. **Clean and Build the Project**:
mvn clean install<br/>
3. **Run the Application**:
mvn spring-boot:run<br/>
<h2>Access URLs</h2>
[Swagger UI]: http://localhost:9091/swagger-ui/index.html<br/>
[Spring Doc API]: http://localhost:9091/pricing-feed<br/>
[PgAdmin]: http://localhost:6060/browser/<br/>
[prometheus]: http://localhost:9091/actuator/prometheus<br/>
<h2>Documentation</h2>
API Documentation: Access the Swagger UI for comprehensive API documentation.<br/>
Spring Actuator: Provides monitoring and management features. Check the Spring Doc URL for details.<br/>
<h2>Source Code</h2>
Repository: [GitHub - Pricing Feeds](https://github.com/javasaroj/pricing-feeds.git)
