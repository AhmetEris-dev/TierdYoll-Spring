package com.ajwalker.service;


import com.ajwalker.dto.request.AddProductToBasketRequestDto;
import com.ajwalker.entity.Basket;
import com.ajwalker.entity.BasketProduct;
import com.ajwalker.entity.Product;
import com.ajwalker.entity.User;
import com.ajwalker.exception.ErrorType;
import com.ajwalker.exception.TierdYolException;
import com.ajwalker.repository.BasketRepository;
import com.ajwalker.utility.enums.EBasketState;
import com.ajwalker.views.VwGetBasket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final UserService userService;

    

    public void addProductToBasket(AddProductToBasketRequestDto dto) {
        Optional<User> optionalUser = userService.findById(dto.userId());
        Optional<Product> optionalProduct = productService.findByProductId(dto.productId());

        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            User user = optionalUser.get();
            Product product = optionalProduct.get();

            Basket basket = basketRepository.findByUserAndBasketState(user.getId(), EBasketState.ACTIVE);

            if (basket == null) {
                basket = new Basket();
                basket.setUserId(dto.userId());
                basket.setBasketState(EBasketState.ACTIVE);
                basketRepository.save(basket);
            }
            BasketProduct basketProduct = new BasketProduct();
               basketProduct.setBasketId(basket.getId());
               basketProduct.setProductId(product.getId());
               basketProduct.setQuantity(dto.quantity());
               basketProduct.setUnitPrice(basketProduct.getUnitPrice());
               
               
             
            
        }
    }


    public Optional<VwGetBasket> getBasket(Long Id) {
        Optional<VwGetBasket> optionalBasket = basketRepository.findByBasketId(Id);
        if (optionalBasket.isEmpty()) {
            throw new TierdYolException(ErrorType.NOT_FOUND_BASKET);
        }
        return optionalBasket;
    }
}