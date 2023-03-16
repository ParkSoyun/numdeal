package com.numble.numdeal.layer.form;

import com.numble.numdeal.layer.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class UserSignUpRequestForm {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해주세요.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}", message = "영문자, 숫자, 특수문자(!@#$%^&*)를 모두 포함하여 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(max = 20, message = "20자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private String birthDate;

    @NotBlank(message = "성별을 선택해주세요.")
    private String gender;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    public User toEntity() {
        return User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .birthDate(LocalDate.parse(birthDate))
                    .gender(gender.charAt(0))
                    .phoneNumber(phoneNumber)
                    .enabled(true)
                    .build();
    }
}