package com.schiappa.grillonavigante;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.schiappa.grillonavigante.KML.KmlManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class NavigateActivity extends Activity {
	
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
	private static final long POINT_RADIUS = 1000; // in Meters
	private static final long PROX_ALERT_EXPIRATION = -1;
	private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
	private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
	private static final String PROX_ALERT_INTENT = "com.schiappa.ProximityAlert";


	private static final NumberFormat nf = new DecimalFormat("##.########");

	private LocationManager locationManager;
	private List<String> locationProviders;
	private KmlManager kmlmanager;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		locationProviders = locationManager.getProviders(true);

		locationManager.requestLocationUpdates(				
				                        LocationManager.NETWORK_PROVIDER,			
				                        MINIMUM_TIME_BETWEEN_UPDATE,			
				                        MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				                        new NavLocationListener()		
				        );
		
		
		kmlmanager = new KmlManager("map.kml",this);
		
        webView = (WebView)findViewById(R.id.webMapView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new mapNavigateJavaScriptInterface(this), "GrilloNav");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/map.html");
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigate, menu);
		return true;
	}

	

    private void saveProximityAlertPoint() {

        Location location =

            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location==null) {

            Toast.makeText(this, "No last known location. Aborting...",

                Toast.LENGTH_LONG).show();

            return;
    }

        saveCoordinatesInPreferences((float)location.getLatitude(),

               (float)location.getLongitude());

        addProximityAlert(location.getLatitude(), location.getLongitude());

    }


    private void addProximityAlert(double latitude, double longitude) {

         

        Intent intent = new Intent(PROX_ALERT_INTENT);

        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
     

        locationManager.addProximityAlert(

            latitude, // the latitude of the central point of the alert region

            longitude, // the longitude of the central point of the alert region

            POINT_RADIUS, // the radius of the central point of the alert region, in meters

            PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration

            proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected

       );

       IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT); 

       registerReceiver(new ProximityIntentReceiver(), filter);

        

    }

 

    public void populateCoordinatesFromLastKnownLocation(String lat, String lon) {

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location!=null) {

            lat.concat(nf.format(location.getLatitude()));

            lon.concat(nf.format(location.getLongitude()));

        }

    }

    
    
    public Location populateCoordinatesFromLastKnownLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return location;

    }
    
    private void saveCoordinatesInPreferences(float latitude, float longitude) {

        SharedPreferences prefs = this.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);

        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        prefsEditor.commit();
    }

	
	
    private Location retrievelocationFromPreferences() {

    SharedPreferences prefs = this.getSharedPreferences(getClass().getSimpleName(),
    															Context.MODE_PRIVATE);

    Location location = new Location("POINT_LOCATION");


    location.setLongitude(prefs.getFloat(POINT_LONGITUDE_KEY, 0));

    return location;

}
	
    
    public void MapView(){
    	
      //  webView = (WebView)findViewById(R.id.webMapView);
       // webView.getSettings().setJavaScriptEnabled(true);
       // webView.addJavascriptInterface(new mapNavigateJavaScriptInterface(this), "GrilloNav");
        //webView.setWebChromeClient(new WebChromeClient());
    	webView.loadUrl("file:///android_asset/www/gmaps.html");
    }
	
	public class mapNavigateJavaScriptInterface {
		
		Context mnContext ;
		Location loc;
		
		mapNavigateJavaScriptInterface(Context c){
			mnContext = c;		
		}
		
		@JavascriptInterface
		public void trova(){
			
			loc = NavigateActivity.this.populateCoordinatesFromLastKnownLocation();
			
		}
		
		@JavascriptInterface
		public String getLat(){
			
			String lat = nf.format(loc.getLatitude());
			return lat;
			
		}
		
		
		@JavascriptInterface
		public String getLon(){
			
			String lon = nf.format(loc.getLongitude());
			return lon;
			
		}
		
		@JavascriptInterface
		public void salva(){
			
			NavigateActivity.this.saveProximityAlertPoint();
		}
		
		@JavascriptInterface
		public void salvafile(String name, String descr, String Lat, String Lon){
			
			NavigateActivity.this.kmlmanager.addPlacemark(name,descr,Lat,Lon);
			NavigateActivity.this.kmlmanager.SetKmlToFile();
		}
		
		@JavascriptInterface
		public String getKmlFile(){
			
			return NavigateActivity.this.kmlmanager.getFileName();
		}
		
		@JavascriptInterface
		public void map(){
			NavigateActivity.this.MapView();
			
		}
		
	}
	
	
	  public class NavLocationListener implements LocationListener {
		  @Override
		          public void onLocationChanged(Location location) {
		 
		              Location pointLocation = retrievelocationFromPreferences();
		
		              float distance = location.distanceTo(pointLocation);
		  
		              Toast.makeText(NavigateActivity.this,
		  
		                      "Distance from Point:"+distance, Toast.LENGTH_LONG).show();
	
		          }
		 
		          public void onStatusChanged(String s, int i, Bundle b) {           
		  
		          }
		 
		          public void onProviderDisabled(String s) {
		 
		          }
		  
		          public void onProviderEnabled(String s) {           
		 
		          }

				

		  
		      }

	
}
