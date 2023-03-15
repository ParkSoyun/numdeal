package com.numble.numdeal.layer.dto.response;

import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.config.ImageFileUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimedealResponseDto {
    private Long productId;
    private String brandName;
    private String name;
    private int regularPrice;
    private int salePrice;
    private int discountRate;
    private int stock;
    private String imageFile;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private ProductStatusEnum status;

    public TimedealResponseDto(Long productId, String brandName, String name, int regularPrice, int salePrice, int stock, String imageFileName, LocalDateTime openTime, LocalDateTime closeTime, ProductStatusEnum status) throws IOException {
        this.productId = productId;
        this.brandName = brandName;
        this.name = name;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.discountRate = (int) Math.floor((double) (regularPrice - salePrice) / regularPrice * 100);
        this.stock = stock;
        this.imageFile = ImageFileUtil.encode(imageFileName);
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
    }
}
