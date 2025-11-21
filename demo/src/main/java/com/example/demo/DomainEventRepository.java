package com.example.demo;

import com.example.demo.entity.DomainEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainEventRepository extends JpaRepository<DomainEvent,Long> {

}
