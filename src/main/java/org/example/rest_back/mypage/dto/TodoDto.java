package org.example.rest_back.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.entity.Todo;
import org.example.rest_back.mypage.repository.MemberRepository;
import org.example.rest_back.mypage.repository.TodoRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TodoDto {
    private int todo_id;
    private String member_id;
    private String content;
    private Boolean completed;
    private LocalDateTime create_time;


    public TodoDto() {
        this.completed = false;
    }

    public static TodoDto from(Todo todo){
        TodoDto dto = new TodoDto();
        dto.setTodo_id(todo.getTodo_id());
        dto.setMember_id(todo.getMember().getMember_id());
        dto.setCreate_time(todo.getCreate_time());
        dto.setContent(todo.getContent());
        dto.setCompleted(todo.getCompleted());
        return dto;
    }
}
