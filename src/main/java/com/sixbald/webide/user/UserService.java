package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.RedisUtil;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.EmailCheckRequest;
import com.sixbald.webide.user.dto.request.NicknameRequest;
import com.sixbald.webide.user.dto.request.PasswordRequest;
import com.sixbald.webide.user.dto.request.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.mail.username}")
    private String sendEmail;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    public Response<Void> nicknameCheck(NicknameRequest request) {
        String nickname = request.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
        }
        return Response.success("사용가능한 닉네임 입니다");
    }


    // Todo : Token 에 대한 예외처리 해줘야 한다.
    public Response<Void> passwordEdit(PasswordRequest request) {
        String password = request.getPassword();

        /**
         * 1. 검증된 로그인 유저 id 값 가져오기
         * 2. userRepository 에서 유저찾기 ex) userRepository.findBYId(검증된 로그인 유저 id)
         * 3. 코드 구현
         * User user = userRepsitory.findById(id);
         * user.updatePassword(password);
         * return Response.success("비밀번호 수정 성공");
         */
        return Response.success("비밀번호 수정 성공");
    }

    public Response<Void> sendMail(SendEmailRequest request) {
        String email = request.getEmail();
        String authCode = createdCode();
        if (userRepository.existsByEmail(email)) {
            throw new GlobalException(ErrorCode.DUPLICATED_EMAIL);
        } else {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Web_IDE 인증번호 입니다.");
            simpleMailMessage.setText("이메일 인증번호는 " + authCode + "입니다");
            simpleMailMessage.setFrom(sendEmail);
            mailSender.send(simpleMailMessage);
            redisUtil.setDataExpire(email, authCode, 5 * 60L);

            return Response.success("인증 번호 발송");
        }
    }

    public Response<Void> emailCheck(EmailCheckRequest request) {
        String code = request.getCode();
        String email = request.getEmail();

        String codeRoundByEmail = redisUtil.getData(email);

        if (codeRoundByEmail == null) {
            throw new GlobalException(ErrorCode.EXPIRED_AUTHENTICATION_TIME);
        } else if (!codeRoundByEmail.equals(code)) {
            throw new GlobalException(ErrorCode.MISMATCHED_CODE);
        }

        redisUtil.deleteData(email);
        return Response.success("이메일 인증에 성공했습니다.");
    }

    private String createdCode() {

        String CHAR_SET = "0123456789";
        int CODE_LENGTH = 6;
        Random RANDOM = new Random();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHAR_SET.length());
            char randomChar = CHAR_SET.charAt(randomIndex);
            code.append(randomChar);
        }

        return code.toString();
    }
}
