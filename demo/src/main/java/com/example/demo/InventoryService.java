package com.example.demo;

import com.example.demo.entity.DomainEvent;
import com.example.demo.entity.InventoryItem;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    private  EntityManager em;
    private  InventoryRepository inventoryRepository;
    private  DomainEventRepository eventRepository;

    @Autowired
    public InventoryService(EntityManager em,
                            InventoryRepository inventoryRepository,
                            DomainEventRepository eventRepository) {
        this.em = em;
        this.inventoryRepository = inventoryRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void reserve(String sku, int qty) {

        for (int i=0;i<3;i++){
            InventoryItem item = inventoryRepository.findById(sku)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

            if(item.getAvailable()<qty){
               throw new IllegalStateException("Not enough available");
            }

            int newAvailable = item.getAvailable() - qty;
            int newReserved = item.getReserved() + qty;
            long newVersion = item.getVersion() + 1;

            int updated = em.createQuery(
                            "UPDATE InventoryItem i " +
                                    "SET i.available = :a, " +
                                    "    i.reserved  = :r, " +
                                    "    i.version   = :v " +
                                    "WHERE i.sku = :sku " +
                                    "  AND i.version = :oldVersion"
                    )
                    .setParameter("a", newAvailable)
                    .setParameter("r", newReserved)
                    .setParameter("v", newVersion)
                    .setParameter("sku", sku)
                    .setParameter("oldVersion", item.getVersion())
                    .executeUpdate();

            if (updated > 0) {
                saveEvent(sku, qty);
                return;
            }

        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not reserve item after 3 attempts");

    }

    private void saveEvent(String sku, int qty) {
        String payload = "{\"qty\":" + qty + "}";

        DomainEvent event = new DomainEvent();
        event.setSku(sku);
        event.setType("ItemReservedEvent");
        event.setPayload(payload);
        event.setCreatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }
}
