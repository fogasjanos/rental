# Getting Started

## How to compile
```
mvnw clean compile
```

## How to run
```
mvnw spring-boot:run
```

## How to test

### Swagger
http://localhost:8080/swagger-ui/

### DB console
http://localhost:8080/h2/

You can log in with the default credentials.

### Project requirements
#### Java version
It requires Java 17.

#### Lombok
Step-by-step guide how to enable/install into your favourite IDE can be found here: https://www.baeldung.com/lombok-ide


### Possible improvements
- do not allow booking to the past
- booking date should be more precise
- add more details to the cars when requesting a car with ID (or hide details from the list)
- versioning API
- add metrics
- use persistent DB
- logging to file
