package cat.guillempages.homecontrol.apiai.action;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

import ai.api.model.Result;
import cat.guillempages.homecontrol.R;

/**
 * Action for getting the current time and return a human readable string.
 *
 * Created by guillem on 29/05/2017.
 */
public class GetTime extends AbstractAction {
    /**
     * Constructor.
     *
     * @param context The context.
     */
    public GetTime(final Context context) {
        super(context, null);
    }

    @Override
    public String getName() {
        return "get_time";
    }

    @Override
    public String execute(@NonNull final Result result) {
        super.execute(result);

        final Calendar calendar = Calendar.getInstance();
        return getContext().getString(R.string.reply_time, calendar.getTimeInMillis());
    }
}
