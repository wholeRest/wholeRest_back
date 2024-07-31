package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByUser(User user);

    @Query("SELECT e FROM Event e WHERE e.user = :user AND e.date = :date")
    List<Event> findAllByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
}
