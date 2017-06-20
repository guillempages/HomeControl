package cat.guillempages.homecontrol.hass.message;

/**
 * Additional Data to pass to {@link RemoteServiceRequest Service Requests}.
 *
 * Created by guillem on 20/06/2017.
 */
public class RemoteServiceData extends ServiceData {
    private String activity;

    @Override
    public RemoteServiceData setEntityId(final String entityId) {
        super.setEntityId(entityId);
        return this;
    }

    /**
     * Set the activity ID value on this object. Can be used as in a Builder pattern.
     *
     * @param activity The activity id to set.
     * @return this instance.
     */
    public RemoteServiceData setActivity(final String activity) {
        this.activity = activity;
        return this;
    }
}
