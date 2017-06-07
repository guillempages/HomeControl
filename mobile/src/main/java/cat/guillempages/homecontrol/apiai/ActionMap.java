package cat.guillempages.homecontrol.apiai;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ai.api.model.Result;
import cat.guillempages.homecontrol.apiai.action.AbstractAction;
import cat.guillempages.homecontrol.apiai.action.EmptyAction;
import cat.guillempages.homecontrol.apiai.action.GetTime;
import cat.guillempages.homecontrol.apiai.action.InputWelcome;
import cat.guillempages.homecontrol.apiai.action.RadioOff;
import cat.guillempages.homecontrol.apiai.action.RadioOn;
import cat.guillempages.homecontrol.hass.HassService;

/**
 * Helper class to define all supported actions.
 *
 * Created by guillem on 29/05/2017.
 */
public class ActionMap {
    private final Map<String, AbstractAction> mActionMap = new HashMap<>();

    private final AbstractAction mDefaultAction;

    /**
     * Constructor.
     *
     * @param context The context.
     */
    public ActionMap(final Context context) {
        mDefaultAction = new EmptyAction(context);

        addAction(new GetTime(context));
        addAction(new RadioOn(context));
        addAction(new RadioOff(context));
        addAction(new InputWelcome(context));
    }

    /**
     * Add the given action to the map.
     *
     * @param action The action to add to the map.
     */
    private void addAction(@NonNull final AbstractAction action) {
        mActionMap.put(action.getName().toLowerCase(Locale.getDefault()), action);
    }

    /**
     * Get the {@link AbstractAction action} according to the given string.
     *
     * @param action The desired action's name.
     * @return The desired action, or the default action if not found.
     */
    @NonNull
    public AbstractAction getAction(@NonNull final String action) {
        final AbstractAction result = mActionMap.get(action.toLowerCase(Locale.getDefault()));
        if (result == null) {
            return mDefaultAction;
        } else {
            return result;
        }
    }

    /**
     * Execute the action corresponding to the result.
     *
     * @param result The API.ai result.
     * @return The String corresponding to the action execution.
     */
    @NonNull
    public String execute(@NonNull final Result result) {
        return getAction(result.getAction()).execute(result);
    }

    /**
     * Store a reference to the Home Assistant service.
     *
     * @param haas The home assistant service.
     */
    public void setHass(final HassService haas) {
        for (final AbstractAction action: mActionMap.values()) {
            action.setHass(haas);
        }
    }
}
