package com.gravefinder.model;

import java.util.ArrayList;
import java.util.List;

public class Memorial {
    private int memorialId;
    private int cemeteryId;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String deathDate;
    private String defaultLinkToShare;
    private String defaultPhotoToShare;
    private List<Photo> photos;

    public Memorial(int memorialId, int cemeteryId, String firstName, String lastName, String birthDate,
            String deathDate, String defaultLinkToShare, String defaultPhotoToShare) {
        this.setMemorialId(memorialId);
        this.setCemeteryId(cemeteryId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthDate(birthDate);
        this.setDeathDate(deathDate);
        this.setDefaultLinkToShare(defaultLinkToShare);
        this.setDefaultPhotoToShare(defaultPhotoToShare);

        this.photos = new ArrayList<>();
    }

    // public Memorial() {

    // }

    public int getMemorialId() {
        return memorialId;
    }

    public void setMemorialId(int memorialId) {
        this.memorialId = memorialId;
    }

    public int getCemeteryId() {
        return cemeteryId;
    }

    public void setCemeteryId(int cemeteryId) {
        this.cemeteryId = cemeteryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getDefaultLinkToShare() {
        return defaultLinkToShare;
    }

    public void setDefaultLinkToShare(String defaultLinkToShare) {
        this.defaultLinkToShare = defaultLinkToShare;
    }

    public String getDefaultPhotoToShare() {
        return defaultPhotoToShare;
    }

    public void setDefaultPhotoToShare(String defaultPhotoToShare) {
        this.defaultPhotoToShare = defaultPhotoToShare;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    @Override
    public String toString() {
        return "Memorial" + '\n' +
                "memorialId=" + memorialId + '\n' +
                "firstName='" + firstName + "'" + '\n' +
                "lastName='" + lastName + "'" + '\n' +
                "birthDate=" + birthDate + "'" + '\n' +
                "deathDate=" + deathDate + "'" + '\n' +
                "defaultLinkToShare='" + defaultLinkToShare + "'" + '\n' +
                "defaultPhotoToShare='" + defaultPhotoToShare + "'" + '\n' +
                "photos=" + photos + "'" + '\n' +
                '}';
    }
}
