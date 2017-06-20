package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;

import ai.api.model.Result;
import cat.guillempages.homecontrol.R;
import cat.guillempages.homecontrol.apiai.EntityMap;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.Light;

/**
 * Turn the lights on.
 *
 * Created by guillem on 29/05/2017.
 */
public class LightOff extends AbstractAction {
    private Hass mHass;

    /**
     * Constructor.
     *
     * @param context The context.
     * @param hass    The HASS wrapper.
     */
    public LightOff(final Context context, final Hass hass) {
        super(context);
        mHass = hass;
    }

    @Override
    public String getName() {
        return "light_off";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        final JsonElement parameter = result.getParameters().get("light_name");
        if (parameter == null) {
            // No parameter specified; the default for lights is to ask which light to turn off
            return result.getFulfillment().getSpeech();
        } else {
            final String lightEntity = parameter.getAsString();
            if (turnOff(lightEntity)) {
                return result.getFulfillment().getSpeech();
            } else {
                return getContext().getString(R.string.error_could_switch_off_light);
            }
        }
    }

    /**
     * Turn the specified light entity off. This method will resolve the mapping between API.AI and
     * HASS entities, and call the turnOff method in the HASS entity (if a mapping was found).
     *
     * @param lightEntity the (API.AI) entity to turn off.
     * @return True if the entity was found and could be started; false otherwise.
     */
    private boolean turnOff(final String lightEntity) {
        final boolean success;
        Log.d(getName(), "Turning " + lightEntity + " off");
        final Light light =
                mHass.getLight(EntityMap.getEntityName(lightEntity));
        success = light != null && light.turnOff();
        return success;
    }
}
