package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;

/**
 * {@link Light} entity for the colored LED stripe.
 *
 * Created by guillem on 08/06/2017.
 */
public class ColoredLeds extends Light {

    public static final String ENTITY_ID = "light.hue_lightstrip_plus_1";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public ColoredLeds(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
}
