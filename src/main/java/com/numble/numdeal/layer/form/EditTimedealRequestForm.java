package com.numble.numdeal.layer.form;

import com.numble.numdeal.layer.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
public class EditTimedealRequestForm {

    private Long productId;

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

    private String imageFile;

    @NotBlank(message = "타임딜 시작 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyyMMdd HH:mm:ss")
    private String openDateTime;

    @NotBlank(message = "타임딜 종료 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyyMMdd HH:mm:ss")
    private String closeDateTime;

    public EditTimedealRequestForm(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.regularPrice = product.getRegularPrice();
        this.salePrice = product.getSalePrice();
        this.stock = product.getStock();
        this.imageFile = product.getImageFile();
        this.openDateTime = String.valueOf(product.getOpenTime());
        this.closeDateTime = String.valueOf(product.getCloseTime());
    }
}