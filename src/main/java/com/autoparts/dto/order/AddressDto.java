package com.autoparts.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postcode;
    private String email;
    private String phone;
    private boolean isDefault;
}
