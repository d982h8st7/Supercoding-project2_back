package com.example.project02.service;

import com.example.project02.entity.Cart;
import com.example.project02.entity.CartProduct;
import com.example.project02.entity.Product;
import com.example.project02.entity.User;
import com.example.project02.exception.OutOfStockException;
import com.example.project02.model.CartRequest;
import com.example.project02.repository.CartProductRepository;
import com.example.project02.repository.CartRepository;
import com.example.project02.repository.ProductRepository;
import com.example.project02.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void addProduct(Long userId, CartRequest request) {

        Cart cart = this.cartRepository.findByUserId(userId);

        User user = userRepository.findById(userId).orElseThrow(() ->
            new RuntimeException("가입되지 않은 정보입니다."));

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
            new RuntimeException("등록되지 않은 제품입니다."));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new OutOfStockException("재고 부족");
        } else {
            if (cart == null) {
                cart = Cart.createCart(user);
                cartRepository.save(cart);
            }

            CartProduct cartProduct = this.cartProductRepository.findByCartIdAndProductId(cart.getId(), product.getId());
            if (cartProduct == null) {
                cartProduct = CartProduct.createCartProduct(cart, product, request.getQuantity());
                this.cartProductRepository.save(cartProduct);
            } else {
                cartProduct.setCart(cartProduct.getCart());
                cartProduct.setProduct(cartProduct.getProduct());
                cartProduct.addCount(request.getQuantity());
                cartProduct.setCount(cartProduct.getCount());
                this.cartProductRepository.save(cartProduct);
            }

            cart.setCount(cart.getCount() + request.getQuantity());
            cartProduct.setPrice(product);

            Integer count = this.cartProductRepository.findCount(user.getId(), product.getId());

            if (count < request.getQuantity()) {
                throw new OutOfStockException("재고 부족");
            }
        }
    }


}
