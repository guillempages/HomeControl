package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;

/**
 * {@link MediaPlayer} Entity for the Kitchen.
 *
 * Created by guillem on 08/06/2017.
 */
public class KitchenPlayer extends MediaPlayer {
    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public KitchenPlayer(final Hass hass) {
        super(hass);
    }

    @Override
    String getEntityId() {
        return "media_player.cuina";
    }
}
