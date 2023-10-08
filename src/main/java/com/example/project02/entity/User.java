package com.example.project02.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    private String phone;

    private String address;

    private String gender;

    private String status;

    @Column(length = 10, nullable = false)
    private String type;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;

        @Builder
        public SmsCertificationRequest(String phone, String certificationNumber) {
            this.phone = phone;
            this.certificationNumber = certificationNumber;
        }

    }

}
