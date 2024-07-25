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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    @Column(unique = true)
    private LocalDate date;

    private int emoji;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private String morning_image_url;
    private String lunch_image_url;
    private String dinner_image_url;
    private String today_memo;
}
