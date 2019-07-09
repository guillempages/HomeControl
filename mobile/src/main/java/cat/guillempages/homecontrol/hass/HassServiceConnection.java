package cat.guillempages.homecontrol.hass;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import cat.guillempages.homecontrol.hass.HassService.HassBinder;

/**
 * Service connection class, to simplify binding to the Hass service.
 *
 * Created by guillem on 28/05/2017.
 */
class HassServiceConnection implements ServiceConnection {
    private boolean mServiceBound;

    private HassService mHass;

    private Context mContext;

    private final Collection<WeakReference<ServiceConnectionListener>> mConnectionListeners
            = new HashSet<>();

    /**
     * Constructor.
     *
     * @param activity The context.
     */
    public HassServiceConnection(final Context activity) {
        mContext = activity;
    }

    @Override
    public void onServiceConnected(final ComponentName className,
                                   final IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        mHass = ((HassBinder) service).getService();
        mServiceBound = true;
        final Iterator<WeakReference<ServiceConnectionListener>> iterator
                = mConnectionListeners.iterator();
        while (iterator.hasNext()) {
            final WeakReference<ServiceConnectionListener> listenerRef = iterator.next();
            final ServiceConnectionListener listener = listenerRef.get();
            if (listener == null) {
                iterator.remove();
            } else {
                listener.onServiceConnected(mHass);
            }
        }
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
        mServiceBound = false;
        mHass = null;
        final Iterator<WeakReference<ServiceConnectionListener>> iterator
                = mConnectionListeners.iterator();
        while (iterator.hasNext()) {
            final WeakReference<ServiceConnectionListener> listenerRef = iterator.next();
            final ServiceConnectionListener listener = listenerRef.get();
            if (listener == null) {
                iterator.remove();
            } else {
                listener.onServiceDisconnected();
            }
        }
    }

    /**
     * Bind to the service if not already bound.
     */
    public void connect() {
        if (!mServiceBound) {
            final Intent intent = new Intent(mContext, HassService.class);
            mContext.bindService(intent, this, Service.BIND_AUTO_CREATE);
        }
    }

    /**
     * Unbind from the service if still bound.
     */
    public void disconnect() {
        if (mServiceBound) {
            mContext.unbindService(this);
            mServiceBound = false;
        }
        mHass = null;
    }

    /**
     * Get the instance of the HASS service.
     *
     * @return The instance of the bound HASS service, or null if not bound.
     */
    @Nullable
    public HassService getHass() {
        return mHass;
    }

    /**
     * Register a new listener to get notified when the service connects/disconnects.
     *
     * @param listener The listener to register.
     */
    public void registerConnectionListener(final ServiceConnectionListener listener) {
        mConnectionListeners.add(new WeakReference<>(listener));
    }

    /**
     * Unregister a previously registered listener to get notified when the service
     * connects/disconnects.
     *
     * @param listener The listener to unregister.
     */
    public void unregisterConnectionListener(final ServiceConnectionListener listener) {
        final Iterator<WeakReference<ServiceConnectionListener>> iterator
                = mConnectionListeners.iterator();
        while (iterator.hasNext()) {
            final WeakReference<ServiceConnectionListener> listenerRef = iterator.next();
            if (listenerRef.get() == null || listenerRef == listener) {
                iterator.remove();
            }
        }
    }

    /**
     * Interface to receive notifications when the service gets connected/disconnected.
     */
    interface ServiceConnectionListener {
        /**
         * The service has been connected.
         *
         * @param service The connected service.
         */
        void onServiceConnected(final HassService service);

        /**
         * The service has been disconnected.
         */
        void onServiceDisconnected();
    }

}
