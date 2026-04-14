package com.javeriana.zoo_fantastico.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javeriana.zoo_fantastico.dto.ZoneResponse;
import com.javeriana.zoo_fantastico.exception.BadRequestException;
import com.javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import com.javeriana.zoo_fantastico.model.Zone;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;

@Service
@Transactional
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone) {
        validateZone(zone);
        return zoneRepository.save(zone);
    }

    @Transactional(readOnly = true)
    public List<ZoneResponse> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ZoneResponse getZoneResponseById(Long id) {
        return toResponse(getZoneById(id));
    }

    @Transactional(readOnly = true)
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));
    }

    public Zone updateZone(Long id, Zone updatedZone) {
        Zone existing = getZoneById(id);
        validateZone(updatedZone);
        existing.setName(updatedZone.getName());
        existing.setDescription(updatedZone.getDescription());
        existing.setCapacity(updatedZone.getCapacity());
        return zoneRepository.save(existing);
    }

    public void deleteZone(Long id) {
        Zone existing = getZoneById(id);
        if (existing.getCreatures() != null && !existing.getCreatures().isEmpty()) {
            throw new BadRequestException("Cannot delete zone with assigned creatures");
        }
        zoneRepository.delete(existing);
    }

    private void validateZone(Zone zone) {
        if (zone == null) {
            throw new BadRequestException("Zone data is required.");
        }
        if (zone.getName() == null || zone.getName().trim().isEmpty()) {
            throw new BadRequestException("Zone name is required.");
        }
        if (zone.getCapacity() < 0) {
            throw new BadRequestException("Zone capacity must be 0 or greater.");
        }
    }

    private ZoneResponse toResponse(Zone zone) {
        int creatureCount = zone.getCreatures() == null ? 0 : zone.getCreatures().size();
        return new ZoneResponse(zone.getId(), zone.getName(), zone.getDescription(), zone.getCapacity(), creatureCount);
    }
}
