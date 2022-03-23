package com.project.apiproject.repository;

import com.project.apiproject.domain.Cart;
import com.project.apiproject.domain.LineItems;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class CartRepositoryInteTest {

        @Autowired
        CartRepository cartRepository;

        @BeforeEach
        void setup()
        {
            var cartinfo = new Cart("asd123","Sripal",1000.0,List.of(new LineItems("ToothPaste",2,500.0)));
            cartRepository.save(cartinfo).block();
        }

        @AfterEach
        void tearDown()
        {
            cartRepository.deleteAll().block();
        }

        @Test
        void findAll()
        {
            var cartinfoFLux = cartRepository.findAll();

            StepVerifier.create(cartinfoFLux).expectNextCount(1).verifyComplete();
        }

        @Test
        void addCart()
        {
            var cartInfo = new Cart(null,"Sam",1000.0,List.of(new LineItems("ToothBrush",2,500.0)));

            var CartInfo = cartRepository.save(cartInfo).log();

            StepVerifier.create(CartInfo).assertNext(CartInfos->{
                assertNotNull(CartInfos.getCart_id());
                assertEquals("Sam",CartInfos.getCustomername());

            });
        }

        @Test
        void updateCart()
        {
            var cartInfo=cartRepository.findById("asd123").block();
            cartInfo.setCustomername("Suraj");
            var CartInfos = cartRepository.save(cartInfo);


            StepVerifier.create(CartInfos).assertNext(cart -> {
                assertEquals("Suraj",cart.getCustomername());
            })
                    .verifyComplete();

        }

        @Test
        void deleteCart()
        {
            var cartInfo = cartRepository.deleteById("asd123");

            StepVerifier.create(cartInfo).
                    expectNextCount(0).verifyComplete();
        }

}