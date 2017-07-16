package cat.guillempages.homecontrol.hass.message;

/**
 * Authentication messages.
 *
 * Created by guillem on 28/05/2017.
 */
public class HassAuthentication extends BaseHassMessage {
    public String ha_version;
    public String api_password;
    public String message;

    /**
     * Constructor.
     */
    public HassAuthentication() {
        id = null;
        type = "auth";
    }

    @Override
    public boolean isSecret() {
        return true;
    }
}
