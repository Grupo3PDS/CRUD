package com.javeriana.zoo_fantastico.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javeriana.zoo_fantastico.exception.BadRequestException;
import com.javeriana.zoo_fantastico.model.Creature;
import com.javeriana.zoo_fantastico.model.Zone;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;

@ExtendWith(MockitoExtension.class)
class CreatureServiceTest {

    @Mock
    private CreatureRepository creatureRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private CreatureService creatureService;

    private Creature creature;
    private Zone zone;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId(1L);
        zone.setName("Zona Fuego");

        creature = new Creature();
        creature.setId(1L);
        creature.setName("Fénix");
        creature.setSize(5.0);
        creature.setDangerLevel(5);
        creature.setHealthStatus("healthy");
        creature.setZone(zone);
    }

    @Test
    void testCreateCreature_ShouldReturnSavedCreature() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateCreature_ShouldReturnSavedCreature");
        System.out.println("Datos: Nombre=" + creature.getName() + ", Tamaño=" + creature.getSize() + ", Peligro=" + creature.getDangerLevel() + ", ZonaID=" + creature.getZone().getId());
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature savedCreature = creatureService.createCreature(creature);

        assertNotNull(savedCreature);
        assertEquals("Fénix", savedCreature.getName());
        System.out.println("Estado: EXITOSO. Criatura creada: " + savedCreature.getName());
    }

    @Test
    void testCreateCreature_WithNegativeSize_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateCreature_WithNegativeSize_ShouldThrowException");
        creature.setSize(-1.0);
        System.out.println("Datos: Tamaño=" + creature.getSize());
        
        Exception exception = assertThrows(BadRequestException.class, () -> {
            creatureService.createCreature(creature);
        });

        assertEquals("Creature size cannot be negative.", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }

    @Test
    void testCreateCreature_WithInvalidDangerLevel_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateCreature_WithInvalidDangerLevel_ShouldThrowException");
        creature.setDangerLevel(11);
        System.out.println("Datos: NivelPeligro=" + creature.getDangerLevel());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            creatureService.createCreature(creature);
        });

        assertEquals("Creature dangerLevel must be between 1 and 10.", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }

    @Test
    void testGetCreatureById_ShouldReturnCreature() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testGetCreatureById_ShouldReturnCreature");
        System.out.println("Datos: ID=" + creature.getId());
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        Creature found = creatureService.getCreatureById(1L);

        assertNotNull(found);
        assertEquals("Fénix", found.getName());
        System.out.println("Estado: EXITOSO. Criatura encontrada: " + found.getName());
    }

    @Test
    void testUpdateCreature_ShouldReturnUpdatedCreature() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testUpdateCreature_ShouldReturnUpdatedCreature");
        Creature updatedInfo = new Creature();
        updatedInfo.setName("Fénix Renacido");
        updatedInfo.setSize(6.0);
        updatedInfo.setDangerLevel(6);
        updatedInfo.setZone(zone);
        System.out.println("Datos actualizados: Nuevo Nombre=" + updatedInfo.getName());

        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature result = creatureService.updateCreature(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("Fénix Renacido", result.getName());
        System.out.println("Estado: EXITOSO. Criatura actualizada: " + result.getName());
    }

    @Test
    void testDeleteCreature_ShouldDeleteSuccessfully() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testDeleteCreature_ShouldDeleteSuccessfully");
        System.out.println("Datos: ID=" + creature.getId() + ", Estado de salud=" + creature.getHealthStatus());
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        creatureService.deleteCreature(1L);

        verify(creatureRepository, times(1)).delete(creature);
        System.out.println("Estado: EXITOSO. Criatura eliminada.");
    }

    @Test
    void testDeleteCreature_InCriticalStatus_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testDeleteCreature_InCriticalStatus_ShouldThrowException");
        creature.setHealthStatus("critical");
        System.out.println("Datos: ID=" + creature.getId() + ", Estado de salud=" + creature.getHealthStatus());
        when(creatureRepository.findById(1L)).thenReturn(Optional.of(creature));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            creatureService.deleteCreature(1L);
        });

        assertEquals("Cannot delete creatures in critical health", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }
}
