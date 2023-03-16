package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.form.UserSignUpRequestForm;
import com.numble.numdeal.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // ID 중복 확인
    public boolean checkExistsId(String id) {
        return userRepository.existsByEmail(id);
    }

    // 회원가입
    public ResultResponseDto signUp(UserSignUpRequestForm userSignUpRequestForm) {
        if(!checkPassword(userSignUpRequestForm.getPassword(), userSignUpRequestForm.getPasswordCheck())) {
            throw new IllegalStateException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        userRepository.save(userSignUpRequestForm.toEntity());

        return new ResultResponseDto(HttpStatus.OK, "회원가입에 성공하였습니다.");
    }

    // 비밀번호와 비밀번호 확인이 같은지 체크
    private boolean checkPassword(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }
}
