package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.form.SellerSignUpRequestForm;
import com.numble.numdeal.layer.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    // ID 중복 확인
    public boolean checkExistsId(String id) {
        return sellerRepository.existsByEmail(id);
    }

    // 회원가입
    public ResultResponseDto signUp(SellerSignUpRequestForm sellerSignUpRequestForm) {
        if(!checkPassword(sellerSignUpRequestForm.getPassword(), sellerSignUpRequestForm.getPasswordCheck())) {
            throw new IllegalStateException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        sellerRepository.save(sellerSignUpRequestForm.toEntity());

        return new ResultResponseDto(HttpStatus.OK, "회원가입에 성공하였습니다.");
    }

    // 비밀번호와 비밀번호 확인이 같은지 체크
    private boolean checkPassword(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }
}
