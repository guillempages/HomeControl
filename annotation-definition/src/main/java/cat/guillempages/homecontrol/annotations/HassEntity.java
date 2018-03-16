package cat.guillempages.homecontrol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gupa8558 on 14.03.18.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Inherited
public @interface HassEntity {
    enum EntityType {
        Entity, Light, Remote, MediaPlayer;
    }

    EntityType value() default EntityType.Entity;
}
