package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Schedule;
import org.example.rest_back.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByUser(User user);

    @Query("SELECT s FROM Schedule s WHERE s.user = :user AND " +
            "(s.start_date BETWEEN :startDate AND :endDate OR s.end_date BETWEEN :startDate AND :endDate OR " +
            "s.start_date <= :startDate AND s.end_date >= :endDate)")
    List<Schedule> findAllByUserAndDateRange(@Param("user") User user,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Schedule s WHERE s.user = :user AND :date BETWEEN s.start_date AND s.end_date")
    List<Schedule> findAllByUserAndDate(@Param("user") User user, @Param("date") LocalDate targetDate);
}
