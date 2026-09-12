package com.mcflurryfinder.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mcflurryfinder.backend.model.Machine;
import com.mcflurryfinder.backend.model.MachineStatus;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    List<Machine> findByStatus(MachineStatus status);

    List<Machine> findByRestaurantId(Long restaurantId);

}
