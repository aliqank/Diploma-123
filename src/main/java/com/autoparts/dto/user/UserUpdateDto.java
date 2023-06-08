package com.autoparts.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class UserUpdateDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
}
