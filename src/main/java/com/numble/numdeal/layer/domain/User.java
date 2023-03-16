package com.numble.numdeal.layer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 320, nullable = false)
    private String email;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 1, nullable = false)
    private Character gender;

    @Column(length = 13, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean enabled;
}
