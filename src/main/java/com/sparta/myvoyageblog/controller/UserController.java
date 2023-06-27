package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.dto.UserInfoDto;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

//    @GetMapping("/user/login")
//    public String loginSuccess() {
//        return "로그인이 완료되었습니다.";
//    }
//
//    @GetMapping("/user/signup")
//    public String signupSucceess() {
//        return "회원가입이 완료되었습니다.";
//    }

    @PostMapping("/user/signup")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "회원가입에 실패하였습니다.";
        }

        userService.signup(requestDto);

        return "회원가입에 성공하였습니다.";
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