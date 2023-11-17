package com.example.todo_back.todo;

import com.example.todo_back.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = TodoController.class)
@Import(ExceptionHandlingAdvice.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private List<Todo> todos;

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
    void getTodos_shouldGet4TodosInJson() throws Exception {
        when(todoService.getAll()).thenReturn(todos);

        mockMvc.perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andDo(print());
    }

    @Test
    void getTodosWithId_shouldReturnCorrectTodo() throws Exception {
        Todo todo = todos.get(2);
        when(todoService.getById(any())).thenReturn(todo);

        mockMvc.perform(get("/todos/1"))
                .andExpect(jsonPath("$.etat", is(todo.getEtat())))
                .andDo(print());
    }

    @Test
    void getTodoWithNonExistingId_should404() throws Exception {
        when(todoService.getById(any())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/todos/101"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postNewTodo_shouldReturnHeaderWithLocation() throws Exception {
        Todo todo = new Todo(4L, "Faire cela avec ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        todo.setId(4L);
        when(todoService.create(any())).thenReturn(todo);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(header().string("Location", "/todos/"+todo.getId()))
                .andDo(print());
    }

    @Test
    void postExistingTodo_shouldReturn409() throws Exception {
        Todo todo = todos.get(1);
        when(todoService.create(any())).thenThrow(ResourceAlreadyExistsException.class);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void putExistingModifiedTodo_shouldReturnNoContent() throws Exception {
        Todo todo = todos.get(0);
        todo.setContenu("modified");
        when(todoService.update(any())).thenReturn(todo);

        mockMvc.perform(put("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void putNonExistingTodo_should404() throws Exception {
        Todo todo = new Todo(6L, "Faire cela avec ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        todo.setId(5L);
        when(todoService.update(any())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void deleteExistingTodo_shouldReturnNoContent() throws Exception {
        Todo todo = todos.get(3);

        mockMvc.perform(delete("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(todoService).delete(any());

    }

    @Test
    void deleteNonExistingTodo_should404() throws Exception {
        Todo todo = new Todo(5L, "Faire cela avec ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli");
        doThrow(ResourceNotFoundException.class).when(todoService).delete(any());

        mockMvc.perform(delete("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
