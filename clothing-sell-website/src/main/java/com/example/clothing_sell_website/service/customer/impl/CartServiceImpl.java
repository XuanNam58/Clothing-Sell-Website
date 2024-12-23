package com.example.clothing_sell_website.service.customer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.clothing_sell_website.entity.Cart;
import com.example.clothing_sell_website.repository.CartRepository;
import com.example.clothing_sell_website.service.customer.CartService;

@org.springframework.stereotype.Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepo;

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Override
    public void deleteCart(long cartId) {
        cartRepo.findById(cartId);
        cartRepo.deleteById(cartId);
    }

    @Override
    public Cart getCartByCP(String customerId, String productId) {
        return cartRepo.getCartByCP(customerId, productId);
    }

    @Override
    public Cart getCartById(long cartId) {
        return cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Id không tồn tại!"));
    }

    @Override
    public List<Cart> getCartByCus(String customerId) {
        return cartRepo.getCartByCus(customerId);
    }
}
