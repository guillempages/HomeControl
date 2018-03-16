package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;

import ai.api.model.Result;
import cat.guillempages.homecontrol.R;
import cat.guillempages.homecontrol.apiai.EntityMap;
import cat.guillempages.homecontrol.hass.HassEntityMap;
import cat.guillempages.homecontrol.hass.entities.Light;

/**
 * Turn the lights on.
 *
 * Created by guillem on 29/05/2017.
 */
public class LightOn extends AbstractAction {

    /**
     * Constructor.
     *
     * @param context The context.
     * @param hass    The HASS entity map.
     */
    public LightOn(final Context context, final HassEntityMap hass) {
        super(context, hass);
    }

    @Override
    public String getName() {
        return "light_on";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        final JsonElement parameter = result.getParameters().get("light_name");
        if (parameter == null) {
            // No parameter specified; the default for lights is to ask which light to turn on
            return result.getFulfillment().getSpeech();
        } else {
            final String lightEntity = parameter.getAsString();
            if (turnOn(lightEntity)) {
                return result.getFulfillment().getSpeech();
            } else {
                return getContext().getString(R.string.error_could_switch_on_light);
            }
        }
    }

    /**
     * Turn the specified light entity on. This method will resolve the mapping between API.AI and
     * HASS entities, and call the turnOn method in the HASS entity (if a mapping was found).
     *
     * @param lightEntity the (API.AI) entity to turn on.
     * @return True if the entity was found and could be started; false otherwise.
     */
    private boolean turnOn(final String lightEntity) {
        final boolean success;
        Log.d(getName(), "Turning " + lightEntity + " on");
        final Light light = getHassEntities().getLight(EntityMap.getEntityName(lightEntity));
        success = light != null && light.turnOn();
        return success;
    }
}
