package com.example.testapp;

public class Launch {
    private String flight_number; // Ensure this matches the JSON key
    private String mission_name;
    private String launch_date_utc;

    public String getFlightNumber() {
        return flight_number;
    }

    public void setFlightNumber(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getMissionName() {
        return mission_name;
    }

    public void setMissionName(String mission_name) {
        this.mission_name = mission_name;
    }

    public String getLaunchDate() {
        return launch_date_utc;
    }

    public void setLaunchDate(String launch_date_utc) {
        this.launch_date_utc = launch_date_utc;
    }
}
