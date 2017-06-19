package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;

/**
 * {@link MediaPlayer} entity for the Office.
 *
 * Created by guillem on 08/06/2017.
 */

public class OfficePlayer extends MediaPlayer {

    public static final String ENTITY_ID = "media_player.despatx";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public OfficePlayer(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
}
