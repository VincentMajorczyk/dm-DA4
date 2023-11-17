package com.example.todo_back.todo;

import com.example.todo_back.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TodoJPAService.class)
public class TodoJPAServiceTest {

    @Autowired
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;
    List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));
            add(new Todo(2L, "Faire cela", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache pas encore accompli"));
            add(new Todo(3L, "Faire ceci avec cela", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));
            add(new Todo(4L, "Faire cela avec ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));
        }};
    }

    @Test
    void whenGettingAll_shouldGet4() {
        when(todoRepository.findAll()).thenReturn(todos);
        assertEquals(4, todoService.getAll().size());
    }

    @Test
    void whenCreating_shouldReturnSame() {
        Todo todo = new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        when(todoRepository.save(any())).thenReturn(todo);

        assertEquals(todo, todoService.create(todo));
    }

    @Test
    void whenCreatingAlreadyExisting_shouldThrowException() {
        Todo todo = new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        when(todoRepository.exists(Example.of(todo))).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> todoService.create(todo));
    }

    @Test
    void whenGettingByExistingId_shouldReturnCorrectProjet() {
        Todo expected_todo = todos.get(0);
        when(todoRepository.findById(any())).thenReturn(Optional.of(expected_todo));

        assertEquals(expected_todo, todoService.getById(expected_todo.getId()));
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        Todo todo = new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        todo.setId(4L);
        when(todoRepository.exists(Example.of(todo))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> todoService.update(todo));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        Todo toDelete = new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        doThrow(ResourceNotFoundException.class).when(todoRepository).delete(any());

        assertThrows(ResourceNotFoundException.class, () -> todoService.delete(toDelete));
    }


}
