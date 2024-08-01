package org.example.rest_back.user.service;

import org.example.rest_back.user.domain.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PwRestTokenService {
    /*
    * 해당 클래스 서비스가 필요한 이유
    * 피그마를 보면, 비밀번호 찾기는 3 단계의 방식으로 이루어짐
    * 1. 아이디, 성명, 생년월일을 통해서 User 정보를 얻고
    * 2. 이메일 인증 이후에
    * 3. 다시 해당하는 User 객체의 pw 를 변경하는 구조
    * 처음에는 findByColums 를 통해서 Service 로직에서 User 객체를 찾고 (1)
    * 해당하는 User 객체를 이용하여 User.changePassword와 같은 도메인 클래스 내부의 함수를 사용하려 했는데 (3)
    * 각각의 API 는 독립적으로 동작하기에 한 요청에서 생성된 User 객체는 다른 요청에서 직접적으로 참조할 수 없게 됨..
    * 이를 위해서 임시로 token 을 발급받고, 진행하는 과정이 필요. 
    * */

    private final Map<String, User> tokenStore = new HashMap<>();

    // 토큰 저장
    public void saveToken(String token, User user) {
        tokenStore.put(token, user);
    }

    // 토큰을 사용하여 사용자 조회
    public User getUserByToken(String token) {
        return tokenStore.get(token);
    }

    // 토큰 삭제 (비밀번호 변경 후 호출 등)
    public void deleteToken(String token) {
        tokenStore.remove(token);
    }
}
