package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="InventoryItem")
public class InventoryItem {
    @Id
    @Column(name="sku")
    private String sku;
    @Column(name="available")
    private int available;
    @Column(name="reserved")
    private int reserved;
    @Column(name="version")
    private long version;

    protected InventoryItem() {}
    public InventoryItem(String sku, int available, int reserved, long version) {
        this.sku = sku;
        this.available = available;
        this.reserved = reserved;
        this.version = version;

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
//Stworzyć REST-owy serwis, który umożliwia rezerwację towaru w magazynie przy poprawnej obsłudze równoległych operacji oraz trwałym zapisie zdarzeń domenowych.
//
//Wymagania funkcjonalne:
//
//        1) Encja: InventoryItem
//   - sku (String)
//   - available (int)
//   - reserved (int)
//   - version (long) — zarządzane ręcznie, BEZ @Version.
//gotowe
//2) Operacja: reserve(sku, qty)
//   - qty > 0
//        - available >= qty
//   - jeśli operacja się powiedzie:
//available = available - qty
//        reserved = reserved + qty
//version = version + 1
//zapisać zdarzenie domenowe ItemReservedEvent
//
//3) Zdarzenia domenowe:
//Przechowujemy w osobnej tabeli jako niezmienne rekordy:
//        - id
//   - sku
//   - type
//   - payload (JSON)
//   - created_at
//Nie wolno używać Spring events / ApplicationEventPublisher.
//
//4) Konkurencyjność:
//Należy zaimplementować własny mechanizm compare-and-swap przy użyciu SQL:
//WHERE sku = :sku AND version = :version
//
//Zabronione jest używanie:
//        - @Version
//   - JPA optimistic locking
//   - SELECT FOR UPDATE
//   - transakcji obejmującej całą operację
//   - Redis/Hazelcast/distributed lock
//
//5) Jeśli wersja się nie zgadza — wykonać ponowną próbę.
//        Maksymalnie 3 próby.
//        Po 3 nieudanych próbach zwrócić 409 CONFLICT.
//
//6) Endpoint REST:
//POST /inventory/{sku}/reserve
//Body:
//        { "qty": 5 }