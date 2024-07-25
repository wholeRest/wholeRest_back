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

    @GetMapping("/{memberId}")
    public ResponseEntity<List<TodoDto>> getTodosByMemberId(@PathVariable String memberId){
        List<TodoDto> todos = todoService.getTodosByMemberId(memberId);
        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(@RequestBody TodoDto todoDto){
        todoService.createTodo(todoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTodoPartial(@PathVariable int id, @RequestBody TodoDto todoDto) {
        todoService.updateTodo(id, todoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }
}
