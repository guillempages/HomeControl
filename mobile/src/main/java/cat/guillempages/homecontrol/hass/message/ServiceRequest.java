package cat.guillempages.homecontrol.hass.message;

/**
 * HAAS service request.
 *
 * Created by guillem on 28/05/2017.
 */
public class ServiceRequest extends BaseHassMessage {
    private String domain;
    private String service;

    public ServiceData serviceData;

    /**
     * Constructor.
     *
     * @param domain  The domain for the service.
     * @param service The service to call.
     */
    public ServiceRequest(final String domain, final String service) {
        type = "call_service";
        this.domain = domain;
        this.service = service;
    }
}
