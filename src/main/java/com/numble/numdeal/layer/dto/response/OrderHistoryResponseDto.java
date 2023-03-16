package com.numble.numdeal.layer.dto.response;

import com.numble.numdeal.config.ImageFileUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class OrderHistoryResponseDto {
    private String orderDate;
    private Long productId;
    private String brandName;
    private String name;
    private int salePrice;
    private String imageFile;

    public OrderHistoryResponseDto(LocalDateTime orderDate, Long productId, String brandName, String name, int salePrice, String imageFileName) throws IOException {
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.productId = productId;
        this.brandName = brandName;
        this.name = name;
        this.salePrice = salePrice;
        this.imageFile = ImageFileUtil.encode(imageFileName);
    }
}
