package com.numble.numdeal.layer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto {
    private Long id;
    private String email;
    private String name;
    private String authority;
}
