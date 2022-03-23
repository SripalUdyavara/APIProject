package com.project.apiproject.controller;


import com.project.apiproject.domain.Cart;
import com.project.apiproject.domain.LineItems;
import com.project.apiproject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;
@Validated
@RestController
@RequestMapping("/Cart")
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/addItem")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cart> addItems(@RequestBody @Valid Cart cart)
    {
        return cartService.addItems(cart);
    }

    @GetMapping("/getAllItems")
    public Flux<Cart> getAllItems()
    {
        return cartService.getAllItems();

    }

    @GetMapping("/getCartDetails/{customername}")
    public Mono<ResponseEntity<Cart>> getCartDetails(@PathVariable String customername)
    {
        return cartService.getCartDetails(customername)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.error(new RuntimeException("No cart found with customer name "+customername)));
    }

    @GetMapping("/getCartSum/{customername}")
    public Mono<Double> getCartSum(@PathVariable String customername)
    {
        return cartService.getCartSum(customername);

    }

    @GetMapping("/computeTotal/{customername}")
    public Mono<Double> getComputeTotal(@PathVariable String customername)
    {
        return cartService.getComputeTotal(customername);
    }


//    @PostMapping("/addCartItem/{customername}")
//    public Mono<ResponseEntity<Cart>> addCartItem(@RequestBody @Valid LineItems lineItems,@PathVariable String customername)
//    {
//        return cartService.addCartItem(lineItems,customername)
//                .map(ResponseEntity.ok()::body)
//                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
//    }

    @PostMapping("/addCartItem2/{customername}")
    public Mono<ResponseEntity<Cart>> addCartItem2(@RequestBody @Valid LineItems lineItems,@PathVariable String customername)
    {
        return cartService.addCartItem2(lineItems,customername)
                .map(ResponseEntity.ok()::body);
    }

    @DeleteMapping("/deleteCartProduct/{customername}/{product_name}")
    public Mono<ResponseEntity<Cart>> deleteCartItem(@PathVariable String customername,@PathVariable String product_name)
    {
        return cartService.deleteCartItem(customername,product_name)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.error(new RuntimeException("Item to be deleted could not be found")));
    }

    @PutMapping("/updateQuantity/{customername}/{product_name}/{quantity}")
    public Mono<ResponseEntity<Cart>> updateProductQuantity(@PathVariable String customername, @PathVariable String product_name, @PathVariable int quantity)
    {
        return cartService.updateProductQuantity(customername,product_name,quantity)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.error(new RuntimeException("Product to be updated could not be found")));
    }

    @DeleteMapping("/deleteAll")
    public Mono<Void> deleteAll()
    {
        return cartService.deleteAll();
    }

    @DeleteMapping("/deleteCart/{customername}")
    public Mono<Cart> deleteCart(@PathVariable String customername)
    {
        return cartService.deleteCart(customername)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart to be deleted could not be found")));
    }


}
