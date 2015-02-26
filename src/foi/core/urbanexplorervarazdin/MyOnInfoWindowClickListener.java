package foi.core.urbanexplorervarazdin;

import android.content.Intent;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;

public class MyOnInfoWindowClickListener implements OnInfoWindowClickListener
{

@Override
public void onInfoWindowClick(Marker arg0) {
	Intent intent = new Intent(MainActivity.activ, foi.core.urbanexplorervarazdin.ShowInfo.class);
	String[] str = arg0.getSnippet().split(":");
	intent.putExtra("foi.core.urbanexplorervarazdin.ID", Integer.parseInt(str[1]));
	MainActivity.activ.startActivity(intent);
	
}

};