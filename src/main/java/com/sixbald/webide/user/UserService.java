package com.sixbald.webide.user;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.config.utils.JwtUtils;
import com.sixbald.webide.domain.RefreshToken;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.RefreshTokenRepository;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.*;
import com.sixbald.webide.user.dto.response.UserDTO;
import com.sixbald.webide.user.dto.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sixbald.webide.config.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${spring.mail.username}")
    private String sendEmail;

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final S3Service s3Service;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional(readOnly = true)
    public UserDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        return UserDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userId(user.getId())
                .imageUrl(user.getProfileImgUrl())
                .build();

    }

    @Transactional
    public void updateUserProfileImage(Long userId, MultipartFile imageUrl) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );
        if (user.getProfileImgUrl() != null) {
            log.info("기존 이미지 경로 : {}", user.getProfileImgUrl());
            String result = s3Service.deleteFile(user.getProfileImgUrl()); //삭제 로직
            log.info("기존 프로필 이미지 삭제 :{}", result);
        }

        String imgPath = s3Service.upload(imageUrl); // 업로드
        log.info("새로 업로드된 이미지 경로 : {}", imgPath);
        user.updateImage(imgPath);
        userRepository.save(user);

    }

    @Transactional
    public Response<Void> updateNickname(Long userId, RequestNickname requestNickname) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );
        String updateNickname = requestNickname.getNickname();
        boolean nicknameValid = requestNickname.isNicknameValid();
        if (!nicknameValid) {
            throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
        }

        log.info("유저 : {}", user);
        user.updateNickname(updateNickname);
        return Response.success("프로필 닉네임 수정 성공");
    }
        
    public Response<Void> nicknameCheck(NicknameRequest request) {
        String nickname = request.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
        }
        return Response.success("사용가능한 닉네임 입니다");
    }

    @Transactional
    public Response<Void> passwordEdit(PasswordRequest request, LoginUser loginUser) {
        String password = request.getPassword();
        Long loginUserid = loginUser.getUser().getId();

        User user = userRepository.findById(loginUserid)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        String updatePwd = passwordEncoder.encode(password);
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalException(ErrorCode.ALREADY_USING_PASSWORD);
        } else {
            user.updatePassword(updatePwd);
            return Response.success("비밀번호 수정 성공");
        }
    }

    @Transactional
    public Response<Void> sendMail(SendEmailRequest request) {
        String email = request.getEmail();
        String authCode = createdCode();
        if (userRepository.existsByEmail(email)) {
            throw new GlobalException(ErrorCode.DUPLICATED_EMAIL);
        } else {
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Web_IDE 인증번호 입니다.");
            simpleMailMessage.setText("이메일 인증번호는 " + authCode + "입니다");
            simpleMailMessage.setFrom(sendEmail);
            mailSender.send(simpleMailMessage);
            redisUtil.setDataExpire(email, authCode, 5 * 60L);

            return Response.success("인증 번호 발송");
        }
    }

    @Transactional
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

    @Transactional
    public void signup(UserSignupRequest dto) {
        if (!dto.isEmailValid()) {
            throw new GlobalException(ErrorCode.UNCHECKED_EMAIL_VALID);
        } else if (!dto.isNicknameValid()) {
            throw new GlobalException(ErrorCode.UNCHECKED_NICKNAME_VALID);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = dto.toEntity();
        userRepository.save(user);
        PathUtils.createUserDirectory(user.getId());
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = jwtUtils.generateJwt(loginUser);
        String refreshToken = jwtUtils.createRefreshToken();

        refreshTokenRepository.save(RefreshToken.builder()
                .id("user")
                .loginUser(loginUser)
                .refreshToken(refreshToken)
                .build());

        return UserLoginResponse.builder()
                .userId(loginUser.getUser().getId())
                .nickname(loginUser.getUser().getNickname())
                .role(loginUser.getUser().getRole())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public UserLoginResponse reissue(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new GlobalException(ErrorCode.EXPIRED_REFRESH_TOKEN, "RefreshToken Expired")
        );

        LoginUser loginUser = token.getLoginUser();
        String newToken = jwtUtils.generateJwt(loginUser);

        return UserLoginResponse.builder()
                .userId(loginUser.getUser().getId())
                .nickname(loginUser.getUser().getNickname())
                .role(loginUser.getUser().getRole())
                .accessToken(newToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public Response<Void> findPassword(FindPasswordRequest request) {
        String email = request.getEmail();
        User findMember = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        if (findMember != null) {
            String tempPassword = getTempPassword();
            findMember.updatePassword(passwordEncoder.encode(tempPassword));
            userRepository.save(findMember);

            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Web_IDE 임시 비밀번호 발급" );
            simpleMailMessage.setText("임시 비밀번호는 " + tempPassword + "입니다");
            simpleMailMessage.setFrom(sendEmail);
            mailSender.send(simpleMailMessage);

        }
        return Response.success("임시 비밀번호 발급 성공");
    }

    public String getTempPassword() {
        // 영문 소문자와 숫자를 포함한 문자열 생성을 위한 문자셋
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();

        Random random = new Random();
        int passwordLength = 8 + random.nextInt(5); // 8에서 12 사이의 길이

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(randomIndex);
            tempPassword.append(randomChar);
        }

        return tempPassword.toString();
    }
}
        
