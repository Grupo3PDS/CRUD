package com.javeriana.zoo_fantastico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javeriana.zoo_fantastico.model.Creature;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;

@Service
public class CreatureService {

    private final CreatureRepository creatureRepository;

    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    public Creature createCreature(Creature creature) {
        return creatureRepository.save(creature);
    }

    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creature not found with id: " + id));
    }

    public Creature updateCreature(Long id, Creature updatedCreature) {
        Creature existingCreature = creatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creature not found with id: " + id));

        existingCreature.setName(updatedCreature.getName());
        existingCreature.setSpecies(updatedCreature.getSpecies());
        existingCreature.setSize(updatedCreature.getSize());
        existingCreature.setDangerLevel(updatedCreature.getDangerLevel());
        existingCreature.setHealthStatus(updatedCreature.getHealthStatus());
        existingCreature.setZone(updatedCreature.getZone());

        return creatureRepository.save(existingCreature);
    }

    public void deleteCreature(Long id) {
        if (!creatureRepository.existsById(id)) {
            throw new RuntimeException("Creature not found with id: " + id);
        }
        creatureRepository.deleteById(id);
    }
}
