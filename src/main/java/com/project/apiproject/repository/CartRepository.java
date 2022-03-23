package com.project.apiproject.repository;

import com.project.apiproject.domain.Cart;
import com.project.apiproject.domain.LineItems;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CartRepository extends ReactiveMongoRepository<Cart,String> {
    Mono<Cart> findByCustomername(String customer_name);
    Mono<Cart> deleteByCustomername(String customer_name);
}
