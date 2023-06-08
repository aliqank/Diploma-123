package com.autoparts.entity;

import com.autoparts.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 32)
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    @Column(unique = true)
    private String email;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String apiToken;
    private String password;
}
