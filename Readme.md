# 📝 Todo App with Spring Boot and MySQL (Dockerized)

This is a simple **Todo REST API** built with **Spring Boot** and **MySQL**, using Docker to manage the database service.  
The app demonstrates a clean architecture using **Entity**, **Repository**, **Service**, and **Controller** layers.

---

## 🚀 Features

✅ Create, Read, Update, Delete (CRUD) Todo items  
✅ MySQL database running in a Docker container  
✅ Spring Data JPA for persistence  
✅ RESTful API with JSON responses  

---

## 📦 Technologies Used

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL 8 (via Docker)
- Docker Compose

---

## 🛠 Prerequisites

- [Java 17+](https://adoptium.net/) installed
- [Maven](https://maven.apache.org/) installed
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed

---

# Steps

##  Docker Setup

Create a `docker-compose.yml` file in the project root:

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8
    container_name: mysql-db
    environment:
      MYSQL_ROOT_PASSWORD: user_password
      MYSQL_DATABASE: todoapp
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:

```

## Spring Boot Configuration

```properties

=== MySQL Database Configuration ===
spring.datasource.url=jdbc:mysql://localhost:3306/todoapp
spring.datasource.username=root
spring.datasource.password=user_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# === Hibernate JPA Settings ===
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

```

```swift

src/main/java/com/example/todo/
├── TodoApplication.java
├── entity/
│   └── Todo.java
├── repository/
│   └── TodoRepository.java
├── service/
│   └── TodoService.java
└── controller/
    └── TodoController.java

```

## Main Application Class
src/main/java/com/example/todo/TodoApplication.java
```java
package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}

```

## Entity Class
src/main/java/com/example/todo/entity/Todo.java
```java
package com.example.todo.entity;

import javax.persistence.*;
/* import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType; */

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @JsonProperty("completed")
    private boolean completed;

    public Todo() {
    }

    public Todo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("completed")
    public boolean isCompleted() {
        return completed;
    }

    @JsonProperty("completed")
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}


```

## Repository Interface
src/main/java/com/example/todo/repository/TodoRepository.java
```java
package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}

```

## Service Class

   src/main/java/com/example/todo/service/TodoService.java

   ```java
   package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<Todo> findAll() {
        return repository.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return repository.findById(id);
    }

    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

   ```   

   ## Controller Class
   src/main/java/com/example/todo/controller/TodoController.java

   ```java
   package com.example.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        return service.findById(id)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return service.save(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        return service.findById(id)
                      .map(existing -> {
                          existing.setTitle(todo.getTitle());
                          existing.setCompleted(todo.isCompleted());
                          return ResponseEntity.ok(service.save(existing));
                      })
                      .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

   ```


   ## Running the App
   1. Start MySQL with Docker:

   ```bash
   docker-compose up -d

   ```

   2. Run the Spring Boot app:

   ```bash
   mvn spring-boot:run
   ```