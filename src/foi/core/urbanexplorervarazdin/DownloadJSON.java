package foi.core.urbanexplorervarazdin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.Toast;


/** A class to download data from Google Directions URL */
public class DownloadJSON extends AsyncTask<String, Void, String>{
	

    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try{
            // Fetching the data from web service
            data = downloadUrl(url[0]);
        }catch(Exception e){
            //Log.d("Background Task",e.toString());
            Toast.makeText(MainActivity.activ.getApplicationContext(), "Problem s preuzimanjem podataka... provjerite radi li vam veza na Internet.", Toast.LENGTH_SHORT).show();
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParseJSON parserTask = new ParseJSON();

        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);
    }
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
    	//Testing.test(strUrl);
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            //Log.d("Problem kod preuzimanja: ", e.toString());
            Toast.makeText(MainActivity.activ.getApplicationContext(), "Problem s preuzimanjem podataka... provjerite radi li vam veza na Internet.", Toast.LENGTH_SHORT).show();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
