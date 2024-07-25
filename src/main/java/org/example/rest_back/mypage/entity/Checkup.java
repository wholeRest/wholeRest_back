package org.example.rest_back.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Checkup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkup_id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private String content;
    private Boolean completed;

    @CreationTimestamp
    private LocalDate create_time;
}
