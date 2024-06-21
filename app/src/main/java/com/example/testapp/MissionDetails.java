package com.example.testapp;

public class MissionDetails {
    private String mission_id;
    private String mission_name;
    private String description;
    private String[] manufacturers;
    private String[] payload_ids;
    private String wikipedia;
    private String website;
    private String twitter;

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

    public String[] getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(String[] manufacturers) {
        this.manufacturers = manufacturers;
    }

    public String[] getPayloadIds() {
        return payload_ids;
    }

    public void setPayloadIds(String[] payload_ids) {
        this.payload_ids = payload_ids;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
