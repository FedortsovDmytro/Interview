package com.example.demo;

import com.example.demo.entity.InventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;
    @Autowired
    public InventoryController(InventoryService inventoryService, InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping("/{sku}/reserve")
    public ResponseEntity<?> reserve(@PathVariable String sku,
                                     @RequestBody ReserveRequest request) {
        try {
            inventoryService.reserve(sku, request.getQty());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @GetMapping("/{sku}")
    public InventoryItem getInventory(@PathVariable String sku) {
        return inventoryRepository.findById(sku)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public static class ReserveRequest {
        private int qty;

        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }
}
