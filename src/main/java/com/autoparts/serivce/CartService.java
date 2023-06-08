package com.autoparts.serivce;

import com.autoparts.entity.Cart;
import com.autoparts.entity.Wishlist;
import com.autoparts.repository.CartRepository;
import com.autoparts.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public Wishlist create(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> findAllWishlist() {
        return wishlistRepository.findAll();
    }
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }
}
