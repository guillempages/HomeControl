package cat.guillempages.homecontrol.hass.message;

/**
 * Service request for the media player domain.
 * Created by guillem on 19/06/2017.
 */
public class MediaPlayerServiceRequest extends ServiceRequest {
    /**
     * Constructor.
     *
     * @param service The service to call
     */
    public MediaPlayerServiceRequest(final String service) {
        super("media_player", service);
    }
}
