package cat.guillempages.homecontrol.hass.message;

/**
 * Request the states of all entities/platforms.
 *
 * Created by guillem on 28/05/2017.
 */
public class StatesRequest extends BaseHassMessage {
    /**
     * Constructor.
     */
    public StatesRequest() {
        type = "get_states";
    }
}
