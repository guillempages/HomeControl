package cat.guillempages.homecontrol.hass.message;

import cat.guillempages.homecontrol.hass.entities.Light;

/**
 * Additional Data to pass to {@link LightServiceRequest Service Requests}.
 *
 * Created by guillem on 20/06/2017.
 */
public class LightServiceData extends ServiceData {
    private int transition;
    private int brightness;

    @Override
    public LightServiceData setEntityId(final String entityId) {
        super.setEntityId(entityId);
        return this;
    }

    /**
     * Set the transition value on this object. Can be used as in a Builder pattern.
     *
     * @param transition The transition time to set, in seconds. Must be a positive integer.
     * @return this instance.
     */
    public LightServiceData setTransition(final int transition) {
        if (transition > 0) {
            this.transition = transition;
        }
        return this;
    }

    /**
     * Set the brightness value on this object. Can be used as in a Builder pattern.
     *
     * @param brightness The brightness to set. Must be in the range 0-{@link Light#MAX_BRIGHTNESS}
     *                   (will be capped).
     * @return this instance.
     */
    public LightServiceData setBrightness(final int brightness) {
        if (brightness < 0) {
            this.brightness = 0;
        } else if (brightness > Light.MAX_BRIGHTNESS) {
            this.brightness = Light.MAX_BRIGHTNESS;
        } else {
            this.brightness = brightness;
        }
        return this;
    }
}
