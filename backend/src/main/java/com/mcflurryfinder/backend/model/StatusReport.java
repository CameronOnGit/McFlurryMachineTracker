package com.mcflurryfinder.backend.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "status_reports")
public class StatusReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    private Instant reportedAt = Instant.now();

    private String source;

    public StatusReport() {
    }

    public StatusReport(Machine machine, MachineStatus status, String source) {
        this.machine = machine;
        this.status = status;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public Machine getMachine() {
        return machine;
    }

    public MachineStatus getStatus() {
        return status;
    }

    public Instant getReportedAt() {
        return reportedAt;
    }

    public String getSource() {
        return source;
    }
}
