package com.autoparts.serivce;

import com.autoparts.dto.user.UserCreateDto;
import com.autoparts.dto.user.UserReadDto;
import com.autoparts.dto.user.UserUpdateDto;
import com.autoparts.entity.User;
import com.autoparts.entity.enums.Role;
import com.autoparts.mapper.user.UserCreateMapper;
import com.autoparts.mapper.user.UserReadMapper;
import com.autoparts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public UserReadDto create(UserCreateDto userCreateDto) {

        if(userRepository.existsByUsername(userCreateDto.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username already exists"
            );
        }

        if(userRepository.existsByEmail(userCreateDto.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already exists"
            );
        }


        return Optional.of(userCreateDto)
                .map(userCreateDto1 -> {
                    Optional.of(userCreateDto.getPassword())
                            .filter(StringUtils::hasText)
                            .map(passwordEncoder::encode)
                            .ifPresent(userCreateDto1::setPassword);
                    return userCreateMapper.toEntity(userCreateDto1);
                })
                .map(entity -> {
                    entity.setRole(Role.USER);
                    entity.setAvatar("https://images.unsplash.com/photo-1633332755192-727a05c4013d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHw%3D&w=1000&q=80");
                    return userRepository.save(entity);
                })
                .map(userReadMapper::toDto)
                .orElseThrow();
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::toDto)
                .toList();
    }

    public Optional<UserReadDto> currentUserProfile() {
        var currentUser = getCurrentUser().orElseThrow();
        return userRepository.findByUsername(currentUser)
                .map(userReadMapper::toDto);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::toDto);
    }

    @Transactional
    public UserReadDto update(String username, UserUpdateDto userUpdateDto){

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        userCreateMapper.update(user, userUpdateDto);

        return Optional.of(user)
                .map(userRepository::save)
                .map(userReadMapper::toDto)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
//    @Cacheable("user")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    @Transactional()
    public User findByUsernameAndPassword(String username, String password) {
        var user = findByUsername(username);
        return Objects.nonNull(user) && passwordEncoder.matches(password, user.getPassword()) ? user : null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public Optional<String> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return Optional.of(currentUserName);
        }
        return Optional.empty();
    }

    public Optional<User> getCurrentUserObject() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName);
        }
        return Optional.empty();
    }

    public Optional<UserReadDto> getCurrentUserDetails(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            var user = userRepository.findByUsername(currentUserName).orElseThrow(() -> new RuntimeException("User not found"));

            return Optional.of(user).map(userReadMapper::toDto);
        }
        return Optional.empty();
    }


    public Optional<List<String>> getCurrentUserRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // Get the list of authorities from the Authentication object
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Convert authorities to a list of role names
            List<String> roleNames = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return Optional.of(roleNames);
        }

        return Optional.empty();
    }



    public UserReadDto getUserById(Long id) {
        return userRepository.getUserById(id)
                .map(userReadMapper::toDto)
                .orElseThrow();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
