package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.apiai.EntityMap;
import cat.guillempages.homecontrol.hass.Hass;

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
        final Remote harmony = mHass.getRemote(EntityMap.getEntityName("harmony"));
        return harmony.turnOn(HarmonyHub.SONOS_WOHNZIMMER) && super.play();
    }

    @Override
    public boolean pause() {
        final Remote harmony = mHass.getRemote(EntityMap.getEntityName("harmony"));
        return harmony.turnOff() && super.pause();
    }
}
