package com.example.tourismapp;

public class Bookmark {
    public String userId;
    public String pageId;
    public String name;
    public String details;
    public String imageUrl;
    public String wikiUrl;

    // Default constructor required for Firebase
    public Bookmark() {
        // Firebase requires a default constructor
    }

    // Constructor
    public Bookmark(String pageId, String name, String details, String imageUrl, String wikiUrl,String userId) {
        this.userId = userId;
        this.pageId = pageId;
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.wikiUrl = wikiUrl;
    }



    // Getter and Setter methods
    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }
}
