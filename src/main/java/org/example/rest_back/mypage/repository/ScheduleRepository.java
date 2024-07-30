package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Schedule;
import org.example.rest_back.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByUser(User user);

    @Query("SELECT s FROM Schedule s WHERE " +
            "s.user = :user AND (" +
            "(YEAR(s.start_date) = :year AND MONTH(s.start_date) = :month) OR " +
            "(YEAR(s.end_date) = :year AND MONTH(s.end_date) = :month) OR " +
            "(s.start_date <= :endOfMonth AND s.end_date >= :startOfMonth))")
    List<Schedule> findAllByUserAndYearAndMonth(@Param("user") User user,
                                                      @Param("year") int year, @Param("month") int month,
                                                      @Param("startOfMonth") LocalDate startOfMonth,
                                                      @Param("endOfMonth") LocalDate endOfMonth);
}
