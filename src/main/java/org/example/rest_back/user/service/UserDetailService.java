package org.example.rest_back.user.service;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService{

    private final UserRepository userRepository;

    //고유값 - 사용자이름 / username - (userId) 을 통해, 사용자 정보를 가져오는 메소드
    //User 도메인 객체에서, 식별하기 위한 고유 값으로 userId 를 리턴하게 되어있음 (getUsername)
    //일관성을 위해 동일하게 설정
    public User loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
    }
}

