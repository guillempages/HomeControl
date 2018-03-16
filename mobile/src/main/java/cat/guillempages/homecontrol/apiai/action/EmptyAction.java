package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import ai.api.model.Result;

/**
 * Noop action to use as default.
 *
 * Created by guillem on 29/05/2017.
 */

public class EmptyAction extends AbstractAction {

    /**
     * Constructor.
     *
     * @param context The context.
     */
    public EmptyAction(final Context context) {
        super(context, null);
    }

    @Override
    public String getName() {
        return "empty";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);
        return result.getFulfillment().getSpeech();
    }
}
