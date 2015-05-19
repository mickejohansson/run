package org.mikaeljohansson.run.presentation;

import org.mikaeljohansson.run.business.SpeedometerService;

public class SpeedometerServiceFactory {
    private static SpeedometerService sSpeedometerService;

    public static SpeedometerService getSpeedometerService() {
        if (sSpeedometerService == null) {
            sSpeedometerService = new SpeedometerService();
        }

        return sSpeedometerService;
    }
}
