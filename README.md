# Vee Assignment
Spring Boot Demo Covid-19 API using PostgreSQL.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/fa61a785a105787111f2)

## Person Entity

The Person class model has properties as:

* ```id: Integer```
* ```firstName: String```
* ```lastName: String```
* ```dateOfBirth: java.sql.Date```
* ```dateOfInfection: java.sql.Date```
* ```dateOfRecovery: java.sql.Date```

## Routes

```java
// Get All Persons
GET localhost:8080/persons

// Get All Persons (Sorted By Infection Date)
GET localhost:8080/persons?sort=1

// Get Person By Id
GET localhost:8080/persons/{id}

// Add Person
POST localhost:8080/persons

// Update Person's Recovery Date
PUT localhost:8080/persons/{id}

// Delete Person By Id
DELETE localhost:8080/persons/{id}
```
