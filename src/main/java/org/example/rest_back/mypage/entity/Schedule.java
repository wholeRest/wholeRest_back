package org.example.rest_back.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schedule_id;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    private LocalDate start_date;
    private LocalDate end_date;
    private String schedule_color;
}
