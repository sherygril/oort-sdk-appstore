package com.oortcloud.basemodule.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
  *
  * 定位
  */

public class LocationUtil {

    private static OnResponseListener responseListener;
    private volatile static LocationUtil locationUtil;

    private LocationManager locationManager;
    private android.location.Location currentBestLocation;
    private NetworkListener networkListener;
    private GPSLocationListener gpsListener;
    private LocationListener locationListener;
    private LocationUtil() {
        networkListener = new NetworkListener();
        gpsListener = new GPSLocationListener();
    }


    public static LocationUtil getInstance() {
        if (locationUtil == null) {
            synchronized (LocationUtil.class) {
                if (locationUtil == null) {
                    locationUtil = new LocationUtil();
                }
            }
        }
        return locationUtil;

    }
    /**
      * 定位模式
      */
    public enum Mode{
        NETWORK,  //网络定位
        GPS,  //GPS定位
        AUTO  //自动定位,使用网络或GPS定位
    }


    public static final int NO_PERMISSION = 1;   //没权限
    public static final int GPS_CLOSE = 2;   //GPS是关闭的
    public static final int UNAVAILABLE = 3;   //不可用

    /**
      * 请求定位
      */
    public static void requestLocation(Context context, Mode mode, OnResponseListener onResponseListener) {
        LocationUtil.responseListener = onResponseListener;
        if (PermissionUtil.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION,1) ||
                PermissionUtil.checkPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION,2)){
            Log.i("gps","获取定位权限,开始定位");
//开始定位
            locationUtil = getInstance();
            locationUtil.startLocation(context,mode);
        } else {
            Log.i("gps","没有定位权限,定位失败");
            String provider = mode == Mode.GPS ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
            onResponseListener.onErrorResponse(provider, NO_PERMISSION);
        }
    }

    long l;

    /**
      * 开始定位
      */
    private void startLocation(Context context, Mode mode) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(true);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        // 获取最好的定位方式
        String provider = locationManager.getBestProvider(criteria, true); // true 代表从打开的设备中查找
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            locationListener = networkListener;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            locationListener = gpsListener;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(context, "请开启GPS定位服务", Toast.LENGTH_LONG).show();
            return;
        }


        if (PermissionUtil.checkPermission(context,Manifest.permission.ACCESS_FINE_LOCATION,3) ||
                PermissionUtil.checkPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION,4)){
            //获取上次的location
            Location location = locationManager.getLastKnownLocation(provider);
            /*while(location == null)
            {
                location = locationManager.getLastKnownLocation(provider);
            }*/
            if (location == null){
                location = getLastKnownLocation(locationManager);
            }
            locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);

            switch (mode){
                case NETWORK:
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                        Log.i("gps","网络定位");

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0.1f, networkListener);
                    } else {
                        responseListener.onErrorResponse(LocationManager.NETWORK_PROVIDER, UNAVAILABLE);
                    }
                    break;
                case GPS:
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Log.i("gps","GPS定位");

                        l = System.currentTimeMillis();
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 0.1f, gpsListener);
                    } else {
                        responseListener.onErrorResponse(LocationManager.GPS_PROVIDER, GPS_CLOSE);
                    }

                    break;
                case AUTO:
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                        Log.i("gps","自动定位选择网络定位");

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0.1f, networkListener);
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Log.i("gps","自动定位选择gps");

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 0.1f, gpsListener);
                    } else {
                        responseListener.onErrorResponse(LocationManager.NETWORK_PROVIDER, UNAVAILABLE);
                    }

                    break;
                default:

                    break;
            }
        } else {
            Log.i("gps","无权限");
        }


    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation(LocationManager lr) {
//        LocationManager mLocationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        List<String> providers = lr.getAllProviders();
        Location bestLocation = null;
        for (String provider : providers) {
//            if (PermissionUtil.checkPermission(context,Manifest.permission.ACCESS_FINE_LOCATION,3) ||
//                    PermissionUtil.checkPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION,4)) {
                Location l = lr.getLastKnownLocation(provider);

                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
//            }else {
//                Log.e("gps","无权限");
//            }
        }
        return bestLocation;
    }
    /**
      * 停止定位
      */
    public static void stopLocation(){
        if (locationUtil == null){
            Log.i("gps","locationUtil is null");
            return;
        }
        if (locationUtil.networkListener != null){
            locationUtil.locationManager.removeUpdates(locationUtil.networkListener);
        }
        if (locationUtil.gpsListener != null){
            locationUtil.locationManager.removeUpdates(locationUtil.gpsListener);
        }
        Log.i("gps","停止定位");
    }

    private class GPSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("gps","onLocationChanged");
            if (location != null) {
                Log.i("gps","GPS定位成功");
                Log.i("gps","GPS定位耗时:" + ((System.currentTimeMillis() - l) / 1000) + "秒");
                boolean isBetter = isBetterLocation(location, currentBestLocation);
                if (isBetter) {
                    currentBestLocation = location;
                }
                double latitude = currentBestLocation.getLatitude();   //纬度
                double longitude = currentBestLocation.getLongitude();   //经度
                responseListener.onSuccessResponse(latitude, longitude);
            } else {
                Log.i("gps","location is null");
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("gps","onStatusChanged:" + status);
            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                Log.i("gps","GPS定位失败");
//如果之前没有开启过网络定位,自动切换到网络定位
                if (networkListener == null){
//开启网络定位
                    networkListener = new NetworkListener();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("gps","onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("gps","onProviderDisabled");
        }
    }

    private class NetworkListener implements LocationListener {

        @Override
        public void onLocationChanged(android.location.Location location) {
            if (location != null){
                Log.i("gps","网络定位成功");

                boolean isBetter = isBetterLocation(location,currentBestLocation);
                if (isBetter){
                    currentBestLocation = location;
                }
                double latitude = currentBestLocation.getLatitude();   //纬度
                double longitude = currentBestLocation.getLongitude();   //经度
                responseListener.onSuccessResponse(latitude, longitude);

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE ) {
                Log.i("gps","网络定位失败");
                responseListener.onErrorResponse(LocationManager.NETWORK_PROVIDER, UNAVAILABLE);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("gps","可用");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("gps","不可用");
        }
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
      * 比较最新获取到的位置是否比当前最好的位置更好
      *
      * @param location  最新获得的位置
      * @param currentBestLocation 当前获取到的最好的位置
      * @return  最新获取的位置比当前最好的位置更好则返回true
      */
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
// A new locationUtil is always better than no locationUtil
            return true;
        }

//实时性
// Check whether the new locationUtil fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES; //最新位置比当前位置晚两分钟定位
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;//最新位置比当前位置早两分钟定位
        boolean isNewer = timeDelta > 0;

// If it's been more than two minutes since the current locationUtil, use the new locationUtil
// because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
// If the new locationUtil is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

//精确性
// Check whether the new locationUtil fix is more or less accurate
//locationUtil.getAccuracy()值越小越精确
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0; //最新的位置不如当前的精确
        boolean isMoreAccurate = accuracyDelta < 0; //最新的位置比当前的精确
//最新的位置不如当前的精确，但是相差在一定范围之内
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

// Check if the old and new locationUtil are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

// Determine locationUtil quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public interface OnResponseListener {

        /**
          * 定位成功
          * @param latitude  纬度
          * @param longitude 经度
          */
        void onSuccessResponse(double latitude, double longitude);

        /**
          * 定位失败
          * @param provider  provider
          * @param status  失败码
          */
        void onErrorResponse(String provider, int status);
    }

    /**
      * 获取地址
      * @param context Context
      * @param latitude  纬度
      * @param longitude 经度
      * @return  Address
      */
    public static @Nullable Address getAddress(Context context, double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> list = geocoder.getFromLocation(latitude,longitude,3);
            if (list.size() > 0){
                Address address = list.get(0);
                Log.i("gps","省:" + address.getAdminArea());
                Log.i("gps","市:" + address.getLocality());
                Log.i("gps","县/区:" + address.getSubLocality());
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}