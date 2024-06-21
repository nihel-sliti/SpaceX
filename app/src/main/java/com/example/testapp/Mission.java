package com.example.testapp;

public class Mission {
    private String mission_id;
    private String mission_name;
    private String description;

    // Getters and Setters

    public String getMissionId() {
        return mission_id;
    }

    public void setMissionId(String mission_id) {
        this.mission_id = mission_id;
    }

    public String getMissionName() {
        return mission_name;
    }

    public void setMissionName(String mission_name) {
        this.mission_name = mission_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
