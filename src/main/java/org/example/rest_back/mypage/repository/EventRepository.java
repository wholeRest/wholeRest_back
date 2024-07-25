package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Calendar;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCalendar(Calendar calendar);

    @Query("SELECT e FROM Event e WHERE e.calendar.calendar_id = :calendarId AND YEAR(e.date) = :year AND MONTH(e.date) = :month")
    List<Event> findAllByCalendarIdAndYearAndMonth(@Param("calendarId") int calendarId, @Param("year") int year, @Param("month") int month);
}
