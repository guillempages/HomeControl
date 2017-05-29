package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;

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
        super(context);
    }

    @Override
    public String getName() {
        return "empty";
    }
}
