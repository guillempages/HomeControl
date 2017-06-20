package cat.guillempages.homecontrol.hass.entities.lights;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.Light;

/**
 * {@link Light} entity for the warm LED stripe.
 *
 * Created by guillem on 08/06/2017.
 */
public class WarmLeds extends Light {

    public static final String ENTITY_ID = "light.osram_plug";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public WarmLeds(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }

    // TODO: Fix turning on this light.
}
