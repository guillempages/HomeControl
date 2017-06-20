package cat.guillempages.homecontrol.hass.entities.lights;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.Light;

/**
 * {@link Light} entity for all lights.
 *
 * Created by guillem on 08/06/2017.
 */
public class AllLights extends Light {

    public static final String ENTITY_ID = "light.lux_lamp_1,light.lux_lamp_2," +
            "light.hue_lightstrip_plus_1,light.osram_plug";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public AllLights(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
    // TODO: Fix osram_plug
}
