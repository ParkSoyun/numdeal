package com.numble.numdeal.layer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column(unique = true, length = 320, nullable = false)
    private String email;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 13, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean enabled;
}
