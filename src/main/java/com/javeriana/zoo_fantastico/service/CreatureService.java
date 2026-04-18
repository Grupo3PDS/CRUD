package com.javeriana.zoo_fantastico.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javeriana.zoo_fantastico.exception.BadRequestException;
import com.javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import com.javeriana.zoo_fantastico.model.Creature;
import com.javeriana.zoo_fantastico.model.Zone;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;

@Service
@Transactional
public class CreatureService {

    private final CreatureRepository creatureRepository;
    private final ZoneRepository zoneRepository;

    public CreatureService(CreatureRepository creatureRepository, ZoneRepository zoneRepository) {
        this.creatureRepository = creatureRepository;
        this.zoneRepository = zoneRepository;
    }

    public Creature createCreature(Creature creature) {
        validateCreature(creature);
        creature.setZone(resolveZone(creature));
        return creatureRepository.save(creature);
    }

    @Transactional(readOnly = true)
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Creature not found with id: " + id));
    }

    public Creature updateCreature(Long id, Creature updatedCreature) {
        validateCreature(updatedCreature);
        Creature existingCreature = getCreatureById(id);

        existingCreature.setName(updatedCreature.getName());
        existingCreature.setSpecies(updatedCreature.getSpecies());
        existingCreature.setSize(updatedCreature.getSize());
        existingCreature.setDangerLevel(updatedCreature.getDangerLevel());
        existingCreature.setHealthStatus(updatedCreature.getHealthStatus());
        existingCreature.setZone(resolveZone(updatedCreature));

        return creatureRepository.save(existingCreature);
    }

    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (creature.getHealthStatus() != null && creature.getHealthStatus().equalsIgnoreCase("critical")) {
            throw new BadRequestException("Cannot delete creatures in critical health");
        }
        creatureRepository.delete(creature);
    }

    private void validateCreature(Creature creature) {
        if (creature == null) {
            throw new BadRequestException("Creature data is required.");
        }
        if (creature.getSize() < 0) {
            throw new BadRequestException("Creature size cannot be negative.");
        }
        if (creature.getDangerLevel() < 1 || creature.getDangerLevel() > 10) {
            throw new BadRequestException("Creature dangerLevel must be between 1 and 10.");
        }
        if (creature.getZone() == null || creature.getZone().getId() == null) {
            throw new BadRequestException("Creature must be assigned to an existing zone.");
        }
    }

    private Zone resolveZone(Creature creature) {
        Long zoneId = creature.getZone().getId();
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new BadRequestException("Zone not found with id: " + zoneId));
    }
}
