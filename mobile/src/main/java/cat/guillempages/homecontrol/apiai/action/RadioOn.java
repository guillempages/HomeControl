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
 * Turn the radio on.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOn extends AbstractAction {
    private static final String TAG = RadioOn.class.getSimpleName();
    private WeakReference<HassService> mHass;

    /**
     * Constructor.
     *
     * @param context The context.
     */
    public RadioOn(final Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "radio_on";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        startRadio();
        return result.getFulfillment().getSpeech();
    }

    /**
     * Start the radio on the desired room.
     */
    private void startRadio() {
        final HassService hass = mHass.get();
        if (hass == null) {
            Log.e(TAG, "No connection to HASS service!");
        } else {
            final ServiceRequest message = new ServiceRequest();
            message.domain = "media_player";
            message.service = "media_play";
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
