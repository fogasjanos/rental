# Getting Started

## How to compile
```shell
./mvnw clean compile
```

## How to run
```shell
./mvnw spring-boot:run
```

## How to test

### Swagger
[http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/index.html)

### DB console
[http://localhost:8080/h2/](http://localhost:8080/h2/)

You can log in with the default credentials.

### Project requirements
#### Java version
It requires Java 21.

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
