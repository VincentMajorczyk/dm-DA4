package com.example.todo_back.todo;
import com.example.todo_back.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public List<Todo> getAll(){
        return todoService.getAll();
    }

    @GetMapping("/{id}")
    public Todo getById(Long id) throws ResourceNotFoundException {
        try{
            return todoService.getById(id);
        }
        catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("Projet", 1);
        }
    }

    @PostMapping("")
    public ResponseEntity create(Todo uneTodo) throws ResourceAlreadyExistsException {
        try {
            Todo todo = todoService.create(uneTodo);
            return ResponseEntity.created(URI.create("/todos/" + todo.getId())).build();
        }
        catch (ResourceAlreadyExistsException e){
            throw new ResourceAlreadyExistsException("Todo", uneTodo.getId());
        }
    }

    @PutMapping("")
    public ResponseEntity update(Todo uneTodo) throws ResourceNotFoundException{
        try {
            todoService.update(uneTodo);
            return ResponseEntity.noContent().build();
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Todo", 1);
        }
    }

    @DeleteMapping("")
    public ResponseEntity delete(Todo uneTodo) throws  ResourceNotFoundException{
        try {
            todoService.delete(uneTodo);
            return ResponseEntity.noContent().build();
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Projet", 1);
        }
    }

}
