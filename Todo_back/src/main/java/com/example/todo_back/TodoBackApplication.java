package com.example.todo_back;

import com.example.todo_back.todo.Todo;
import com.example.todo_back.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class TodoBackApplication {

	@Autowired
	private TodoRepository todoRepository;

	public static void main(String[] args) {
		SpringApplication.run(TodoBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUpBDD() {
		return (args) -> {
			List<Todo> todos = new ArrayList<>();
			todos.add(new Todo(1L, "Faire ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));
			todos.add(new Todo(2L, "Faire cela", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache pas encore accompli"));
			todos.add(new Todo(3L, "Faire ceci avec cela", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));
			todos.add(new Todo(4L, "Faire cela avec ceci", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "tache accompli"));

			todoRepository.saveAll(todos);
		};
	}
}
