package com.flab.mitdaa.user.service;

import com.flab.mitdaa.exception.ErrorType;
import com.flab.mitdaa.exception.MitdaException;
import com.flab.mitdaa.user.dto.RegisterRequestDto;
import com.flab.mitdaa.user.entity.User;
import com.flab.mitdaa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Transactional
    public void registerUser(RegisterRequestDto req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new MitdaException(ErrorType.EMAIL_DUPLICATED);
        }
        /* 유저 저장 */
        User user = User.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .email(req.email())
                .build();

         userRepository.save(user);


         /* Redis에 토큰 저장 */
         String token = verificationService.createVerificationToken(user.getEmail());

        sendEmailVerification(user, token);
    }

    public void sendEmailVerification(User user , String token) {
        String verificationUrl = "http://localhost:8088/api/verify?token=" + token;
        emailService.sendEmail(user.getEmail(), "이메일 인증을 완료해주세요.", "아래 링크를 클릭하여 이메일을 인증해주세요:\n" + verificationUrl);

    }

}
