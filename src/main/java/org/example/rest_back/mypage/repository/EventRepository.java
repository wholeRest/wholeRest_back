package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByMember(Member member);

    @Query("SELECT e FROM Event e WHERE e.member.member_id = :memberId AND e.date = :date")
    List<Event> findAllByMemberIdAndDate(@Param("memberId") String memberId, @Param("date") LocalDate date);
}
