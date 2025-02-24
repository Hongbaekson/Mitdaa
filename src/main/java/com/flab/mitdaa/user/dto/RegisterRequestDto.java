package com.flab.mitdaa.user.dto;


import com.flab.mitdaa.exception.ErrorType;
import com.flab.mitdaa.exception.MitdaException;
import lombok.NonNull;

import java.io.Serializable;

// validation 체크 필요 인터페이스
public record RegisterRequestDto(
        @NonNull
        String username ,
        @NonNull
        String password,
        @NonNull
        String email) implements Serializable , ValidCheck  {


    @Override
    public void check() {
        // 유저 이름 길이 체크 (3 ~ 20자)
        if (username.length() < 3 || username.length() > 10) {
            throw new MitdaException(ErrorType.INVALID_USERNAME);
        }
        // 패스워드 규격 체크 (최소 6자 이상)
        if (password.length() < 6) {
            throw new MitdaException(ErrorType.INVALID_PASSWORD);
        }
        // 이메일 형식 체크
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new MitdaException(ErrorType.INVALID_EMAIL);
        }
    }
}




