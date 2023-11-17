package com.example.todo_back.todo;

import com.example.todo_back.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    List<Todo> getAll();

    Todo create(Todo uneTodo) throws ResourceAlreadyExistsException;

    Todo getById(Long id) throws ResourceNotFoundException;

    Todo update(Todo expectedTodo) throws ResourceNotFoundException;

    void delete(Todo toDelete) throws  ResourceNotFoundException;
}
