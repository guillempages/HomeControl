package cat.guillempages.homecontrol.hass.message;

/**
 * Additional Data to pass to {@link ServiceRequest Service Requests}.
 *
 * Created by guillem on 28/05/2017.
 */
public class ServiceData {
    public String entityId;
    public String activity;

    /**
     * Set the entity ID value on this object. Can be used as in a Builder pattern.
     *
     * @param entityId The entity id to set.
     * @return this instance.
     */
    public ServiceData setEntityId(final String entityId) {
        this.entityId = entityId;
        return this;
    }

    /**
     * Set the activity ID value on this object. Can be used as in a Builder pattern.
     *
     * @param activity The activity id to set.
     * @return this instance.
     */
    public ServiceData setActivity(final String activity) {
        this.activity = activity;
        return this;
    }
}
