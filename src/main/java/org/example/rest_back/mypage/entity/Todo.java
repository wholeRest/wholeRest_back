package org.example.rest_back.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int todo_id;

//    @ManyToOne
//    private String user_id;

    private String content;
    private Boolean completed;

    @CreationTimestamp
    private LocalDateTime create_time;

    @CreationTimestamp
    private LocalDateTime update_time;

}
