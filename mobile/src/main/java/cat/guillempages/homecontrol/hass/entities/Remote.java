package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.message.RemoteServiceRequest;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Abstract remote class, to offer common basic functionality to all remote instances.
 * Extend this class for each supported entity.
 *
 * Created by guillem on 08/06/2017.
 */
public abstract class Remote implements Entity {
    private Hass mHass;

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    Remote(final Hass hass) {
        mHass = hass;
    }

    /**
     * Start activity.
     */
    public boolean turnOn(final String activity) {
        final ServiceRequest message = new RemoteServiceRequest("turn_on");
        message.serviceData = new ServiceData().setEntityId(getEntityId()).setActivity(activity);

        return mHass.send(message);
    }

    /**
     * Stop all activities.
     */
    public boolean turnOff() {
        final ServiceRequest message = new RemoteServiceRequest("turn_off");
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        return mHass.send(message);
    }
}
