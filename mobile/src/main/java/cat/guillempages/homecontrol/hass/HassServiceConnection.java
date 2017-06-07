package cat.guillempages.homecontrol.hass;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cat.guillempages.homecontrol.MainActivity;
import cat.guillempages.homecontrol.hass.HassService.HassBinder;

/**
 * Service connection class, to simplify binding to the Hass service.
 *
 * Created by guillem on 28/05/2017.
 */
public class HassServiceConnection implements ServiceConnection {
    private boolean mServiceBound;

    private HassService mHass;

    private MainActivity mContext;

    /**
     * Constructor.
     *
     * @param activity The context.
     */
    public HassServiceConnection(final MainActivity activity) {
        mContext = activity;
    }

    @Override
    public void onServiceConnected(final ComponentName className,
                                   final IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        mHass = ((HassBinder) service).getService();
        mServiceBound = true;
        mContext.serviceConnected(mHass);
    }

    @Override
    public void onServiceDisconnected(final ComponentName arg0) {
        mServiceBound = false;
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

}
