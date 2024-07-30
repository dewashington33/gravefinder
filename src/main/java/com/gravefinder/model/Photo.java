package com.gravefinder.model;

public class Photo {
    private int photoId;
    private String path;
    private String caption;
    private String dateCreated;
    private String type;

    public Photo(int photoId, String path, String caption, String dateCreated, String type) {
        this.setPhotoId(photoId);
        this.setPath(path);
        this.setCaption(caption);
        this.setDateCreated(dateCreated);
        this.setType(type);
    }

    public Photo() {

    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Photo [dateCreated=" + dateCreated + ", path=" + path + ", photoId=" + photoId + ", type=" + type + "]";
    }
}
