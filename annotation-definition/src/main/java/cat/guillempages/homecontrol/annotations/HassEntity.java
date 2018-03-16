package cat.guillempages.homecontrol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes annotated with this represent an HASS entity. An instance of every non-abstract class
 * (or sub-class) with this annotation in its class hierarchy will be automatically added to the
 * generated HassEntityMap class.
 *
 * The entity must have a public constructor accepting a "Hass" as a single argument.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Inherited
public @interface HassEntity {

    /**
     * Entity classes. The values in this enum must exactly correspond to the basic abstract entity
     * classes; the name of the types will be used as a type to generate the entity class
     * collections.
     */
    enum EntityType {
        Entity, Light, Remote, MediaPlayer;
    }

    /**
     * The type of entity this annotation refers to.
     * @return The entity class.
     */
    EntityType value() default EntityType.Entity;
}
