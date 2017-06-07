package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

import ai.api.model.Result;
import cat.guillempages.homecontrol.hass.HassService;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Turn the radio off.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOff extends AbstractAction {
    private static final String TAG = RadioOff.class.getSimpleName();
    private WeakReference<HassService> mHass;

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
        final HassService hass = mHass.get();
        if (hass == null) {
            Log.e(TAG, "No connection to HASS service.");
        } else {
            final ServiceRequest message = new ServiceRequest();
            message.domain = "media_player";
            message.service = "media_pause";

            // TODO: parameterize.
            message.serviceData = new ServiceData().setEntityId("media_player.despatx");

            final String jsonMsg = GSON.toJson(message);
            hass.send(jsonMsg);
        }
    }

    @Override
    public void setHass(final HassService hass) {
        mHass = new WeakReference<>(hass);
    }
}
