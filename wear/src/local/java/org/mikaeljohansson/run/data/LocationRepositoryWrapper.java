package org.mikaeljohansson.run.data;

public class LocationRepositoryWrapper {

    public static LocationRepository getLocationRepository() {
        new MockLocationTask().execute();

        return LocationRepository.getInstance();
    }
}
