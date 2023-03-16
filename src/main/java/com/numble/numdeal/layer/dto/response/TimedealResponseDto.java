package com.numble.numdeal.layer.dto.response;

import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.domain.ProductStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class TimedealResponseDto {
    private Long productId;
    private Long sellerId;
    private String brandName;
    private String name;
    private int regularPrice;
    private int salePrice;
    private int discountRate;
    private int stock;
    private String imageFile;
    private String openTime;
    private String closeTime;

    public TimedealResponseDto(Long productId, Long sellerId, String brandName, String name, int regularPrice, int salePrice, int stock, String imageFile, LocalDateTime openTime, LocalDateTime closeTime) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.brandName = brandName;
        this.name = name;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.discountRate = (int) Math.floor((double) (regularPrice - salePrice) / regularPrice * 100);
        this.stock = stock;
        this.imageFile = imageFile;
        this.openTime = openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.closeTime = closeTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public TimedealResponseDto(Product product) {
        this.productId = product.getProductId();
        this.sellerId = product.getSeller().getSellerId();
        this.brandName = product.getSeller().getName();
        this.name = product.getName();
        this.regularPrice = product.getRegularPrice();
        this.salePrice = product.getSalePrice();
        this.discountRate = (int) Math.floor((double) (regularPrice - salePrice) / regularPrice * 100);
        this.stock = product.getStock();
        this.imageFile = product.getImageFile();
        this.openTime = String.valueOf(product.getOpenTime());
        this.closeTime = String.valueOf(product.getCloseTime());
    }
}
