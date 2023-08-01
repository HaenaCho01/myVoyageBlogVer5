package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class UserServiceImplTest {
	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Role - User 회원가입 성공 테스트")
	void signUpSuccessRoleUserTest() {
		// given
		String username = "user123";
		String password = "password123";
		String encodedPassword = "encodedPassword123";
		SignupRequestDto requestDto = SignupRequestDto.builder()
				.username(username)
				.password(password)
				.passwordCheck(password)
				.build();
		given(userRepository.findByUsername(username)).willReturn(Optional.empty());
		given(passwordEncoder.encode(password)).willReturn(encodedPassword);

		// when
		userService.signup(requestDto);

		// then
		// userRepository.save() 메소드가 1번 호출되었는지 확인
		then(userRepository).should(times(1)).save(any(User.class));
	}
}