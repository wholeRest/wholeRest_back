package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Calendar;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    Calendar findByMember(Member member);
}
