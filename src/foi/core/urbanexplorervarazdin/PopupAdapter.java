package foi.core.urbanexplorervarazdin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

class PopupAdapter implements InfoWindowAdapter {
  LayoutInflater inflater=null;
  Context context;
  
  private static final String LOG_TAG = "UrbanExplorerVarazdin";

  PopupAdapter(LayoutInflater inflater, Context context) {
    this.inflater=inflater;
    this.context = context;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return(null);
  }

  @Override
  public View getInfoContents(Marker marker) {
    View popup=inflater.inflate(R.layout.popup, null);

    TextView tv=(TextView)popup.findViewById(R.id.title);

    tv.setText(marker.getTitle());
    tv=(TextView)popup.findViewById(R.id.snippet);
    tv.setText(marker.getSnippet());

    String[] str = marker.getSnippet().split(":");
    
    Log.e(LOG_TAG, str[0]);
    
    int id = 0;
    
    try
    {
    	id = Integer.parseInt(str[1]);
    }
    catch (Exception e)
    {
    	 //TODO
    }
    
    
    tv.setText(MainActivity.activ.pois.get(id).naziv);
    tv=(TextView)popup.findViewById(R.id.snippet);
   
    tv.setText(MainActivity.activ.pois.get(id).short_desc);
    
    Log.e(LOG_TAG, str[1]);
   
    
    return(popup);
  }
}