package com.autoparts.controller;

import com.autoparts.dto.user.UserReadDto;
import com.autoparts.dto.user.UserUpdateDto;
import com.autoparts.serivce.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<UserReadDto> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id){
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/myProfile")
    public Optional<UserReadDto> myProfile(){
        return userService.currentUserProfile();
    }

    @GetMapping("/currentUser")
    public Optional<UserReadDto> userRole(){
        return userService.getCurrentUserDetails();
    }

    @PutMapping("/{Username}")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody UserUpdateDto userUpdateDto, @PathVariable("Username") String Username) {
        userService.update(Username ,userUpdateDto);
    }
}
