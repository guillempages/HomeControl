package cat.guillempages.homecontrol.hass;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cat.guillempages.homecontrol.MainActivity;
import cat.guillempages.homecontrol.hass.HassServiceConnection.ServiceConnectionListener;
import cat.guillempages.homecontrol.hass.entities.KitchenPlayer;
import cat.guillempages.homecontrol.hass.entities.MediaPlayer;
import cat.guillempages.homecontrol.hass.entities.OfficePlayer;
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

    private final Map<String, MediaPlayer> mMediaPlayers = new HashMap<>();

    /**
     * Constructor.
     */
    public Hass() {
        addMediaPlayer(new OfficePlayer(this));
        addMediaPlayer(new KitchenPlayer(this));
    }

    /**
     * Add a mediaplayer to the media players map.
     *
     * @param mediaPlayer The player to add.
     */
    private void addMediaPlayer(final MediaPlayer mediaPlayer) {
        mMediaPlayers.put(mediaPlayer.getEntityId(), mediaPlayer);
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
     * Get the instance of the media player for the given entity id
     *
     * @param entityId The entity to get the media player for.
     * @return The {@link MediaPlayer} entity.
     */
    public MediaPlayer getMediaPlayer(final String entityId) {
        final MediaPlayer player = mMediaPlayers.get(entityId);
        Log.d(TAG, "Got player for " + entityId + ": " + player);
        return player;
    }
}
