package com.javeriana.zoo_fantastico.dto;

public class ZoneResponse {

    private Long id;
    private String name;
    private String description;
    private int capacity;
    private int creatureCount;

    public ZoneResponse(Long id, String name, String description, int capacity, int creatureCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.creatureCount = creatureCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCreatureCount() {
        return creatureCount;
    }
}
