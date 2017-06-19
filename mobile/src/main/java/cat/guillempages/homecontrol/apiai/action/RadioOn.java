package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import ai.api.model.Result;
import cat.guillempages.homecontrol.R;
import cat.guillempages.homecontrol.apiai.EntityMap;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.MediaPlayer;

/**
 * Turn the radio on.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOn extends AbstractAction {
    private static final String DEFAULT_RADIO = "Küche";

    private Hass mHass;

    /**
     * Constructor.
     *
     * @param context The context.
     * @param hass    The HASS wrapper.
     */
    public RadioOn(final Context context, final Hass hass) {
        super(context);
        mHass = hass;
    }

    @Override
    public String getName() {
        return "radio_on";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        boolean success = false;

        final JsonElement parameter = result.getParameters().get("sonos_name");
        final JsonArray entitiesList = parameter.getAsJsonArray();
        if (entitiesList.size() > 0) {
            // TODO: Join all requested players into a single stream.
            for (final JsonElement entity : entitiesList) {
                final String mediaEntity = entity.getAsString();
                success |= play(mediaEntity);
            }
        } else {
            success = play(DEFAULT_RADIO);
        }
        if (success) {
            return result.getFulfillment().getSpeech();
        } else {
            return getContext().getString(R.string.error_could_not_play);
        }
    }

    /**
     * Turn the specified media entity on. This method will resolve the mapping between API.AI and
     * HASS entities, and call the play method in the HASS entity (if a mapping was found).
     *
     * @param mediaEntity the (API.AI) entity to turn on.
     * @return True if the entity was found and could be started; false otherwise.
     */
    private boolean play(final String mediaEntity) {
        final boolean success;
        Log.d(getName(), "Turning " + mediaEntity + " on");
        final MediaPlayer player =
                mHass.getMediaPlayer(EntityMap.getEntityName(mediaEntity));
        success = player != null && player.play();
        return success;
    }
}
