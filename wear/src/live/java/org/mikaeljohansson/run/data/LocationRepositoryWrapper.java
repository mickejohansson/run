package org.mikaeljohansson.run.data;

public class LocationRepositoryWrapper {

    public LocationRepository getLocationRepository() {
        return new LocationRepository();
    }
}
