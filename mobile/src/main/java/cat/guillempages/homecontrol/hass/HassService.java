package cat.guillempages.homecontrol.hass;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLException;

import cat.guillempages.homecontrol.hass.message.BaseHassMessage;
import cat.guillempages.homecontrol.hass.message.HassAuthentication;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.tls.OkHostnameVerifier;

/**
 * Service to communicate with the home assistant server.
 *
 * Created by guillem on 27/05/2017.
 */
class HassService extends Service {

    private static final String TAG = HassService.class.getSimpleName();

    public static final int CONNECTION_TIMEOUT = 10;
    public static final int DISCONNECT_CODE = 1001;

    private AtomicBoolean mIsConnecting = new AtomicBoolean(false);

    private AtomicBoolean mIsConnected = new AtomicBoolean(false);
    private WebSocket mHassSocket;
    private WebSocketListener socketListener = new HassSocketListener();

    // Binder given to clients
    private final IBinder mBinder = new HassBinder();
    private Gson mGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Override
    public void onCreate() {
        super.onCreate();
        connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Log.d(TAG, "Binding service");
        return mBinder;
    }

    /**
     * Get the URL of the server to connect to.
     * TODO: Make it configurable in the preferences.
     *
     * @return The URL of the server to connect to.
     */
    private String getUrl() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String protocol;
        if (prefs.getBoolean("pref_ha_use_https", true)) {
            protocol = "https";
        } else {
            protocol = "http";
        }
        final String serverName = prefs.getString("pref_ha_server", "");
        final String serverPort = prefs.getString("pref_ha_port", "8123");
        final String url = protocol + "://" + serverName + ":" + serverPort + "/api/websocket";

        Log.d(TAG, "URL: " + url);
        return url;
    }

    /**
     * Start a connection to the home assistant server.
     */
    private void connect() {
        // If already connecting, just ignore the call.
        if (mIsConnecting.compareAndSet(true, true)) {
            Log.d(TAG, "Already connecting...");
            return;
        }

        // Check if already connected
        if (mHassSocket != null) {
            if (mIsConnected.get()) {
                Log.d(TAG, "Already connected");
                mIsConnecting.set(false);
                return;
            } else {
                Log.d(TAG, "Disconnecting previous instance...");
                disconnect();
            }
        }

        final HttpUrl url = HttpUrl.parse(getUrl());

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .hostnameVerifier(OkHostnameVerifier.INSTANCE).build();
        Log.d(TAG, "Connecting...");
        mHassSocket = client.newWebSocket(new Request.Builder().url(url).build(), socketListener);
    }

    /**
     * Close the connection to the server.
     */
    private void disconnect() {
        if (mHassSocket != null) {
            Log.d(TAG, "Disconnecting...");
            mHassSocket.close(DISCONNECT_CODE, "Application closed");
            mHassSocket = null;
        } else {
            mIsConnected.set(false);
        }
    }

    /**
     * Check whether the service is connected.
     *
     * @return True if the service is connected, false otherwise.
     */
    public boolean isConnected() {
        return mIsConnected.get();
    }

    /**
     * Send the given message to the HASS server.
     *
     * @param message The message to send.
     * @return true if successfully sent; false otherwise.
     */
    public boolean send(final BaseHassMessage message) {
        Log.d(TAG, "Sending message: " + message);
        if (mHassSocket == null || !mIsConnected.get()) {
            Log.e(TAG, "Could not send message. Server is not connected");
            return false;
        }
        final String text = mGson.toJson(message);
        return mHassSocket.send(text);
    }


    /**
     * Web socket listener.
     */
    private class HassSocketListener extends WebSocketListener {
        @Override
        public void onOpen(final WebSocket webSocket, final Response response) {
            Log.d(TAG, "Opening socket...");
            mIsConnecting.set(false);
            mIsConnected.set(true);
        }

        @Override
        public void onMessage(final WebSocket webSocket, final String text) {
            Log.d(TAG, "Received " + text);
            try {
                final BaseHassMessage message = mGson.fromJson(text, BaseHassMessage.class);
                final String type = message.type;
                switch (type != null ? type : "") {
                    case "auth_required":
                        Log.d(TAG, "Authenticating..");
                        //TODO: authenticate;
                        break;
                    case "auth_failed":
                    case "auth_invalid":
                        Log.w(TAG, "Authentication failed: "
                                + mGson.fromJson(text, HassAuthentication.class).message);
                        break;
                    case "auth_ok":
                        Log.d(TAG, "Authenticated.");
                        break;
                    case "event":
                        // TODO
                        break;
                    case "result":
                        // TODO
                        break;
                }
            } catch (final Throwable e) {
                // Catch everything that it doesn't get passed to onFailure
                Log.e(TAG, "Error in onMessage()", e);
            }
        }

        @Override
        public void onClosed(final WebSocket webSocket, final int code, final String reason) {
            Log.d(TAG, "WebSocket closed: " + reason);
            mIsConnected.set(false);
        }

        @Override
        public void onFailure(final WebSocket webSocket, final Throwable e,
                              final Response response) {
            mIsConnecting.set(false);
            mIsConnected.set(false);
            if (e instanceof SocketException || e instanceof ProtocolException ||
                    e instanceof SSLException || e instanceof UnknownHostException) {
                Log.e(TAG, "Error while connecting to Socket: ", e);
                disconnect();
                return;
            }
            Log.e(TAG, "Error from onFailure()", e);
        }
    }

    /**
     * Binder class to be able to locally bind to the service.
     */
    class HassBinder extends Binder {
        /**
         * Get the instance of the service from the binder.
         *
         * @return The {@link HassService} instance.
         */
        HassService getService() {
            return HassService.this;
        }
    }

}
