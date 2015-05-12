package org.mikaeljohansson.run.data;

public class LocationRepositoryWrapper {

    public static LocationRepository getLocationRepository() {
        return LocationRepository.getInstance();
    }
}
