package foi.core.urbanexplorervarazdin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowInfo extends Activity {
	
	public static ShowInfo showContext;
	
	public static int id; 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		ShowInfo.id = intent.getIntExtra("foi.core.urbanexplorervarazdin.ID", 0);
		
		setContentView(R.layout.info);
		ShowInfo.showContext = this;
		
		TextView poiNaslov = (TextView) findViewById(R.id.textViewNaslov);
		TextView descShort = (TextView) findViewById(R.id.textViewDescShort);
		TextView descExpand = (TextView) findViewById(R.id.textViewDescExpand);
		TextView udaljenost = (TextView) findViewById(R.id.textUdaljenost);
		
		
		poiNaslov.setText(MainActivity.activ.pois.get(ShowInfo.id).naziv);
		descShort.setText(MainActivity.activ.pois.get(ShowInfo.id).short_desc);
		descExpand.setText(MainActivity.activ.pois.get(ShowInfo.id).long_desc);
		
		double dist = (6371 * Math.acos( Math.cos( Math.toRadians(MainActivity.activ.pois.get(id).lat) ) * Math.cos( Math.toRadians( MainActivity.gpsTrack.latitude ) ) * Math.cos( Math.toRadians( MainActivity.gpsTrack.longitude ) - Math.toRadians(MainActivity.activ.pois.get(id).lng) ) + Math.sin( Math.toRadians(MainActivity.activ.pois.get(id).lat) ) * Math.sin( Math.toRadians( MainActivity.gpsTrack.latitude ) ) ) );
		
		udaljenost.setText("Udaljenost: " + Double.toString(dist * 1000) + " m");
		
		
		Button izlaz = (Button) findViewById(R.id.odustani);
		izlaz.setOnClickListener(new OnClickListener()
			{
			@Override
			public void onClick(View v) {
				ShowInfo.showContext.finish();
			}
			});
		
		Button slike = (Button) findViewById(R.id.prikaziSlike);
		
		slike.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Intent intent = new Intent(MainActivity.activ, foi.core.urbanexplorervarazdin.PictureGallery.class);
				intent.putExtra("foi.core.urbanexplorervarazdin.PicLocID", ShowInfo.id);
				MainActivity.activ.startActivity(intent);
				return false;
			}
		});
		
		
	}
}
