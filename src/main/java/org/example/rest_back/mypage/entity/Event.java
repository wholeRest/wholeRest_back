package org.example.rest_back.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.user.domain.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false)
    private LocalDate date;

    private int emoji;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private String morning_image_url;
    private String lunch_image_url;
    private String dinner_image_url;
    private String morning_image_filename;
    private String lunch_image_filename;
    private String dinner_image_filename;
    private String today_memo;
}
