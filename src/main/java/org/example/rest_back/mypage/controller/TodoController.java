package org.example.rest_back.mypage.controller;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.TodoDto;
import org.example.rest_back.mypage.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/todo")
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<TodoDto>> getTodosByEventId(@PathVariable int eventId){
        List<TodoDto> todos = todoService.getTodosByEventId(eventId);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> createTodo(@PathVariable int eventId, @RequestBody TodoDto todoDto){
        todoService.createTodo(eventId, todoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<Void> updateTodo(@PathVariable int todoId, @RequestBody TodoDto todoDto) {
        todoService.updateTodo(todoId, todoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok().build();
    }
}