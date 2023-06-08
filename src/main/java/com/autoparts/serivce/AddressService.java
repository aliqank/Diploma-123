package com.autoparts.serivce;

import com.autoparts.dto.order.AddressDto;
import com.autoparts.dto.user.UserReadDto;
import com.autoparts.entity.User;
import com.autoparts.entity.order.Address;
import com.autoparts.mapper.AddressMapper;
import com.autoparts.mapper.user.UserCreateMapper;
import com.autoparts.mapper.user.UserReadMapper;
import com.autoparts.repository.UserRepository;
import com.autoparts.repository.order.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;
    private final AddressMapper addressMapper;
    private final UserService userService;

    @Transactional
    public AddressDto create(AddressDto addressDto) {

        return Optional.of(addressDto)
                .map(s->{
                    Address address = addressMapper.toEntity(s);
                    var currentUser = userService.getCurrentUserObject()
                            .orElseThrow();
                    address.setUser(currentUser);
                    address.setIsDefault(false);
                    return address;
                })
                .map(addressRepository::save)
                .map(addressMapper::toDto)
                .orElseThrow();
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::toDto)
                .toList();
    }

    public List<AddressDto> findUserAddress() {
        String currentUserName = userService.getCurrentUser().orElseThrow();
        User user = userService.findByUsername(currentUserName);
        return addressRepository.findAllByUser(user).stream()
                .map(addressMapper::toDto)
                .toList();
    }

    public Optional<AddressDto> findUserDefaultAddress() {
        String currentUserName = userService.getCurrentUser().orElseThrow();
        User user = userService.findByUsername(currentUserName);
        return addressRepository.findTopByUserAndIsDefault(user, true).stream()
                .map(addressMapper::toDto).findFirst();
    }

    public Optional<AddressDto> findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto);

    }

    @Transactional
    public AddressDto update(Long id, AddressDto addressDto){
        Address address = addressRepository.findById(id)
                .orElseThrow();
        addressMapper.update(address, addressDto);
        address.setIsDefault(addressDto.isDefault());

        return Optional.of(address)
                .map(addressRepository::save)
                .map(addressMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public boolean delete(Long id) {
        return addressRepository.findById(id)
                .map(entity -> {
                    addressRepository.delete(entity);
                    addressRepository.flush();
                    return true;
                })
                .orElse(false);
    }


}
