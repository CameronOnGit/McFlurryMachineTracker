package com.mcflurryfinder.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mcflurryfinder.backend.model.StatusReport;

public interface StatusReportRepository extends JpaRepository<StatusReport, Long> {

    List<StatusReport> findByMachineIdOrderByReportedAtDesc(Long machineId);
}
