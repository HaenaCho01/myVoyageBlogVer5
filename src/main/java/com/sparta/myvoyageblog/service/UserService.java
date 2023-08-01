package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.SignupRequestDto;

public interface UserService {

    /**
     * 사용자 회원 가입
     * @param requestDto 사용자 회원가입 요청정보
     */
    void signup(SignupRequestDto requestDto);
}