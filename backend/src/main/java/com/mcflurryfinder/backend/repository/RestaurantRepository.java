package com.mcflurryfinder.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mcflurryfinder.backend.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
