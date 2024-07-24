package org.example.rest_back.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
//@AllArgsConstructor
@Table(name = "member")
public class User implements UserDetails {
    //UserDetails 를 상속받아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;
    // 중복 값 및 Null 값 허용되지 않게 설정

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(unique = true, nullable = false)
    private String nickName;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
//      PrePersist : 엔티티가 데베에 저장되기 전에 자동으로 호출됨.
        createdAt = LocalDateTime.now();
    }

    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // 디폴트로 사용자의 권한은 ROLE_USER 로 설정함
    }


    @Override // Spring Security에서 사용자 식별을 위해 사용되는 메소드
    public String getUsername() {
        return userId; // 식별을 위한 고유한 값;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 판단
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 확인
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드 만료 확인
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 사용 가능 여부 판단
        return true;
    }

    public User(String userId, String password, String name, String nickName, String email, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
