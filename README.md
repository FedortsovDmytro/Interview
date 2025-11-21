# Inventory Reservation Service

## Architecture

- Spring Boot 3, Java 17+
- REST API for inventory reservations
- `InventoryItem` entity with manual versioning (`version` field)
- `DomainEvent` table for immutable domain events

## Concurrency & CAS

- Manual Compare-And-Swap (CAS) using SQL:
  ```sql
  UPDATE inventory_item
  SET available = ?, reserved = ?, version = ?
  WHERE sku = ? AND version = ?
