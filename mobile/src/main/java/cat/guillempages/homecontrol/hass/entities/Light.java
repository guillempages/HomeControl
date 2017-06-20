package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.message.LightServiceData;
import cat.guillempages.homecontrol.hass.message.LightServiceRequest;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Abstract remote class, to offer common basic functionality to all light instances.
 * Extend this class for each supported entity.
 *
 * Created by guillem on 08/06/2017.
 */
public abstract class Light implements Entity {
    /** Maximum value for the brightness for any light. */
    public static final int MAX_BRIGHTNESS = 255;

    private static final String SERVICE_TURN_ON = "turn_on";
    private static final String SERVICE_TURN_OFF = "turn_off";
    private Hass mHass;

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    Light(final Hass hass) {
        mHass = hass;
    }

    /**
     * Switch light on.
     */
    public boolean turnOn() {
        final ServiceRequest message = new LightServiceRequest(SERVICE_TURN_ON);
        message.serviceData = new LightServiceData()
                .setEntityId(getEntityId())
                .setBrightness(MAX_BRIGHTNESS);

        return mHass.send(message);
    }

    /**
     * Switch light off.
     */
    public boolean turnOff() {
        final ServiceRequest message = new LightServiceRequest(SERVICE_TURN_OFF);
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        return mHass.send(message);
    }
}
