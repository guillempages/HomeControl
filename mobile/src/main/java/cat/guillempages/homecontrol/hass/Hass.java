package cat.guillempages.homecontrol.hass;

import android.util.Log;

import java.lang.ref.WeakReference;

import cat.guillempages.homecontrol.MainActivity;
import cat.guillempages.homecontrol.hass.HassServiceConnection.ServiceConnectionListener;
import cat.guillempages.homecontrol.hass.message.BaseHassMessage;

/**
 * Wrapper class to abstract the Home Assistant communication.
 *
 * Get access to the Entity Factory or the Hass Service via this class.
 *
 * Created by guillem on 08/06/2017.
 */
public class Hass implements ServiceConnectionListener {
    private static final String TAG = Hass.class.getSimpleName();

    private WeakReference<HassService> mHassService;
    private HassServiceConnection mConnection;
    private HassEntityMap mEntityMap;


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

    /**
     * Bind to the Hass Service.
     *
     * @param context The Context, to be able to bind to the service.
     */
    public void connect(final MainActivity context) {
        if (mConnection == null) {
            mConnection = new HassServiceConnection(context);
            mConnection.registerConnectionListener(this);
        }
        mConnection.connect();
        if (mHassService != null) {
            final HassService service = mHassService.get();
            if (service != null && !service.isConnected()) {
                service.connect();
            }
        }
    }

    /**
     * Unbind from the Hass Service.
     */
    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    @Override
    public void onServiceConnected(final HassService service) {
        mHassService = new WeakReference<>(service);
    }

    @Override
    public void onServiceDisconnected() {
        mHassService = null;
        mConnection = null;
    }

    /**
     * Check whether the HASS service is bound.
     *
     * @return true if the service is bound; false otherwise.
     */
    public boolean isBound() {
        return mConnection != null && mHassService.get() != null;
    }

    /**
     * Check whether the HASS service is currently connected to the server.
     *
     * @return True if the service is connected to the server; false otherwise.
     */
    public boolean isConnected() {
        final HassService hassService = mHassService.get();
        return hassService != null && hassService.isConnected();
    }

    /**
     * Check whether the HASS service is currently authenticated to the server.
     *
     * @return True if the service is authenticated to the server; false otherwise.
     */
    public boolean isAuthenticated() {
        final HassService hassService = mHassService.get();
        return hassService != null && hassService.isAuthenticated();
    }

    public void setEntityMap(final HassEntityMap entityMap) {
        mEntityMap = entityMap;
    }

    public HassEntityMap getEntityMap() {
        return mEntityMap;
    }
}
