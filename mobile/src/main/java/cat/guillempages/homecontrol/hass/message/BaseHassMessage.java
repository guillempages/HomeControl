package cat.guillempages.homecontrol.hass.message;

/**
 * Base class for all Hass Messages. This class takes care of automatically updating the id to
 * ensure successful communication.
 *
 * Created by guillem on 28/05/2017.
 */
public class BaseHassMessage {
    private static int sMessageId = 1;

    public String type;
    protected Integer id;

    /**
     * Constructor.
     */
    public BaseHassMessage() {
        id = sMessageId++;
    }

    /**
     * Overwrite this to return true, if messages should not be logged.
     * @return
     * whether the message can be logged or not.
     */
    public boolean isSecret() {
        return false;
    }
}
