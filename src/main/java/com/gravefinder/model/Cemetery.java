package com.gravefinder.model;

import java.util.ArrayList;
import java.util.List;

public class Cemetery {
    private int cemeteryId;
    private String name;
    private String cemLatitude;
    private String cemLongitude;
    private String cityName;
    private String countyName;
    private String stateName;
    private String linkToShare;
    private String defaultPhotoToShare;
    private List<Photo> photos;
    private List<Memorial> memorials;

    public Cemetery(int cemeteryId, String name, String cemLatitude, String cemLongitude, String cityName,
            String countyName, String stateName, String linkToShare, String defaultPhotoToShare) {
        this.setCemeteryId(cemeteryId);
        this.setName(name);
        this.setCemLatitude(cemLatitude);
        this.setCemLongitude(cemLongitude);
        this.setCityName(cityName);
        this.setCountyName(countyName);
        this.setStateName(stateName);
        this.setLinkToShare(linkToShare);
        this.setDefaultPhotoToShare(defaultPhotoToShare);
        this.photos = new ArrayList<>();
        this.memorials = new ArrayList<>();
    }

    public int getCemeteryId() {
        return cemeteryId;
    }

    public void setCemeteryId(int cemeteryId) {
        this.cemeteryId = cemeteryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCemLatitude() {
        return cemLatitude;
    }

    public void setCemLatitude(String cemLatitude) {
        this.cemLatitude = cemLatitude;
    }

    public String getCemLongitude() {
        return cemLongitude;
    }

    public void setCemLongitude(String cemLongitude) {
        this.cemLongitude = cemLongitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLinkToShare() {
        return linkToShare;
    }

    public void setLinkToShare(String linkToShare) {
        this.linkToShare = linkToShare;
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

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }

    public List<Memorial> getMemorials() {
        return memorials;
    }

    public void addMemorial(Memorial memorial) {
        this.memorials.add(memorial);
    }

    public void removeMemorial(Memorial memorial) {
        this.memorials.remove(memorial);
    }

    @Override
    public String toString() {
        return "Cemetery " + name + '\n' +
                "cemeteryId='" + cemeteryId + "'" + '\n' +
                "name='" + name + "'" + '\n' +
                "cemLatitude='" + cemLatitude + "'" + '\n' +
                "cemLongitude='" + cemLongitude + "'" + '\n' +
                "cityName='" + cityName + "'" + '\n' +
                "countyName='" + countyName + "'" + '\n' +
                "stateName='" + stateName + "'" + '\n' +
                "linkToShare='" + linkToShare + "'" + '\n' +
                "defaultPhotoToShare='" + defaultPhotoToShare + "'" + '\n' +
                "photos=" + photos +
                '}';
    }

}
