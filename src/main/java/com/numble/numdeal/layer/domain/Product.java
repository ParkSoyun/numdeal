package com.numble.numdeal.layer.domain;

import com.numble.numdeal.layer.form.EditTimedealRequestForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private int regularPrice;

    @Column(nullable = false)
    private int salePrice;

    @Column(nullable = false)
    private int stock;

    @Column(length = 50, nullable = false)
    private String imageFileName;

    @Column(nullable = false)
    private LocalDateTime openTime;

    @Column(nullable = false)
    private LocalDateTime closeTime;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ProductStatusEnum status;

    public void reduceStock() {
        this.stock--;
    }

    public void edit(EditTimedealRequestForm editTimedealRequestForm) {
        this.name = editTimedealRequestForm.getName();
        this.regularPrice = editTimedealRequestForm.getRegularPrice();
        this.salePrice = editTimedealRequestForm.getSalePrice();
        this.stock = editTimedealRequestForm.getStock();
        this.openTime = LocalDateTime.parse(editTimedealRequestForm.getOpenDateTime());
        this.closeTime = LocalDateTime.parse(editTimedealRequestForm.getCloseDateTime());
    }
}
