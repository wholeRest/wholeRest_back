package org.example.rest_back.mypage.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.mypage.dto.TodoDto;
import org.example.rest_back.mypage.entity.Todo;
import org.example.rest_back.exception.NotFoundException;
import org.example.rest_back.exception.UnauthorizedException;
import org.example.rest_back.mypage.repository.TodoRepository;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    //Read
    public List<TodoDto> getTodosByMemberId(HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        List<Todo> todos = todoRepository.findByUser(user);
        return todos.stream()
                .map(TodoDto::from)
                .collect(Collectors.toList());
    }

    //Create
    public void createTodo(TodoDto todoDto, HttpServletRequest request) {
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        Todo todo = new Todo();
        todo.setUser(user);
        todo.setContent(todoDto.getContent());
        todo.setCompleted(todoDto.getCompleted());
        todoRepository.save(todo);
    }

    //Update
    public void updateTodo(int id, TodoDto todoDto, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //todo id값으로 todo정보 가져오기
        Todo todo = todoRepository.findById(id).orElse(null);
        if(todo == null){
            throw new NotFoundException("해당하는 todo정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 todo작성자가 일치하는지 판별
        if(todo.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        //todoDto에 정보가 있을 경우 수정
        if(todoDto.getContent()!=null)
            todo.setContent(todoDto.getContent());
        if(todoDto.getCompleted()!=null)
            todo.setCompleted(todoDto.getCompleted());
        todoRepository.save(todo);
    }

    //Delete
    public void deleteTodo(int id, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //todo id값으로 todo정보 가져오기
        Todo todo = todoRepository.findById(id).orElse(null);
        if(todo == null){
            throw new NotFoundException("해당하는 todo정보가 존재하지 않습니다.");
        }

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 todo작성자가 일치하는지 판별
        if(todo.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        todoRepository.deleteById(id);
    }
}
