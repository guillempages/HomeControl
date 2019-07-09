package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ai.api.model.Result;
import cat.guillempages.homecontrol.R;
import cat.guillempages.homecontrol.hass.HassEntityMap;

/**
 * Base class for all API.AI actions.
 * <p>
 * Created by guillem on 29/05/2017.
 */

public abstract class AbstractAction {

    private static final String TAG = AbstractAction.class.getSimpleName();

    private Context mContext;
    private HassEntityMap mEntityMap;

    /**
     * Constructor.
     *
     * @param context  The context.
     * @param entities The entity map, to be able to communicate with Hass entities.
     */
    AbstractAction(final Context context, @Nullable final HassEntityMap entities) {
        mContext = context;
        mEntityMap = entities;
    }

    /**
     * Get the name of the action.
     *
     * @return the name of the action.
     */
    abstract public String getName();

    /**
     * Execute the action. The default implementation does nothing and returns a "Not
     * implemented" string.
     *
     * @param result The API.ai result object, containing the parameters for the action, and the
     *               user friendly response.
     * @return A human readable string to show and read to the user.
     */
    public String execute(@NonNull final Result result) {
        Log.d(TAG, "Executing action '" + getName() + "' with parameters '"
            + getParameterString(result.getParameters()) + "'");
        return mContext.getString(R.string.not_implemented_yet);
    }

    /**
     * Get the context.
     *
     * @return the context.
     */
    public Context getContext() {
        return mContext;
    }

    protected String getParameterString(final HashMap<String, JsonElement> parameters) {
        String parameterString = "";

        if (parameters != null && !parameters.isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry
                : parameters.entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        return parameterString;
    }

    /**
     * Get the Hass entity map, to be able to control the entities.
     *
     * @return The entity map.
     */
    protected HassEntityMap getHassEntities() {
        return mEntityMap;
    }
}
