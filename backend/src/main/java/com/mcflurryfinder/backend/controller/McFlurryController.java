package com.mcflurryfinder.backend.controller;

import com.mcflurryfinder.backend.model.Restaurant;
import com.mcflurryfinder.backend.model.StatusReport;
import com.mcflurryfinder.backend.repository.RestaurantRepository;
import com.mcflurryfinder.backend.repository.StatusReportRepository;
import com.mcflurryfinder.backend.repository.MachineRepository;
import com.mcflurryfinder.backend.model.Machine;
import com.mcflurryfinder.backend.model.MachineStatus;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class McFlurryController {

    private final RestaurantRepository restaurants;
    private final MachineRepository machines;
    private final StatusReportRepository reports;

    public McFlurryController(RestaurantRepository restaurants, MachineRepository machines,
            StatusReportRepository reports) {
        this.restaurants = restaurants;
        this.machines = machines;
        this.reports = reports;
    }

    // =========================
    // RESTAURANTS
    // =========================
    @GetMapping("/api/restaurants")
    public List<Restaurant> restaurants() {
        return restaurants.findAll();
    }

    @GetMapping("/api/restaurants/{id}")
    public ResponseEntity<Restaurant> restaurant(
            @PathVariable Long id) {
        return restaurants.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/restaurants")
    public Restaurant addRestaurant(
            @RequestBody Restaurant restaurant) {
        return restaurants.save(restaurant);
    }

    // =========================
    // MACHINES
    // =========================

    @GetMapping("/api/machines")
    public List<Machine> machines() {
        return machines.findAll();
    }

    @GetMapping("/api/machines/{id}")
    public ResponseEntity<Machine> machine(
            @PathVariable Long id) {
        return machines.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/machines/working")
    public List<Machine> working() {
        return machines.findByStatus(MachineStatus.WORKING);
    }

    @GetMapping("/api/machines/broken")
    public List<Machine> broken() {
        return machines.findByStatus(MachineStatus.BROKEN);
    }

    @GetMapping("/api/restaurants/{id}/machines")
    public List<Machine> restaurantMachines(
            @PathVariable Long id) {
        return machines.findByRestaurantId(id);
    }

    @PostMapping("/api/machines")
    public Machine addMachine(
            @RequestBody Machine machine) {
        machine.setStatus(MachineStatus.UNKNOWN);
        machine.setLastStatusUpdate(Instant.now());

        return machines.save(machine);
    }

    // =========================
    // UPDATE MACHINE STATUS
    // =========================

    @PutMapping("/api/machines/{id}/status")
    public ResponseEntity<Machine> updateStatus(
            @PathVariable Long id,
            @RequestParam MachineStatus status,
            @RequestParam(defaultValue = "USER") String source) {
        return machines.findById(id)
                .map(machine -> {
                    machine.setStatus(status);
                    machine.setLastStatusUpdate(Instant.now());

                    Machine saved = machines.save(machine);

                    reports.save(
                            new StatusReport(
                                    saved,
                                    status,
                                    source));
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // STATUS HISTORY
    // =========================

    @GetMapping("/api/machines/{id}/history")
    public List<StatusReport> history(
            @PathVariable Long id) {
        return reports.findByMachineIdOrderByReportedAtDesc(id);
    }
}
