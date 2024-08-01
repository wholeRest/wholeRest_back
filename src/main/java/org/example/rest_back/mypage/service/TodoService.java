package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.exception.NotFoundException;
import org.example.rest_back.mypage.dto.TodoDto;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Todo;
import org.example.rest_back.mypage.repository.EventRepository;
import org.example.rest_back.mypage.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final EventRepository eventRepository;

    //Read
    public List<TodoDto> getTodosByEventId(int event_id){
        Event event = eventRepository.findById(event_id).orElse(null);
        List<Todo> todos = todoRepository.findByEvent(event);
        return todos.stream()
                .map(TodoDto::from)
                .collect(Collectors.toList());
    }

    //Create
    public void createTodo(int event_id, TodoDto todoDto) {
        Event event = eventRepository.findById(event_id).orElse(null);
        if (event == null)
            return;

        Todo todo = new Todo();
        todo.setEvent(event);
        todo.setContent(todoDto.getContent());
        todo.setCompleted(todoDto.getCompleted());
        todoRepository.save(todo);
    }

    //Update
    public void updateTodo(int todo_id, TodoDto todoDto){
        //todo id값으로 todo정보 가져오기
        Todo todo = todoRepository.findById(todo_id).orElse(null);
        if(todo == null){
            throw new NotFoundException("해당하는 todo정보가 존재하지 않습니다.");
        }

        //todoDto에 정보가 있을 경우 수정
        if(todoDto.getContent()!=null)
            todo.setContent(todoDto.getContent());
        if(todoDto.getCompleted()!=null)
            todo.setCompleted(todoDto.getCompleted());
        todoRepository.save(todo);
    }

    //Delete
    public void deleteTodo(int todo_id){
        todoRepository.deleteById(todo_id);
    }
}
