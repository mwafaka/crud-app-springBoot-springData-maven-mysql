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
