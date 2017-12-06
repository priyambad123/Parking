package com.apkglobal.parking.easypark;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.apkglobal.parking.R;
import com.apkglobal.parking.helpers.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleDirectionsActivity extends FragmentActivity implements LocationListener {

    public Location location = null;
    LocationManager locationManager;
    GoogleMap mMap;
    GetDirections md;
    String provider;
    double fromlat, fromlng;
    double tolat, tolng;
    LatLng fromPosition;
    LatLng toPosition;
    TextView tv_duration;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdirections);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main2 thread
                .penaltyLog().build());
        Log.i("GoogleDirectionsActivity", "Inside Oncreate");

        Intent theIntent = getIntent();
        tolat = theIntent.getDoubleExtra(GetParkingLots.LATITUDE, Constants.doubleDefaultValue);
        tolng = theIntent.getDoubleExtra(GetParkingLots.LONGITUDE, Constants.doubleDefaultValue);

        toPosition = new LatLng(tolat, tolng);


        Log.i("GoogleDirectionsActivity", "After Setting tolat, tolng, toPosition" + tolat + "\n" + tolng + "\n" + toPosition);

        md = new GetDirections();
        Log.i("GoogleDirectionsActivity", "After calling GetDirctions constructor");

        FragmentManager fm = getSupportFragmentManager();
        Log.i("GoogleDirectionsActivity", "After creating fragmentManager" + fm);
        SupportMapFragment supportMapfragment = ((SupportMapFragment) fm.findFragmentById(R.id.drivingdirections));

        Log.i("GoogleDirectionsActivity", "After creating SupportMapFragment" + supportMapfragment);
        ((MapFragment) getFragmentManager().findFragmentById(
                R.id.drivingdirections)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (ActivityCompat.checkSelfPermission(GoogleDirectionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleDirectionsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap=googleMap;
                googleMap.setMyLocationEnabled(true);
                fromPosition = new LatLng(fromlat, fromlng);
                LatLng coordinates = new LatLng(fromlat, fromlng);


                Document doc = md.getDocument(fromPosition, toPosition, Constants.MODE_DRIVING);
                String duration = md.getDurationText(doc);

                tv_duration = (TextView) findViewById(R.id.tv_time);
                tv_duration.setText("Estimated driving time:" +duration);

                ArrayList<LatLng> directionPoint = md.getDirection(doc);

                PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.RED);

                for(int i = 0 ; i < directionPoint.size() ; i++) {
                    rectLine.add(directionPoint.get(i));
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));

                googleMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
                googleMap.addMarker(new MarkerOptions().position(toPosition).title("End"));
                googleMap.addPolyline(rectLine);

            }

        });
        Log.i("GoogleDirectionsActivity", "After getting map");


        //mMap.setMyLocationEnabled(true);
        Log.i("GoogleDirectionsActivity","After  setMyLocationEnabled");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.i("GoogleDirectionsActivity","After  locationManager");
        Criteria criteria = new Criteria();



	   // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, true);


        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
             location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 500, 1, GoogleDirectionsActivity.this);


            if(location!=null)
            {

            	fromlat = location.getLatitude();
            	fromlng = location.getLongitude();
            }

            else
            {
            	fromlat = GetParkingLots.latitude;
            	fromlng = GetParkingLots.longitude;
            }

        }else{
        	Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        	finish();
        }
        Log.e("GoogleDirectionsActivity","After  setting location");



    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}



}
