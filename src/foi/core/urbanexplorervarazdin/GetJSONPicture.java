package foi.core.urbanexplorervarazdin;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;


public class GetJSONPicture extends AsyncTask<String, Void, String> {
	
	MainActivity activ;
	
	public GetJSONPicture(MainActivity activ)
	{
		this.activ = activ;		
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
	        
		        for (int i = 0; i < jsonArray.length(); i++) {
		        	
		            JSONObject jsonObj = jsonArray.getJSONObject(i);
		            
		            PoiInfo temp = MainActivity.activ.pois.get(jsonObj.getInt("idLocation") - 1);
		            temp.picNo++;
		            
		            String picture = jsonObj.getString("image");
		            //Bitmap tempBit = BitmapFactory.decodeByteArray(Base64.decode(picture, Base64.DEFAULT), 0, Base64.decode(picture, Base64.DEFAULT).length);
		            Bitmap tempBit = BitmapFactory.decodeStream(new ByteArrayInputStream(picture.getBytes()));
		            
		            if(temp.picNo < 10)
		            	temp.imageBitmaps[temp.picNo-1] = tempBit;
		            	
	            	
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

