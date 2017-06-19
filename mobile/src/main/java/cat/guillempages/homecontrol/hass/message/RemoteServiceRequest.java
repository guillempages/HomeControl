package cat.guillempages.homecontrol.hass.message;

/**
 * Service request for the remote domain.
 * Created by guillem on 19/06/2017.
 */
public class RemoteServiceRequest extends ServiceRequest {
    /**
     * Constructor.
     *
     * @param service The service to call
     */
    public RemoteServiceRequest(final String service) {
        super("remote", service);
    }
}
