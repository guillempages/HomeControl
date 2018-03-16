package cat.guillempages.homecontrol.hass.entities;

import cat.guillempages.homecontrol.annotations.HassEntity;
import cat.guillempages.homecontrol.annotations.HassEntity.EntityType;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.message.MediaPlayerServiceRequest;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Abstract media player class, to offer common basic functionality to all media player instances.
 * Extend this class for each supported entity.
 * <p>
 * Created by guillem on 08/06/2017.
 */
@HassEntity(EntityType.MediaPlayer)
public abstract class MediaPlayer implements Entity {
    private static final String SERVICE_PLAY = "media_play";
    private static final String SERVICE_PAUSE = "media_pause";
    protected Hass mHass;

    /**
     * Constructor.
     *
     * @param hass The {@link Hass Home Assistant Wrapper}.
     */
    protected MediaPlayer(final Hass hass) {
        mHass = hass;
    }

    /**
     * Start playing.
     */
    public boolean play() {
        final ServiceRequest message = new MediaPlayerServiceRequest(SERVICE_PLAY);
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        return mHass.send(message);
    }

    /**
     * Stop playing.
     */
    public boolean pause() {
        final ServiceRequest message = new MediaPlayerServiceRequest(SERVICE_PAUSE);
        message.serviceData = new ServiceData().setEntityId(getEntityId());

        return mHass.send(message);
    }
}
