package org.example.rest_back.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping
    public ResponseEntity<List<TodoDto>> getTodosByMemberId(HttpServletRequest request){
        List<TodoDto> todos = todoService.getTodosByMemberId(request);
        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(@RequestBody TodoDto todoDto, HttpServletRequest request){
        todoService.createTodo(todoDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable int id, @RequestBody TodoDto todoDto, HttpServletRequest request) {
        todoService.updateTodo(id, todoDto, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id, HttpServletRequest request) {
        todoService.deleteTodo(id, request);
        return ResponseEntity.ok().build();
    }
}