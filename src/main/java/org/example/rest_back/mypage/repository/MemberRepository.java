package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
