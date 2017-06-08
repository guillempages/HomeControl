package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import ai.api.model.Result;
import cat.guillempages.homecontrol.hass.Hass;
import cat.guillempages.homecontrol.hass.entities.OfficePlayer;

/**
 * Turn the radio on.
 *
 * Created by guillem on 29/05/2017.
 */
public class RadioOn extends AbstractAction {
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

        // TODO: Parameterize
        new OfficePlayer(mHass).play();

        return result.getFulfillment().getSpeech();
    }
}
