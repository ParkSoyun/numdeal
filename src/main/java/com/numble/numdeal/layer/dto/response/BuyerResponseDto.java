package com.numble.numdeal.layer.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class BuyerResponseDto {
    private String orderDate;
    private String email;
    private String name;

    public BuyerResponseDto(LocalDateTime orderDate, String email, String name) {
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.email = email;
        this.name = name;
    }
}
