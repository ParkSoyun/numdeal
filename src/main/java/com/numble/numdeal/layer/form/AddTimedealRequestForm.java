package com.numble.numdeal.layer.form;

import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.domain.Seller;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddTimedealRequestForm {

    @NotBlank(message = "상품명을 입력해주세요.")
    @Length(max = 100, message = "100자 이하로 입력해주세요.")
    private String name;

    @PositiveOrZero
    private int regularPrice;

    @NotNull(message = "상품 판매가를 입력해주세요.")
    @PositiveOrZero
    private int salePrice;

    @NotNull(message = "재고량을 입력해주세요.")
    @PositiveOrZero
    private int stock;

    @NotNull(message = "상품 이미지를 등록해주세요.")
    private MultipartFile imageFile;

    @NotBlank(message = "타임딜 시작 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyyMMdd HH:mm:ss")
    private String openDateTime;

    @NotBlank(message = "타임딜 종료 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyyMMdd HH:mm:ss")
    private String closeDateTime;

    public Product toEntity(Seller seller, String imageFileName) {
        return Product.builder()
                .seller(seller)
                .name(name)
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .stock(stock)
                .imageFile(imageFileName)
                .openTime(LocalDateTime.parse(openDateTime))
                .closeTime(LocalDateTime.parse(closeDateTime))
                .build();
    }
}