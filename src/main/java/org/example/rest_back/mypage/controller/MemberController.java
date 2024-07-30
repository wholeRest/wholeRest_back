package org.example.rest_back.mypage.controller;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/member")
public class MemberController {
    private final MemberRepository memberRepository;

    //Read
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers(){
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    //Create
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody Member member){
        Member newMember = new Member();
        newMember.setMember_id(member.getMember_id());
        newMember.setName(member.getName());
        memberRepository.save(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Delete
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable String id){
        memberRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
