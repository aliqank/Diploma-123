package com.autoparts.controller;

import com.autoparts.entity.Cart;
import com.autoparts.entity.Wishlist;
import com.autoparts.serivce.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Cart cart) {
        cartService.create(cart);
    }

    @PostMapping("/wishlist")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Wishlist wishlist) {
        cartService.create(wishlist);
    }
}
