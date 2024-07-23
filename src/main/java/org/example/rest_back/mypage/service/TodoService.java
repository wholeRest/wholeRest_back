package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.TodoDto;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.entity.Todo;
import org.example.rest_back.mypage.repository.MemberRepository;
import org.example.rest_back.mypage.repository.TodoRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    //Read
    public List<TodoDto> getTodosByMemberId(String member_id){
        Member member = memberRepository.findById(member_id).orElse(null);
        List<Todo> todos = todoRepository.findByMember(member);
        return todos.stream()
                .map(TodoDto::from)
                .collect(Collectors.toList());
    }

    //Create
    public void createTodo(TodoDto todoDto) {
        Member member = memberRepository.findById(todoDto.getMember_id()).orElse(null);
        if (member == null)
            return;

        Todo todo = new Todo();
        todo.setMember(member);
        todo.setContent(todoDto.getContent());
        todo.setCompleted(todoDto.getCompleted());
        todoRepository.save(todo);
    }

    //Update
    public void updateTodo(int id, TodoDto todoDto){
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null){
            if(todoDto.getContent()!=null)
                todo.setContent(todoDto.getContent());
            if(todoDto.getCompleted()!=null)
                todo.setCompleted(todoDto.getCompleted());
            todoRepository.save(todo);
        }
    }

    //Delete
    public void deleteTodo(int id){
        todoRepository.deleteById(id);
    }
}
