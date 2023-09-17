package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.response.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void findUser() throws Exception{
        User user = User.builder()
                .id(1L)
                .email("sosak@gmail.com")
                .password("asdf")
                .profileImgUrl("asefnkdlsskdl.jpg")
                .nickname("sosak")
                .build();
        userRepository.save(user);

        //given
//        Response userInfo = userService.getUserInfo(user.getId());
        //when

        //then

    }


}