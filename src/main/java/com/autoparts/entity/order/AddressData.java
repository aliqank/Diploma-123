package com.autoparts.entity.order;

import lombok.*;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AddressData {

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
}
