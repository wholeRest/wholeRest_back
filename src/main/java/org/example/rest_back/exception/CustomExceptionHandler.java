package org.example.rest_back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // 전역적으로 적용하는 예외처리 클래스
public class CustomExceptionHandler {

    // DTO의 기본 valid를 처리한 뒤 프론트엔드에게 좀 더 직관적인 에러메세지를 전달하기 위함
    @ExceptionHandler(MethodArgumentNotValidException.class) // 특정 예외 핸들링 -> @Valid 어노테이션이 적용된 dto에 해당
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 특정 예외 발생시 상태 코드 지정 ( 400 )
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        //String, String : 오류 필드와 메세지 매핑

        ex.getBindingResult().getAllErrors().forEach(error -> {
            // 모든 에러 순회하면서
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            // valid 에 걸린 error 메세지 추가
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        // 로그인 시도 : 아이디가 일치하지 않을 때, Exception 핸들링 
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        // 로그인 시도 : 비밀번호가 일치하지 않을 때, Exception 핸들링
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 위에 있는 2가지 CustomException에 대한 부분 핸들링 했는데
    // 내가 커스텀 한 메세지가 안나옴..
    //"message": "Unauthorized",

    /*
    * Spring Security에서 AuthenticationException이 발생하면, 기본적으로 "Unauthorized"라는 메시지가 반환됨 ( 디폴트 )
    * UserNotFoundException과 InvalidPasswordException이 AuthenticationException의 처리 과정에서 무시되는 거 같음..
    * */

    // 비밀번호와 비밀번호 재입력과 같은 두가지 필드값을 입력받을때
    @ExceptionHandler(PasswordNotEqualException.class)
    public ResponseEntity<Map<String, String>> handlePasswordNotEqualException(PasswordNotEqualException ex) {
        // 로그인 시도 : 비밀번호가 일치하지 않을 때, Exception 핸들링
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // IllegalArumentException : 전달된 값이 적절하지 않을 때 throw 하는 exception ( AuthController 참고 )
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
