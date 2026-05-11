package com.javeriana.zoo_fantastico;

import com.javeriana.zoo_fantastico.model.Creature;
import com.javeriana.zoo_fantastico.model.Zone;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;
import com.javeriana.zoo_fantastico.service.CreatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CreatureServiceIntegrationTest {

    @Autowired
    private CreatureService creatureService;

    @Autowired
    private CreatureRepository creatureRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    private Zone testZone;

    @BeforeEach
    void setUp() {
        creatureRepository.deleteAll();
        zoneRepository.deleteAll();

        testZone = new Zone();
        testZone.setName("Zona Test");
        testZone.setDescription("Zona para pruebas");
        testZone.setCapacity(10);
        testZone = zoneRepository.save(testZone);
    }

    @Test
    void testCreateCreature_ShouldPersistInDatabase() {
        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setSpecies("Equino Magico");
        creature.setSize(1.5);
        creature.setDangerLevel(3);
        creature.setHealthStatus("healthy");
        creature.setZone(testZone);

        creatureService.createCreature(creature);

        Optional<Creature> found = creatureRepository.findById(creature.getId());
        assertTrue(found.isPresent());
        assertEquals("Unicornio", found.get().getName());
    }

    @Test
    void testGetCreatureById_ShouldReturnCorrectCreature() {
        Creature creature = new Creature();
        creature.setName("Dragon");
        creature.setSpecies("Reptil Alado");
        creature.setSize(5.0);
        creature.setDangerLevel(9);
        creature.setHealthStatus("healthy");
        creature.setZone(testZone);
        creatureService.createCreature(creature);

        Creature found = creatureService.getCreatureById(creature.getId());

        assertNotNull(found);
        assertEquals("Dragon", found.getName());
        assertEquals(9, found.getDangerLevel());
    }

    @Test
    void testUpdateCreature_ShouldReflectChangesInDatabase() {
        Creature creature = new Creature();
        creature.setName("Fenix");
        creature.setSpecies("Ave Mitica");
        creature.setSize(2.0);
        creature.setDangerLevel(7);
        creature.setHealthStatus("healthy");
        creature.setZone(testZone);
        creatureService.createCreature(creature);

        creature.setName("Fenix Dorado");
        creature.setDangerLevel(8);
        creatureService.updateCreature(creature.getId(), creature);

        Optional<Creature> updated = creatureRepository.findById(creature.getId());
        assertTrue(updated.isPresent());
        assertEquals("Fenix Dorado", updated.get().getName());
        assertEquals(8, updated.get().getDangerLevel());
    }

    @Test
    void testDeleteCreature_ShouldRemoveFromDatabase() {
        Creature creature = new Creature();
        creature.setName("Grifo");
        creature.setSpecies("Hibrido Alado");
        creature.setSize(3.0);
        creature.setDangerLevel(6);
        creature.setHealthStatus("healthy");
        creature.setZone(testZone);
        creatureService.createCreature(creature);
        Long id = creature.getId();

        creatureService.deleteCreature(id);

        Optional<Creature> deleted = creatureRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testDeleteCreature_WhenCritical_ShouldThrowException() {
        Creature creature = new Creature();
        creature.setName("Basilisco");
        creature.setSpecies("Serpiente Mitica");
        creature.setSize(4.0);
        creature.setDangerLevel(10);
        creature.setHealthStatus("critical");
        creature.setZone(testZone);
        creatureService.createCreature(creature);

        assertThrows(Exception.class, () -> {
            creatureService.deleteCreature(creature.getId());
        });

        Optional<Creature> stillThere = creatureRepository.findById(creature.getId());
        assertTrue(stillThere.isPresent());
    }
}