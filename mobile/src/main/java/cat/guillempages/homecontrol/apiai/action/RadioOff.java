package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import ai.api.model.Result;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.OfficePlayer;

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
     * @param hass    The HASS wrapper.
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

        // TODO: Parameterize
        new OfficePlayer(mHass).pause();
        return result.getFulfillment().getSpeech();
    }
}
