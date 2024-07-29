package com.gravefinder.model;

import java.util.ArrayList;
import java.util.List;

public class Memorial {
    private int memorialId;
    private int cemeteryId;
    private String firstName;
    private String lastName;
    private int birthYear;
    private int deathYear;
    private int deathMonth;
    private int deathDay;
    private String defaultLinkToShare;
    private String defaultPhotoToShare;
    private List<Photo> photos;

    public Memorial(int memorialId, int cemeteryId, String firstName, String lastName, int birthYear,
            int deathYear,
            int deathMonth, int deathDay, String defaultLinkToShare, String defaultPhotoToShare) {
        this.setMemorialId(memorialId);
        this.setCemeteryId(deathDay);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthYear(birthYear);
        this.setDeathYear(deathYear);
        this.setDeathMonth(deathMonth);
        this.setDeathDay(deathDay);
        this.setDefaultLinkToShare(defaultLinkToShare);
        this.setDefaultPhotoToShare(defaultPhotoToShare);

        this.photos = new ArrayList<>();
    }

    public Memorial() {

    }

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

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }

    public int getDeathMonth() {
        return deathMonth;
    }

    public void setDeathMonth(int deathMonth) {
        this.deathMonth = deathMonth;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
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
                "birthYear=" + birthYear + "'" + '\n' +
                "deathYear=" + deathYear + "'" + '\n' +
                "deathMonth=" + deathMonth + "'" + '\n' +
                "deathDay=" + deathDay + "'" + '\n' +
                "defaultLinkToShare='" + defaultLinkToShare + "'" + '\n' +
                "defaultPhotoToShare='" + defaultPhotoToShare + "'" + '\n' +
                "photos=" + photos + "'" + '\n' +
                '}';
    }
}
