package cn.dazhou.commonlib.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * LocationHelper
 * Created by Hooyee on 2017/08/07.
 */
public class LocationHelper implements LocationListener {

    private static final String TAG = "LocationHelper";

    private LocationManager mLocationManager;
    private MyLocationListener mListener;
    private Context mContext;

    public LocationHelper(Context context) {
        mContext = context.getApplicationContext();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void initLocation(MyLocationListener listener) {
        mListener = listener;
        Location location;

//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }

//        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Log.d(TAG, "LocationManager.GPS_PROVIDER");
//            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                mListener.updateLastLocation(location);
//            }
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
//
//        }
//        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            Log.d(TAG, "LocationManager.NETWORK_PROVIDER");
//            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                mListener.updateLastLocation(location);
//            }
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
//        }

        if (!(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            Toast.makeText(mContext, "请打开网络或GPS定位功能!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

        try {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null){
                Log.d(TAG, "onCreate.location = null");
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            Log.d(TAG, "onCreate.location = " + location);

            if (location != null) {
                mListener.updateLastLocation(location);
            }

            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
        }catch (SecurityException  e){
            e.printStackTrace();
        }
    }

    public void removeLocationUpdatesListener() {
//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    // 定位坐标发生改变
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        if (mListener != null) {
            mListener.updateLocation(location);
        }
    }

    // 定位服务状态改变
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");

        if (mListener != null) {
            mListener.updateStatus(provider, status, extras);
        }
    }

    // 定位服务开启
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    // 定位服务关闭
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }

}
