package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import com.sparta.myvoyageblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);

        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if (requestDto.getPassword().contains(username)) {
            throw new IllegalArgumentException("비밀번호에는 닉네임과 같은 값이 포함될 수 없습니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
         User user = new User(username, password, role);
        userRepository.save(user);
    }
}