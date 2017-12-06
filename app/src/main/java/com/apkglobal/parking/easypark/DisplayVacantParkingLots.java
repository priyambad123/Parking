
package com.apkglobal.parking.easypark;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListAdapter;

import android.widget.TextView;

import android.app.ProgressDialog;


import com.apkglobal.parking.R;
import com.apkglobal.parking.helpers.Constants;
import com.apkglobal.parking.helpers.IconGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class DisplayVacantParkingLots extends FragmentActivity implements OnMapReadyCallback {
	
	public static boolean isRadius;
	GoogleMap googleMap;
	String TAG_PARKINGLOTS = "parkinglots";
	
	// To pass the arraylist to switch view 
	public static final String ARRAYLISTMAP = "com.apkglobal.easypark.DisplayVacantParkingLots.ARRAYLISTMAP";

	public  ArrayList<HashMap<String, String>> parkingLotsMapList = new ArrayList<HashMap<String, String>>();
	
	TextView AvailableLotAddress;
	
    private ProgressDialog pDialog;
    
    Button btn_switchToListView;

	public static long fromTime;
	public static long toTime;
	 int success;
	 String returnString;
	static final String getParkingLotsurl =  Constants.IPAddress +"/searchkero.com/nearbyparking/getparkinglotsdata.php";
	    public   double radius;
		public long zipcode;
		ListAdapter adapter;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewlistvacantparkinglots);
		
		Intent displayIntent = getIntent(); 
		
		double usersCurrentLatitude = displayIntent.getDoubleExtra(GetParkingLots.LATITUDE,Constants.doubleDefaultValue); 
		double usersCurrentLongitude = displayIntent.getDoubleExtra(GetParkingLots.LONGITUDE, Constants.doubleDefaultValue);
		 fromTime = displayIntent.getLongExtra(GetParkingLots.FROMTIME, 0);
		 toTime = displayIntent.getLongExtra(GetParkingLots.TOTIME, 0);
		 
		 isRadius = displayIntent.getBooleanExtra(GetParkingLots.RadiusOrZIPCODE, false);
		 if (isRadius)
		 {
		 
		 radius = displayIntent.getDoubleExtra(GetParkingLots.RADIUS, 0.0);
		 }
		 else
		 {
			 
			 zipcode = displayIntent.getLongExtra(GetParkingLots.ZIPCODE, 0);
		 }
		 
	    Log.i("DisplayVacantParkingLots","Before Calling Async task");
		new GetParkingLotsFromWebService(this, usersCurrentLatitude, usersCurrentLongitude, fromTime, toTime).execute();
		Log.i("DisplayVacantParkingLots","After Calling Async task");
		btn_switchToListView = (Button) findViewById(R.id.switchtolistview);
		btn_switchToListView.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if (parkingLotsMapList != null) // fixed crashing of application when no parking data  found
					{
					Intent intent = new Intent(DisplayVacantParkingLots.this, DisplayParkingLotsAsList.class);
					intent.putExtra(ARRAYLISTMAP, parkingLotsMapList);
					startActivity(intent);
					}
				}
				});
		
	}

	
	
	@SuppressWarnings("deprecation")
	public void updatemap(ArrayList<HashMap<String, String>> alofHashmap , int Success)
	{
	
		if (success == 1)
		{
			try
			{
				SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map);
				mapFragment.getMapAsync(this);
		  
		  googleMap.clear();
	        for (int i = (alofHashmap.size() - 1); i >= 0; i--)
	        {
	        LatLng latLng = new LatLng(Double.parseDouble(alofHashmap.get(i).get("latitude")), Double.parseDouble(alofHashmap.get(i).get("longitude")));

				IconGenerator tc = new IconGenerator(this);
				Bitmap bmp = tc.makeIcon("costForParking");
	        googleMap.addMarker(new MarkerOptions()
			.position(latLng)

			.icon(BitmapDescriptorFactory.fromBitmap(bmp))).showInfoWindow();
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	        }
	        
	        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    LatLng markerLatLng = arg0.getPosition();
                    Log.i("DisplayVacantParkingLots","Value of the marker that was clicked is" +markerLatLng.toString());
                    for (int i = (parkingLotsMapList.size() - 1); i >=0 ; i--)
                    {
                    	Log.i("DisplayVacantParkingLots", "value in the parkinglots map inside for loop" +parkingLotsMapList.get(i).toString());
                       if   (parkingLotsMapList.get(i).containsValue(String.valueOf(markerLatLng.latitude)) && parkingLotsMapList.get(i).containsValue(String.valueOf(markerLatLng.longitude)))
                       {
                    	   Intent intent = new Intent (DisplayVacantParkingLots.this, GetIndividualParkingSpotDetails.class);
                    	   intent.putExtra("individualParkingLotId", parkingLotsMapList.get(i).get("vacantParkingLotId"));
                    	   Log.i("DisplayVacantParkingLots","The value of the parkinglot of the marker clicked is" +parkingLotsMapList.get(i).get("vacantParkingLotId"));
                    	   intent.putExtra(GetParkingLots.FROMTIME, DisplayVacantParkingLots.fromTime);
                    	   intent.putExtra(GetParkingLots.TOTIME, DisplayVacantParkingLots.toTime);
                    	   startActivity(intent); 
                       }
                    }
                    return true;
                }

            });  
	        }
			catch (Exception e)
			{
			
			}
			finally
			{
				
			}
		}
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
                    DisplayVacantParkingLots.this).create();

   
    alertDialog.setTitle("Sorry!");

 
    alertDialog.setMessage("No parking lots found");



   
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to execute after dialog closed
            	Intent intent = new Intent(DisplayVacantParkingLots.this, GetParkingLots.class);
	            startActivity(intent);
            }
    });


    alertDialog.show();

		}
	}
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
		}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		LatLng juet = new LatLng(24.4349, 77.1612);
		googleMap.addMarker(new MarkerOptions().position(juet).title("JUET"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(juet));
		googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(24.4349, 77.1612) , 14.0f) );
	}
	
	class GetParkingLotsFromWebService extends AsyncTask<String, String, String>
	{
		DisplayVacantParkingLots dvpl;
		String latitude, longitude;
		String fromTime, toTime, radius, zipcode;
		double blockedTimeInHours;
		
		 /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayVacantParkingLots.this);
            pDialog.setMessage("Retreiving ParkingLots..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        GetParkingLotsFromWebService(DisplayVacantParkingLots dvpl, double latitude, double longitude, long fromTime, long toTime )
        {
        	
        	this.latitude =  String.valueOf(latitude);
        	this.longitude = String.valueOf(longitude);
        	this.fromTime = String.valueOf(fromTime);
        	this.toTime = String.valueOf(toTime);
        	this.radius = String.valueOf(DisplayVacantParkingLots.this.radius);
        	this.zipcode = String.valueOf(DisplayVacantParkingLots.this.zipcode);
        	this.blockedTimeInHours = ((toTime - fromTime)/ Constants.millisecondsIntoHours);
        	Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","blockedTimeInHours" +blockedTimeInHours);
        	this.dvpl = dvpl;
        	}

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair("latitude",latitude ));
			postParams.add(new BasicNameValuePair("longitude", longitude));
			postParams.add(new BasicNameValuePair("isradius", String.valueOf(isRadius)));
			postParams.add(new BasicNameValuePair("radius", radius));
			postParams.add(new BasicNameValuePair("zipcode", zipcode));
			postParams.add(new BasicNameValuePair("fromtime", fromTime));
			postParams.add(new BasicNameValuePair("endtime", toTime));
            
            String response = null;
            Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","the postparams are" + postParams);

            // call executeHttpPost method passing necessary parameters 
            try {
       response = EasyParkHttpClient.executeHttpPost(getParkingLotsurl, postParams);
       Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","after making request jsonobject is" +response);
                
        //parse json data
           try{
        	  String result = response.toString();
                  returnString = "";
                  JSONObject jsonOb = new JSONObject(result);
                  success = jsonOb.getInt(Constants.TAG_SUCCESS);
                  if( success == 1)
                  {
             JSONArray jArray =  jsonOb.getJSONArray(TAG_PARKINGLOTS);
             Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","after converting to obj" +jsonOb);

                   for(int i=0;i<jArray.length();i++){
                          JSONObject json_data = jArray.getJSONObject(i);
                           Log.i("log_tag","parkinglotsid: "+json_data.getInt("parkinglotsid")+
                                   ", address: "+json_data.getString("address")+
                                   ", miles: "+json_data.getString("miles")+
                                   ", cost: "+json_data.getString("cost")
                           );
                       
                           HashMap<String, String> parkingLotsMap = new HashMap<String, String>();
               			
               			parkingLotsMap.put("vacantParkingLotId",json_data.getString("parkinglotsid"));
               			parkingLotsMap.put("lotsInfoTextView", json_data.getString("address"));
               			Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService", "Inside hash map blockedtimeInHours"+blockedTimeInHours);
               			
               			double costForparking = (Double.parseDouble(json_data.getString("cost")) * blockedTimeInHours );
               			Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService", "Inside hash map costForparking"+costForparking);
               			parkingLotsMap.put("costForParking",(new DecimalFormat("##.##").format(costForparking)+ "$"));
               			
               			double dist = Double.parseDouble(json_data.getString("miles"));
               			parkingLotsMap.put("distanceInMiles",(new DecimalFormat("##.##").format(dist) + "miles"));
               			
               			parkingLotsMap.put("latitude", json_data.getString("latitude"));
               			parkingLotsMap.put("longitude", json_data.getString("longitude"));
               			
               			parkingLotsMapList.add(parkingLotsMap);
            			
                   }
                  }
                  
           }
           catch(JSONException e){
                   Log.e("log_tag", "Error parsing data "+e.toString());
           }
			
		}
            
            catch(Exception e)
            {
                Log.e("log_tag","Error in http connection!!" + e.toString());     

            }
            return null;
		}
		
		  /**
         * After completing background task Dismiss the progress dialog and use the UI thread to update the UI
         * **/
        protected void onPostExecute(String file_url) {
            
            pDialog.dismiss();
            dvpl.updatemap(parkingLotsMapList, success);
 
        }
	}

	
	}
