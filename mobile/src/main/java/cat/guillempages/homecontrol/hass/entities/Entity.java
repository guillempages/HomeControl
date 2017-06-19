package cat.guillempages.homecontrol.hass.entities;

/**
 * Created by guillem on 08/06/2017.
 */
public interface Entity {
    /**
     * Get the entity Id referenced by this entity.
     *
     * @return The entity id to use in HASS.
     */
    String getEntityId();
}
