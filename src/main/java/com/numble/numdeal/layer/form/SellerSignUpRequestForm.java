package com.numble.numdeal.layer.form;

import com.numble.numdeal.layer.domain.Seller;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SellerSignUpRequestForm {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해주세요.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}", message = "영문자, 숫자, 특수문자(!@#$%^&*)를 모두 포함하여 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    @NotBlank(message = "브랜드명을 입력해주세요.")
    @Length(max = 20, message = "20자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "대표번호를 입력해주세요.")
    private String phoneNumber;

    public Seller toEntity() {
        return Seller.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNumber(phoneNumber)
                .enabled(true)
                .build();
    }
}

