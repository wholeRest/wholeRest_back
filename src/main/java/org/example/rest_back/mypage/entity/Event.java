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

    //투두
    private String todoContent1;
    private String todoContent2;

    //약 복용 시간
    private String medicineContent1;
    private String medicineContent2;
    private String medicineContent3;
    private String medicineContent4;

    //검진 결과
    private String checkupContent1;
    private String checkupContent2;
    private String checkupContent3;
    private String checkupContent4;

    //주의 사항
    private String cautionContent1;
    private String cautionContent2;
    private String cautionContent3;
    private String cautionContent4;
}
