package com.project.apiproject.service;

import com.project.apiproject.controller.CartController;
import com.project.apiproject.domain.Cart;
import com.project.apiproject.domain.LineItems;
import com.project.apiproject.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public Mono<Cart> updateTotal(Cart cart)
    {
        var carts=cart.getLine_items();
        double total =0.0;
        for (LineItems item:carts)
        {
            total+=item.getPrice()* item.getQuantity();
        }
        cart.setTotal_value(total);
        return cartRepository.save(cart);
    }
    public Mono<Cart> addItems(Cart cart) {
        cartRepository.save(cart);
        return updateTotal(cart);
    }

    public Flux<Cart> getAllItems() {
        return cartRepository.findAll();
    }

    public Mono<Cart> addCartItem(LineItems lineItems, String customer_name) {
            return cartRepository.findByCustomername(customer_name).flatMap(cart -> {
               cart.getLine_items().add(lineItems);
               cartRepository.save(cart);
               return updateTotal(cart);
            }).switchIfEmpty(Mono.empty());
    }

    public Mono<Cart> deleteCartItem(String cart_id, String product_name) {
        return cartRepository.findByCustomername(cart_id).flatMap(cart -> {
            var carts = cart.getLine_items();
            Boolean present=false;
            for (LineItems items:carts) {
                if (items.getProduct_name().equals(product_name)) {
                    cart.getLine_items().remove(items);
                    present = true;
                    break;
                }
            }
            if(!present)
                return Mono.empty();
            cartRepository.save(cart);
            return updateTotal(cart);
        });
  }

    public Mono<Void> deleteAll() {
       return cartRepository.deleteAll();
    }

    public Mono<Cart> updateProductQuantity(String cart_id, String product_name, int quantity) {
        return cartRepository.findByCustomername(cart_id).flatMap(cart -> {
         var carts =  cart.getLine_items();
         Boolean present = false;
         for (LineItems items:carts)
         {
             if (items.getProduct_name().equals(product_name))
             {
                 items.setQuantity(quantity);
                 present=true;
                 break;
             }
         }
         if(!present)
         {
             return Mono.empty();
         }
         cart.setLine_items(carts);
         cartRepository.save(cart);
         return updateTotal(cart);
        });
    }

    public Mono<Cart> deleteCart(String customername) {
        return cartRepository.deleteByCustomername(customername);

    }

    public Mono<Cart> getCartDetails(String cart_id) {
        return cartRepository.findByCustomername(cart_id);
    }

    public Mono<Double> getCartSum(String cart_id) {
        return cartRepository.findByCustomername(cart_id).map(cart -> {
            return cart.getTotal_value();
        });
}

    public Mono<Cart> addCartItem2(LineItems lineItems, String customername) {
        return cartRepository.findByCustomername(customername).flatMap(cart -> {
            cart.getLine_items().add(lineItems);
            cartRepository.save(cart);
            return updateTotal(cart);
        }).switchIfEmpty(createCart(lineItems,customername));
    }

    public Mono<Cart> createCart(LineItems lineItems, String customername) {
        var cart= new Cart(null,customername,0.0,List.of(lineItems));
        cartRepository.save(cart);
        return updateTotal(cart);
    }

    public Mono<Double> getComputeTotal(String customername) {
        return cartRepository.findByCustomername(customername).flatMap(cart -> {
            Double total=0.0;
            var carts=cart.getLine_items();
            for(LineItems items:carts)
            {
                total+= items.getQuantity()* items.getPrice();
            }
            return Mono.just(total);
        });
    }
}
