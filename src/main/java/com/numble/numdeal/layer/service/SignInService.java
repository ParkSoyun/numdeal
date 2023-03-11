package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.domain.Seller;
import com.numble.numdeal.layer.domain.User;
import com.numble.numdeal.layer.form.SignInRequestForm;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.repository.SellerRepository;
import com.numble.numdeal.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    // 로그인
    public SignInResponseDto signIn(SignInRequestForm signInRequestDto) {
        String authority = signInRequestDto.getAuthority();

        return authority.equals(Constants.AUTHORITY_USER)
                    ? userSignIn(signInRequestDto.getId(), signInRequestDto.getPassword())
                    : sellerSignIn(signInRequestDto.getId(), signInRequestDto.getPassword());
    }

    // user 로그인
    public SignInResponseDto userSignIn(String id, String password) {
        User user = getUserById(id);

        if(isWrongPassword(user.getPassword(), password)) {
            throw new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return new SignInResponseDto(user.getEmail(), user.getName(), Constants.AUTHORITY_USER);
    }

    // user 회원이 맞는지 확인
    private User getUserById(String id) {
        return userRepository.findByEmail(id)
                .orElseThrow(() -> new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    // seller 로그인
    public SignInResponseDto sellerSignIn(String id, String password) {
        Seller seller = getSellerById(id);

        if(isWrongPassword(seller.getPassword(), password)) {
            throw new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return new SignInResponseDto(seller.getEmail(), seller.getName(), Constants.AUTHORITY_SELLER);
    }

    // seller 회원이 맞는지 확인
    private Seller getSellerById(String id) {
        return sellerRepository.findByEmail(id)
                .orElseThrow(() -> new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    // 비밀번호가 맞는지 확인
    private boolean isWrongPassword(String password, String inputPassword) {
        return !password.equals(inputPassword);
    }
}
