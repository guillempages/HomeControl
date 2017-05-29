package cat.guillempages.homecontrol.hass.message;

/**
 * HAAS service request.
 *
 * Created by guillem on 28/05/2017.
 */
public class ServiceRequest extends BaseHassMessage {
    public String domain;
    public String service;

    public ServiceData serviceData;

    /**
     * Constructor.
     */
    public ServiceRequest() {
        type = "call_service";
    }
}
