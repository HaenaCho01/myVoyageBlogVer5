package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.config.JPAConfiguration;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JPAConfiguration.class)
@Rollback()
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("Role - User 회원가입 성공 테스트")
	void insertUserTest() {
		// given
		var newUser = User.builder().username("user").password("password").role(UserRoleEnum.USER).build();

		// when
		var savedUser = userRepository.save(newUser);

		// then
		assertThat(savedUser).isNotNull();
	}
}
