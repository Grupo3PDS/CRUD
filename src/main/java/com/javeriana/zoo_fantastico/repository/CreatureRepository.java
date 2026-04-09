package com.javeriana.zoo_fantastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javeriana.zoo_fantastico.model.Creature;

public interface CreatureRepository extends JpaRepository<Creature, Long> {
}