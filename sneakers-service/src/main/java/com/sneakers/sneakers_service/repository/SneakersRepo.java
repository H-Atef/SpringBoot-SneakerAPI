package com.sneakers.sneakers_service.repository;

import com.sneakers.sneakers_service.model.Sneakers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SneakersRepo extends MongoRepository<Sneakers,String> {

    Optional<Sneakers> findByTitle(String model);
    Optional<List<Sneakers>>findByCategoryIn(String category);
}
