package com.autoparts.entity.order;

import com.autoparts.entity.User;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_default")
    private Boolean isDefault;

    private String firstName;
    private String lastName;
    private String company;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postcode;
    private String email;
    private String phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
