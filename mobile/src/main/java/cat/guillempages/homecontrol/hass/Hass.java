package cat.guillempages.homecontrol.hass;

import android.util.Log;

import java.lang.ref.WeakReference;

import cat.guillempages.homecontrol.hass.message.BaseHassMessage;

/**
 * Wrapper class to abstract the Home Assistant communication.
 *
 * Get access to the Entity Factory or the Hass Service via this class.
 *
 * Created by guillem on 08/06/2017.
 */
public class Hass {
    private static final String TAG = Hass.class.getSimpleName();

    private WeakReference<HassService> mHassService;

    /**
     * Set the HASS service instance. Needs to be called every time a new instance of the service
     * is bound.
     *
     * @param hassService The instance to the {@link HassService}.
     */
    public void setHassService(final HassService hassService) {
        mHassService = new WeakReference<>(hassService);
    }

    /**
     * Send the given message to the HASS server.
     *
     * @param message The message to send.
     * @return true if successfully sent; false otherwise.
     */
    public boolean send(final BaseHassMessage message) {
        final HassService service = mHassService.get();

        if (service == null) {
            Log.e(TAG, "Service not available to send " + message);
            return false;
        } else {
            return service.send(message);
        }
    }
}
