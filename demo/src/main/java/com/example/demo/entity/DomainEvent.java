package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "domain_event")
public class DomainEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public DomainEvent() {
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getType() { return type; }
    public String getPayload() { return payload; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setSku(String sku) { this.sku = sku; }
    public void setType(String type) { this.type = type; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}