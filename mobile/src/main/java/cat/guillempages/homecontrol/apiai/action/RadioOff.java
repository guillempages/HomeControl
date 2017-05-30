package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import ai.api.model.Result;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Turn the radio off.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOff extends AbstractAction {
    private static final String TAG = RadioOff.class.getSimpleName();

    /**
     * Constructor.
     *
     * @param context The context.
     */
    public RadioOff(final Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "radio_off";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        stopRadio();
        return result.getFulfillment().getSpeech();
    }

    /**
     * Stop the requested radio.
     */
    private void stopRadio() {
        final ServiceRequest message = new ServiceRequest();
        message.domain = "media_player";
        message.service = "media_play_pause";

        // TODO: parameterize.
        message.serviceData = new ServiceData().setEntityId("media_player.despatx");

        final String jsonMsg = GSON.toJson(message);
        Log.d(TAG, "Sending request: " + jsonMsg);
        // TODO: get a reference to the HASS service and send the request.
//        mHass.send(jsonMsg);
    }

}
