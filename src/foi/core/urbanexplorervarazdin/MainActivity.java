package foi.core.urbanexplorervarazdin;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem; 
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String SERVICE_URL = "http://82.196.0.145/foicore/index.php?action=list&entity=location&REST=json";
	private static final String PIC_URL = "http://82.196.0.145/foicore/index.php?action=list&entity=image&REST=json";
	
	public GoogleMap googleMap;
	public static GPSTracker gpsTrack;
	
	public ArrayList<String> destinacije;
	
	private Spinner destinacijeSpinner;
	
	private Marker lastOpenned = null;
	
	public static MainActivity activ;
	
	public ArrayList<PoiInfo> pois;
	
	public ArrayList<Marker> markerList;
	
	public Date start;
	
	Polyline tempPoly;
	Polyline ruta;
    PolylineOptions rutaOpcije;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		destinacije = new ArrayList<String>();
		
		markerList = new ArrayList<Marker>();
		
		destinacije.add("Destinacije");
		
		this.destinacijeSpinner = (Spinner) findViewById(R.id.destinacije);
		
		ArrayAdapter<String> dataAdapterDestinacije = new ArrayAdapter<String>(this, R.layout.spinner_layout, this.destinacije);
		
		destinacijeSpinner.setAdapter(dataAdapterDestinacije);
		
		this.pois = new ArrayList<PoiInfo>();
		
		// kako doæi do vrijednosti varijabli odavde - posebice pois!
		MainActivity.activ = this;
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        
        gpsTrack = new GPSTracker(this);
        if(!gpsTrack.isGPSEnabled)
        	gpsTrack.showSettingsAlert();
        gpsTrack.getLocation();

		
        initializeMap();
        
        new GetJSON(this).execute(SERVICE_URL, Double.toString(gpsTrack.latitude), Double.toString(gpsTrack.longitude));
        new GetJSONPicture(this).execute(PIC_URL, Double.toString(gpsTrack.latitude), Double.toString(gpsTrack.longitude));
        
        try
        {
        	googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(), this));
        	googleMap.setOnInfoWindowClickListener(new MyOnInfoWindowClickListener());
        }
        catch(Exception e3)
    	{
        	Toast.makeText(this, "Google Maps problem. Provjerite nadogradnje Google Play servisa.", Toast.LENGTH_SHORT).show();
    	}
        
        Button qr = (Button) findViewById(R.id.skenirajQR);
        
        qr.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
				return false;
			}
		});
        
        Button igraj = (Button) findViewById(R.id.igrajIgru);
        
        igraj.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				for(int i = 1; i < MainActivity.activ.markerList.size(); i++)
				{
					MainActivity.activ.markerList.get(i).setVisible(false);
				}
				MainActivity.activ.start = new Date();
				return false;
			}
		});
        
        destinacijeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(id != 0)
				{
					Marker marker = activ.markerList.get((int) (id - 1));
					marker.showInfoWindow();
					nacrtajRutu(marker.getPosition().latitude, marker.getPosition().longitude);
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(activ.pois.get((int)(id-1)).getLatLng(), (float) 12.5));
					//googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
					
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try
		{
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         
		         if(contents.equalsIgnoreCase("next poi"))
		         {
		        	 //for(Marker mark : MainActivity.activ.markerList)
		        	 for(int i = 0; i < MainActivity.activ.markerList.size(); i++)
		        	 {
		        		 if(!markerList.get(i).isVisible())
		        		 {
		        			 markerList.get(i).setVisible(true);
		        			 markerList.get(i-1).showInfoWindow();
		        			 return;
		        		 }
		        		 
		        		 Toast.makeText(MainActivity.activ, "Igrali ste ukupno: " + ((new Date().getTime() - MainActivity.activ.start.getTime())/1000)/60/60 + " minuta", Toast.LENGTH_LONG).show();
		        	 }
		         }
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		      }
		   }
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
		}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Problem kreiranja mape!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if(googleMap!=null)
        {
        	googleMap.setMyLocationEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if(gpsTrack.latitude != 0 && gpsTrack.latitude != 0)
            	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTrack.latitude, gpsTrack.longitude), 13));
            
            googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				
				@Override
				public void onCameraChange(CameraPosition position)
				{
					//Ako je potrebno refreshati POI-e ovisno o pomicanju karte
					//LatLng pos = googleMap.getCameraPosition().target;
					
				}
			});
            
            googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            	public boolean onMarkerClick(Marker marker) {
            	    // Check if there is an open info window
            	    if (lastOpenned != null) {
            	        // Close the info window
            	        lastOpenned.hideInfoWindow();

            	        // Is the marker the same marker that was already open
            	        if (lastOpenned.equals(marker)) {
            	            // Nullify the lastOpenned object
            	            lastOpenned = null;
            	            // Return so that the info window isn't openned again
            	            return true;
            	        } 
            	    }

            	    // Open the info window for the marker
            	    marker.showInfoWindow();
            	    nacrtajRutu(marker.getPosition().latitude, marker.getPosition().longitude);
            	    // Re-assign the last openned such that we can close it later
            	    lastOpenned = marker;

            	    // Event was handled by our code do not launch default behaviour.
            	    return true;
            	}
            	});
        }
    }
	
	public void nacrtajRutu(double lat, double lng)
    {
    	LatLng origin = new LatLng(gpsTrack.latitude, gpsTrack.longitude);
    	
    	//Log.e(LOG_TAG, "najjeft_naziv: "+najjeft_naziv+" najjeft_benz_lat: " + najjeft_benz_lat + " najjeft_benz_lng: "+najjeft_benz_lng);
    	if(gpsTrack.latitude != 0.0 && gpsTrack.longitude != 0.0)
	    	if(lat!=0.0 && lng!=0.0)
	        {
	    		if(tempPoly != null)
					tempPoly.remove();
	    		
	    		//Log.e(LOG_TAG, "Temp");
	    		//Log.e(LOG_TAG,"location: "+location.toString());
	        	if (gpsTrack.canGetLocation())
	        	{
	        		//Log.e(LOG_TAG, "location nije null");
	        		if(ruta!=null)
	        		{
	        			ruta.remove();
	        			ruta = null;
	        		}
	        		
	        		LatLng dest = new LatLng(lat, lng);
	        		
	                // Getting URL to the Google Directions API
	                String url = getDirectionsUrl(origin, dest);
	
	                DownloadJSON downloadTask = new DownloadJSON();
	
	                // Start downloading json data from Google Directions API
	                downloadTask.execute(url);
	        		
	        	}
	        }
    }
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
   	 
    	String str_origin = "origin="+origin.latitude+","+origin.longitude;
       	String str_dest = "destination="+dest.latitude+","+dest.longitude;
       	String sensor = "sensor=false";
       	String parameters = str_origin+"&"+str_dest+"&"+sensor + "&mode=walking";
       	String output = "json";
       	String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }

}
