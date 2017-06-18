package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Abstract media player class, to offer common basic functionality to all media player instances.
 * Extend this class for each supported entity.
 *
 * Created by guillem on 08/06/2017.
 */
public abstract class MediaPlayer {
    private Hass mHass;

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    MediaPlayer(final Hass hass) {
        mHass = hass;
    }

    /**
     * Start playing.
     */
    public boolean play() {
        final ServiceRequest message = new ServiceRequest();
        message.domain = "media_player";
        message.service = "media_play";
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        return mHass.send(message);
    }

    /**
     * Stop playing.
     */
    public void pause() {
        final ServiceRequest message = new ServiceRequest();
        message.domain = "media_player";
        message.service = "media_pause";
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        mHass.send(message);
    }

    /**
     * Get the entity Id referenced by this player instance.
     *
     * @return The entity id to use in HASS.
     */
    abstract String getEntityId();
}
