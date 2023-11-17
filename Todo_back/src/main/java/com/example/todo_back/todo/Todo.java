package com.example.todo_back.todo;

import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Todo {
    @Id
    private long id;

    private String contenu;

    private Timestamp dateCreation;

    private Timestamp datCloture;

    private String etat;

    public Todo(long id, String contenu, Timestamp dateCreation, Timestamp datCloture, String etat) {
        this.id = id;
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.datCloture = datCloture;
        this.etat = etat;
    }

    public Todo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Timestamp getDatCloture() {
        return datCloture;
    }

    public void setDatCloture(Timestamp datCloture) {
        this.datCloture = datCloture;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id && Objects.equals(contenu, todo.contenu) && Objects.equals(dateCreation, todo.dateCreation) && Objects.equals(datCloture, todo.datCloture) && Objects.equals(etat, todo.etat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contenu, dateCreation, datCloture, etat);
    }
}
