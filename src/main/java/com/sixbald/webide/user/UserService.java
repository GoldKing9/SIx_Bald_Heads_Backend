package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.NicknameRequest;
import com.sixbald.webide.user.dto.request.PasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
         * 3. if문 작성
         * if(acceessToken == valid) {
         *   user.updatePassword(password)
         *   return Response.success("비밀번호 수정 성공")
         *  } else
         *      { throw new GlobalException(ErrorCode.EXPIRED_TOKEN)
         */
        return Response.success("비밀번호 수정 성공");
    }
}
