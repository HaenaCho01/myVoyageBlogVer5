package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.dto.UserInfoDto;
import com.sparta.myvoyageblog.dto.UserResponseDto;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public UserResponseDto signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult, HttpServletResponse response) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            response.setStatus(400);
            UserResponseDto failureSignup = new UserResponseDto("회원가입", "실패", response.getStatus());
            return failureSignup;
        } else {
            userService.signup(requestDto);

            UserResponseDto succeessSignup = new UserResponseDto("회원가입", "성공", response.getStatus());
            return succeessSignup;
        }
    }

    @GetMapping("/user/login/success")
    public UserResponseDto loginSucceess(HttpServletResponse response) {
        UserResponseDto succeessResponse = new UserResponseDto("로그인", "성공", response.getStatus());
        return succeessResponse;
    }

    @GetMapping("/user/login/unsuccess")
    public UserResponseDto loginUnsucceess(HttpServletResponse response) {
        response.setStatus(401);
        UserResponseDto succeessResponse = new UserResponseDto("로그인", "실패", response.getStatus());
        return succeessResponse;
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
    }

//    @GetMapping("/user-post")
//    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        model.addAttribute("folders", folderService.getFolders(userDetails.getUser()));
//
//        return "index :: #fragment";
//    }
}