package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import ai.api.model.Result;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.message.ServiceData;
import cat.guillempages.homecontrol.hass.message.ServiceRequest;

/**
 * Turn the radio off.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOff extends AbstractAction {
    private Hass mHass;

    /**
     * Constructor.
     *
     * @param context The context.
     * @param hass The HASS wrapper.
     */
    public RadioOff(final Context context, final Hass hass) {
        super(context);
        mHass = hass;
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
            message.service = "media_pause";

            // TODO: parameterize.
            message.serviceData = new ServiceData().setEntityId("media_player.despatx");

            mHass.send(message);
    }

}
