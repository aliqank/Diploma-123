package com.autoparts.controller;

import com.autoparts.dto.order.AddressDto;
import com.autoparts.serivce.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AddressDto addressDto) {
        addressService.create(addressDto);
    }

    @GetMapping()
    public List<AddressDto> findUserAddress(){
        return addressService.findUserAddress();
    }

    @GetMapping("/defaultAddress")
    public Optional<AddressDto> findUserDefaultAddress(){
        return addressService.findUserDefaultAddress();
    }

    @GetMapping("/{id}")
    public AddressDto findById(@PathVariable("id") Long id){
        return addressService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody AddressDto addressDto, @PathVariable("id") Long id) {
        addressService.update(id ,addressDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return addressService.delete(id)
                ? noContent().build()
                : notFound().build();
    }

}
