package com.example.todo_back.todo;

import com.example.todo_back.exceptions.ResourceAlreadyExistsException;
import com.example.todo_back.exceptions.*;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class TodoJPAService implements TodoService{
    private TodoRepository todoRepository;


    public TodoJPAService(TodoRepository todoRepository1){
        this.todoRepository = todoRepository1;
    }

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo create(Todo uneTodo) throws ResourceAlreadyExistsException {
        if(todoRepository.exists(Example.of(uneTodo))){
            throw new ResourceAlreadyExistsException("Todo", uneTodo.getId());
        }
        else{
            todoRepository.save(uneTodo);
            return uneTodo;
        }
    }

    @Override
    public Todo getById(Long id) throws ResourceNotFoundException {
        Optional<Todo> found = todoRepository.findById(id);
        if(found.isEmpty()){
            throw new ResourceNotFoundException("Todo", id);
        }
        else{
            return found.get();
        }
    }

    @Override
    public Todo update(Todo uneTodo) throws ResourceNotFoundException {
        if(todoRepository.exists(Example.of(uneTodo))){
            todoRepository.save(uneTodo);
            return uneTodo;
        }
        else{
            throw new ResourceNotFoundException("Todo", uneTodo.getId());
        }
    }

    @Override
    public void delete(Todo toDelete) throws ResourceNotFoundException{
        if(todoRepository.exists(Example.of(toDelete))){
            todoRepository.delete(toDelete);
        }
        else{
            throw new ResourceNotFoundException("Todo", toDelete.getId());
        }

    }
}
