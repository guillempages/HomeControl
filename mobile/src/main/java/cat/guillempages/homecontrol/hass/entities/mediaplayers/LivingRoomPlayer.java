package cat.guillempages.homecontrol.hass.entities.mediaplayers;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.MediaPlayer;
import cat.guillempages.homecontrol.hass.entities.Remote;
import cat.guillempages.homecontrol.hass.entities.remotes.HarmonyHub;

/**
 * {@link MediaPlayer} Entity for the Living Room. This entity implicitly starts and stops the
 * "Sonos" Activity in the Harmony Hub as well.
 *
 * Created by guillem on 08/06/2017.
 */
public class LivingRoomPlayer extends MediaPlayer {

    public static final String ENTITY_ID = "media_player.sala_destar";

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    public LivingRoomPlayer(final Hass hass) {
        super(hass);
    }

    @Override
    public String getEntityId() {
        return ENTITY_ID;
    }

    @Override
    public boolean play() {
        final Remote harmony = mHass.getEntityMap().getRemote(HarmonyHub.ENTITY_ID);
        return harmony.turnOn(HarmonyHub.SONOS_WOHNZIMMER) && super.play();
    }

    @Override
    public boolean pause() {
        final Remote harmony = mHass.getEntityMap().getRemote(HarmonyHub.ENTITY_ID);
        return harmony.turnOff() && super.pause();
    }
}
