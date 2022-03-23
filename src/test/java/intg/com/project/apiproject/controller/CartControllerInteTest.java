package com.project.apiproject.controller;

import com.project.apiproject.domain.Cart;
import com.project.apiproject.domain.LineItems;
import com.project.apiproject.repository.CartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class CartControllerInteTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var cartinfo = List.of(new Cart("asd123","Sripal",2000.0,
                List.of(new LineItems("ToothPaste",2,500.0),new LineItems("ToothBrush",2,100.0))),new Cart("sdf123","Samuel",2000.0,
                List.of(new LineItems("ToothBrush",5,200.0))));
       cartRepository.saveAll(cartinfo).blockLast();
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll().block();
    }

    @Test
    void addItems() {

        var cartInfo = new Cart(null,"Paul",1000.0,
                List.of(new LineItems("ToothPaste",2,500.0)));

        webTestClient.post().uri("/Cart/addItem")
                .bodyValue(cartInfo).exchange()
                .expectStatus().isCreated().expectBody(Cart.class)
                .consumeWith(cartEntityExchangeResult -> {
                    var data = cartEntityExchangeResult.getResponseBody();
                    assert data!=null;
                    assert data.getCart_id()!=null;
                });
    }

    @Test
    void getAllItems() {

        webTestClient.get().uri("/Cart/getAllItems").exchange()
                .expectStatus()
                .is2xxSuccessful().expectBodyList(Cart.class).hasSize(2);
    }


    @Test
    void deleteCartItem() {

    }

    @Test
    void addCartItem2() {
        var product = new LineItems("Shampoo",5,100.0);

        var customername="Sripal";
        webTestClient.
                post().uri("/Cart/addCartItem2/{customername}",customername)
                .bodyValue(product).exchange()
                .expectBody(Cart.class).consumeWith(cartEntityExchangeResult -> {
                    var responsebody = cartEntityExchangeResult.getResponseBody();
                    assert responsebody.getCart_id()!=null;
                    assertEquals(responsebody.getCustomername(),"Sripal");
                });
    }

    @Test
    void DeleteCartItem() {
        var product = "ToothPaste";
        var name="Sripal";

        webTestClient.delete().uri("/Cart/deleteCartProduct/{customername}/{product_name}",name,product)
                .exchange().expectBody(Cart.class).consumeWith(cartEntityExchangeResult -> {
                    var responsebody = cartEntityExchangeResult.getResponseBody();
                   var items = responsebody.getLine_items();
                   assert items.size()==1;
                });
    }

    @Test
    void updateProductQuantity() {
        var product = "ToothPaste";
        var name="Sripal";
        var quantity =5;

        webTestClient.put()
                .uri("/Cart/updateQuantity/{customername}/{product_name}/{quantity}",name,product,quantity)
                .exchange().expectBody(Cart.class).consumeWith(cartEntityExchangeResult -> {
                   var responsebody = cartEntityExchangeResult.getResponseBody();
                   var items = responsebody.getLine_items();
                   for(LineItems item:items)
                   {
                       if(item.getProduct_name().equals(product))
                       {
                           assertEquals(item.getQuantity(),quantity);
                       }
                   }
                });
    }

    @Test
    void deleteCart() {
        var name="Sripal";

        webTestClient.delete()
                .uri("/Cart/deleteCart/{customername}",name)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(Cart.class)
                .consumeWith(cartEntityExchangeResult ->{
                   var responsebody = cartEntityExchangeResult.getResponseBody();
                   assertEquals(responsebody.getCustomername(),name);
                });
    }

    @Test
    void getCartDetails() {
        var name="Sripal";

        webTestClient.get().uri("/Cart/getCartDetails/{customername}",name)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(Cart.class).consumeWith(cartEntityExchangeResult -> {
                   var responsebody = cartEntityExchangeResult.getResponseBody();
                   assertEquals(responsebody.getCustomername(),name);
                });
    }

    @Test
    void getComputeTotal() {
        var name= "Sripal";

        webTestClient.get().uri("/Cart/getCartSum/{customername}",name)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(Double.class).equals(2000.0);
    }
}