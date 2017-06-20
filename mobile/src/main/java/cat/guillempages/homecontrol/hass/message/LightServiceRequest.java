package cat.guillempages.homecontrol.hass.message;

/**
 * Service request for the light domain.
 * Created by guillem on 19/06/2017.
 */
public class LightServiceRequest extends ServiceRequest {
    /**
     * Constructor.
     *
     * @param service The service to call
     */
    public LightServiceRequest(final String service) {
        super("light", service);
    }
}
