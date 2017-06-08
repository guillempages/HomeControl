package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;

/**
 * {@link MediaPlayer} entity for the Office.
 *
 * Created by guillem on 08/06/2017.
 */

public class OfficePlayer extends MediaPlayer {
    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public OfficePlayer(final Hass hass) {
        super(hass);
    }

    @Override
    String getEntityId() {
        return "media_player.despatx";
    }
}
