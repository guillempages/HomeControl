package cat.guillempages.homecontrol.hass.entities.mediaplayers;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.MediaPlayer;

/**
 * {@link MediaPlayer} Entity for the Kitchen.
 *
 * Created by guillem on 08/06/2017.
 */
public class KitchenPlayer extends MediaPlayer {

    public static final String ENTITY_ID = "media_player.cuina";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public KitchenPlayer(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }
}
