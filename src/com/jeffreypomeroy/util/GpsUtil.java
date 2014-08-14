package com.jeffreypomeroy.util;

import java.util.Random;
 
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
 
public class GpsUtil extends FragmentActivity implements LocationListener {
   
  private LocationManager locationManager;
  private LocationListener locationListener = new DummyLocationListener();
  private GpsListener gpsListener = new GpsListener();
  private Location location;
  private GpsStatus gpsStatus;
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_main);
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    gpsStatus = locationManager.getGpsStatus(null);
    locationManager.addGpsStatusListener(gpsListener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2*1000, 0, locationListener);
  }
    
  public void getSatData(){
    Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
     
    for(GpsSatellite sat : sats){
      StringBuilder sb = new StringBuilder();
      sb.append(sat.getPrn());
      sb.append("\t");
      sb.append(sat.getElevation());
      sb.append("\t");
      sb.append(sat.getAzimuth());
      sb.append("\t");
      sb.append(sat.getSnr());
  
    }
     
    gpsStatus = locationManager.getGpsStatus(gpsStatus);
  }
 
  protected void onResume() {
     super.onResume();
  }
   
  @Override
  public void onLocationChanged(Location location){ }
  @Override
  public void onProviderDisabled(String provider) { }
  @Override
  public void onProviderEnabled(String provider) { }
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) { }
   
  class GpsListener implements GpsStatus.Listener{
      @Override
      public void onGpsStatusChanged(int event) {
        getSatData();
      }
  }
   
  class DummyLocationListener implements LocationListener {
    //Empty class just to ease instatiation
      @Override
      public void onLocationChanged(Location location) { }
      @Override
      public void onProviderDisabled(String provider) { }
      @Override
      public void onProviderEnabled(String provider) { }
      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) { }
  }
}