package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;

/**
 * {@link Light} entity for the wall lights.
 *
 * Created by guillem on 08/06/2017.
 */
public class SideLights extends Light {

    public static final String ENTITY_ID = "light.lux_lamp_1,light.lux_lamp_2";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public SideLights(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
}
