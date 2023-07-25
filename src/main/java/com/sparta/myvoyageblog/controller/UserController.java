package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.ApiResponseDto;
import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException("username은 3~10자이며 알파벳 대소문자와 숫자로, password는 4~15자이며 알파벳 대소문자와 숫자, 특수문자로 구성하여 다시 시도해주세요.");
        } else {
           userService.signup(requestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("회원가입이 완료되었습니다.", HttpStatus.OK.value()));
        }
    }

}