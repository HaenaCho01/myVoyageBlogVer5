package com.sparta.myvoyageblog.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myvoyageblog.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseUtil {

	// Response Body에 상태 담아 반환하기
	public void statusResponse (HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		UserResponseDto responseDto = new UserResponseDto(message, response.getStatus());
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(responseDto);
		response.getWriter().write(result);
	}
}
