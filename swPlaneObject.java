package com.guamflights;

public class swPlaneObject {

    private int itemIndex;
    private String flightNum;
    private Double myLat;
    private Double myLon;
    private Integer planeTrack;
    private Integer squawk;

    public swPlaneObject() {
        this.itemIndex = -1;
        this.flightNum = "";
        this.myLat = 0.0;
        this.myLon = 0.0;
        this.planeTrack = 0;
        this.squawk = 0;
    }

    public swPlaneObject(String flightNum, Double myLat, Double myLon, Integer planeTrack, Integer squawk) {
        this.itemIndex = -1;
        this.flightNum = flightNum;
        this.myLat = myLat;
        this.myLon = myLon;
        this.planeTrack = planeTrack;
        this.squawk = squawk;
    }

    public int getItemIndex() {return itemIndex;}
    public void setItemIndex(int itemIndex){this.itemIndex = itemIndex;}

    public String getFlightNum() {return flightNum;}
    public void setFlightNum(String flightNum) {this.flightNum = flightNum;}

    public Double getMyLat() {return myLat;}
    public void setMyLat(Double myLat) {this.myLat = myLat;}

    public Double getMyLon() {return myLon;}
    public void setMyLon(Double myLon) {this.myLon = myLon;}

    public Integer getPlaneTrack() {return planeTrack;}
    public void setPlaneTrack(Integer planeTrack) {this.planeTrack = planeTrack;}

    public Integer getSquawk() {return squawk;}
    public void setSquawk(Integer squawk) {this.squawk = squawk;}

}
