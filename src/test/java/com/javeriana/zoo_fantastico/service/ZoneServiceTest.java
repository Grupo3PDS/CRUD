package com.javeriana.zoo_fantastico.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
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
import com.javeriana.zoo_fantastico.repository.ZoneRepository;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    private Zone zone;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId(1L);
        zone.setName("Bosque Mágico");
        zone.setDescription("Un lugar lleno de magia");
        zone.setCapacity(10);
    }

    @Test
    void testCreateZone_ShouldReturnSavedZone() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateZone_ShouldReturnSavedZone");
        System.out.println("Datos: Nombre=" + zone.getName() + ", Capacidad=" + zone.getCapacity());
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        Zone savedZone = zoneService.createZone(zone);

        assertNotNull(savedZone);
        assertEquals("Bosque Mágico", savedZone.getName());
        System.out.println("Estado: EXITOSO. Zona creada: " + savedZone.getName());
    }

    @Test
    void testCreateZone_WithEmptyName_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateZone_WithEmptyName_ShouldThrowException");
        zone.setName("");
        System.out.println("Datos: Nombre='" + zone.getName() + "'");

        Exception exception = assertThrows(BadRequestException.class, () -> {
            zoneService.createZone(zone);
        });

        assertEquals("Zone name is required.", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }

    @Test
    void testCreateZone_WithNegativeCapacity_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testCreateZone_WithNegativeCapacity_ShouldThrowException");
        zone.setCapacity(-5);
        System.out.println("Datos: Capacidad=" + zone.getCapacity());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            zoneService.createZone(zone);
        });

        assertEquals("Zone capacity must be 0 or greater.", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }

    @Test
    void testGetZoneById_ShouldReturnZone() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testGetZoneById_ShouldReturnZone");
        System.out.println("Datos: ID=" + zone.getId());
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        Zone found = zoneService.getZoneById(1L);

        assertNotNull(found);
        assertEquals("Bosque Mágico", found.getName());
        System.out.println("Estado: EXITOSO. Zona encontrada: " + found.getName());
    }

    @Test
    void testUpdateZone_ShouldReturnUpdatedZone() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testUpdateZone_ShouldReturnUpdatedZone");
        Zone updatedInfo = new Zone();
        updatedInfo.setName("Bosque Encantado");
        updatedInfo.setDescription("Nuevo bosque");
        updatedInfo.setCapacity(20);
        System.out.println("Datos actualizados: Nuevo Nombre=" + updatedInfo.getName() + ", Nueva Capacidad=" + updatedInfo.getCapacity());

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        Zone result = zoneService.updateZone(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("Bosque Encantado", result.getName());
        assertEquals(20, result.getCapacity());
        System.out.println("Estado: EXITOSO. Zona actualizada: " + result.getName());
    }

    @Test
    void testDeleteZone_ShouldDeleteSuccessfully() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testDeleteZone_ShouldDeleteSuccessfully");
        System.out.println("Datos: ID=" + zone.getId() + ", Criaturas asignadas=0");
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        zoneService.deleteZone(1L);

        verify(zoneRepository, times(1)).delete(zone);
        System.out.println("Estado: EXITOSO. Zona eliminada.");
    }

    @Test
    void testDeleteZone_WithAssignedCreatures_ShouldThrowException() {
        System.out.println("--------------------------------------------------");
        System.out.println("Ejecutando testDeleteZone_WithAssignedCreatures_ShouldThrowException");
        List<Creature> creatures = new ArrayList<>();
        creatures.add(new Creature());
        zone.setCreatures(creatures);
        System.out.println("Datos: ID=" + zone.getId() + ", Criaturas asignadas=" + zone.getCreatures().size());
        
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            zoneService.deleteZone(1L);
        });

        assertEquals("Cannot delete zone with assigned creatures", exception.getMessage());
        System.out.println("Estado: EXITOSO. Excepción lanzada: " + exception.getMessage());
    }
}
