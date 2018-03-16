package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import ai.api.model.Result;
import cat.guillempages.homecontrol.annotations.ApiAiAction;

/**
 * Greeting action.
 *
 * Created by guillem on 29/05/2017.
 */
@ApiAiAction
public class InputWelcome extends AbstractAction {

    /**
     * Constructor.
     *
     * @param context The context.
     */
    public InputWelcome(final Context context) {
        super(context, null);
    }

    @Override
    public String getName() {
        return "input.welcome";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);
        return result.getFulfillment().getSpeech();
    }
}
