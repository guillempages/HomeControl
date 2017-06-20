package cat.guillempages.homecontrol.hass.entities.remotes;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.Remote;

/**
 * {@link Remote} entity for the Harmony Hub.
 *
 * Created by guillem on 08/06/2017.
 */

public class HarmonyHub extends Remote {

    public static final String ENTITY_ID = "remote.harmony_hub";

    /**
     * Activity name / id to use for the living room sonos activity.
     */
    public static final String SONOS_WOHNZIMMER = "Sonos Wohnzimmer";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public HarmonyHub(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
}
