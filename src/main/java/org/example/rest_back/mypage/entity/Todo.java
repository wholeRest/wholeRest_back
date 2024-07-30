package org.example.rest_back.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int todo_id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    private String content;
    private Boolean completed;

    @CreationTimestamp
    private LocalDate create_time;
}
