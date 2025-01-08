package com.example.chargepointsmvc.model;

public class ChargePoint {
    private String referenceId;
    private double latitude;
    private double longitude;
    private String town;
    private String county;
    private String postcode;
    private String chargeDeviceStatus;
    private String connectorID;
    private String connectorType;
    private boolean isFavorite; // Add this field

    // Constructor
    public ChargePoint(String referenceId, double latitude, double longitude, String town, String county,
                       String postcode, String chargeDeviceStatus, String connectorID, String connectorType) {
        this.referenceId = referenceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
        this.chargeDeviceStatus = chargeDeviceStatus;
        this.connectorID = connectorID;
        this.connectorType = connectorType;
        this.isFavorite = false; // Default to not favorite
    }

    // Getters and Setters
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getTown() { return town; }
    public void setTown(String town) { this.town = town; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public String getChargeDeviceStatus() { return chargeDeviceStatus; }
    public void setChargeDeviceStatus(String chargeDeviceStatus) { this.chargeDeviceStatus = chargeDeviceStatus; }

    public String getConnectorID() { return connectorID; }
    public void setConnectorID(String connectorID) { this.connectorID = connectorID; }

    public String getConnectorType() { return connectorType; }
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }

    // Getter and Setter for isFavorite
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    @Override
    public String toString() {
        return "Ref: " + referenceId + ", Town: " + town;
    }
}