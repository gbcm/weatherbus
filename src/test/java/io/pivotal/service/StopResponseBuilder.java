package io.pivotal.service;

public class StopResponseBuilder {
    private double latitude = 47.654365;
    private double longitude = -122.305214;

    public StopResponse build() {
        StopEntry stopEntry = new StopEntry();
        stopEntry.setLatitude(latitude);
        stopEntry.setLongitude(longitude);

        StopData stopData = new StopData();
        stopData.setEntry(stopEntry);

        StopResponse response = new StopResponse();
        response.setData(stopData);

        return response;
    }

    public StopResponseBuilder latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public StopResponseBuilder longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

}
