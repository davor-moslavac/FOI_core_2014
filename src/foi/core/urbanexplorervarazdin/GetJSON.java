package foi.core.urbanexplorervarazdin;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GetJSON extends AsyncTask<String, Void, String> {
	
	GoogleMap googleMap;
	MainActivity activ;
	
	double mLatitude=0;
	double mLongitude=0;
	
	
	public GetJSON(MainActivity activ)
	{
		this.activ = activ;
		this.googleMap = activ.googleMap;
		
	}
	
	//private static final String LOG_TAG = "UrbanExplorerVarazdin";
	
	@Override
	protected String doInBackground(String... params) {
	
		String result = "";
		
		String url = params[0];
		HttpClient httpclient = new DefaultHttpClient();
        HttpGet request;
        try {
        	String strUrl = url;
        	request = new HttpGet(strUrl);
        	
        }
        catch (Exception e)
        {
        	return null;
        }
        ResponseHandler<String> handler = new BasicResponseHandler();
        try {
        	result = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        } catch(IOException e) {
        	e.printStackTrace();
        }
        
        httpclient.getConnectionManager().shutdown();
        return result;
	}
	
	
	@Override
    protected void onPostExecute(String result) {
		try{
			
	        JSONArray jsonArray = new JSONArray(result);
	        activ.pois.clear();
	        googleMap.clear();
	        
		        for (int i = 0; i < jsonArray.length(); i++) {
		        	PoiInfo temp = new PoiInfo();
		            JSONObject jsonObj = jsonArray.getJSONObject(i);
		            temp.lat = jsonObj.getDouble("latitude");
		            temp.lng = jsonObj.getDouble("longitude");
		            temp.naziv = jsonObj.getString("name");
		            temp.poiID = jsonObj.getInt("id");
		            temp.short_desc = jsonObj.getString("desc_short");
		            temp.long_desc = jsonObj.getString("desc_expand");
		            
		            //String tempThumbPic = jsonObj.getString("thumb");
		            //temp.thumb = BitmapFactory.decodeByteArray(Base64.decode(tempThumbPic, 0), 0, Base64.decode(tempThumbPic, 0).length);
		            
		            	Marker mark = googleMap.addMarker(new MarkerOptions()
		                .title(temp.naziv)
		                .snippet(temp.naziv + ":" + i)
		                .position(temp.getLatLng())
		                 );
		            	
		            	
		            	
		            	activ.markerList.add(mark);
		            
	            	activ.pois.add(temp);
	            	activ.destinacije.add(temp.naziv);
	            	
		        }

		        Toast.makeText(activ.getApplicationContext(),
	                    "Podaci obraðeni.", Toast.LENGTH_SHORT)
	                    .show();
		}
        catch (Exception e)
        {
        	
        	
        	Toast.makeText(activ.getApplicationContext(),
                    "Problem s preuzimanjem podataka. Imate li pristup internetu?", Toast.LENGTH_SHORT)
                    .show();
        	
        	
        }
        /*
        Toast.makeText(getApplicationContext(),
        		"Refreshed", Toast.LENGTH_SHORT)
                .show();
        */
    }
}

