package com.sparta.myvoyageblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myvoyageblog.config.WebSecurityConfig;
import com.sparta.myvoyageblog.controller.PostController;
import com.sparta.myvoyageblog.controller.UserController;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import com.sparta.myvoyageblog.mvc.MockSpringSecurityFilter;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.PostServiceImpl;
import com.sparta.myvoyageblog.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
		controllers = {UserController.class, PostController.class},
		excludeFilters = {
				@ComponentScan.Filter(
						type = FilterType.ASSIGNABLE_TYPE,
						classes = WebSecurityConfig.class
				)
		}
)

//@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	PostServiceImpl postServiceImpl;

	@MockBean
	UserServiceImpl userService;

	private Principal mockPrincipal;

	private static final String BASE_URL = "/api";

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity(new MockSpringSecurityFilter()))
				.build();
	}

	@BeforeEach
	private void mockUserSetup() {
		// Mock 테스트 유져 생성
		String username = "sollertia4351";
		String password = "robbie1234";
		UserRoleEnum role = UserRoleEnum.USER;
		User testUser = new User(username, password, role);
		UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
		mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
	}

	@Test
	@DisplayName("게시글 저장 테스트")
	void save_test() throws Exception {
		//given
		String title = "Test title";
		String content = "Test content";

		String body = mapper.writeValueAsString(
				PostRequestDto.builder().content(content).title(title).build()
		);

		//when - then
		mvc.perform(post(BASE_URL + "/posts")
						.content(body) //HTTP Body에 데이터를 담는다
						.contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
						.accept(MediaType.APPLICATION_JSON) // 받는 데이터의 타입을 명시
						.principal(mockPrincipal)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}
}