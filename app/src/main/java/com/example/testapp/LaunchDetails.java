package com.example.testapp;

public class LaunchDetails {
    private String mission_name;
    private String launch_date_utc;
    private String details;
    private Links links;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public static class Links {
        private String article_link;
        private String wikipedia;

        public String getArticleLink() {
            return article_link;
        }

        public void setArticleLink(String article_link) {
            this.article_link = article_link;
        }

        public String getWikipedia() {
            return wikipedia;
        }

        public void setWikipedia(String wikipedia) {
            this.wikipedia = wikipedia;
        }
    }
}
