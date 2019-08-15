package cat.guillempages.homecontrol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes annotated with this will be automatically added to the ApiAi ActionMap.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ApiAiAction {
    /**
     * Whether the action is HAAS related or not. HAAS related actions will get the Haas Entity
     * list passed as argument on the constructor.
     *
     * @return true if the action is HAAS related. defaults to false.
     */
    boolean isHaas() default false;
}
