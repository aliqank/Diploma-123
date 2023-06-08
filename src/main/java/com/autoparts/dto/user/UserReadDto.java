package com.autoparts.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String avatar;
    private String role;

}
