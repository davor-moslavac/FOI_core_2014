package foi.core.urbanexplorervarazdin;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;

public class PoiInfo {

	public double lat;
	public double lng;
	public String naziv;
	public int poiID;
	public String short_desc;
	public String long_desc;
	public Bitmap[] imageBitmaps;
	public int picNo;
	
	public PoiInfo(PoiInfo temp)
	{
		this.lat = temp.lat;
		this.lng = temp.lng;
		this.naziv = new String(temp.naziv);
		this.poiID = temp.poiID;
		this.short_desc = new String(temp.short_desc);
		this.long_desc = new String(temp.long_desc);
		
		this.imageBitmaps = new Bitmap[10];
		
		this.imageBitmaps = temp.imageBitmaps.clone();
		
		this.picNo = temp.picNo;
		
			
	}
	
	public PoiInfo()
	{
		this.lat = 0;
		this.lng = 0;
		this.naziv = new String("");
		this.poiID = 0;
		this.short_desc = new String("");
		this.long_desc = new String("");
		this.imageBitmaps = new Bitmap[10];
		this.picNo = 0;
	}
	
	public LatLng getLatLng()
	{
		return new LatLng(lat, lng);
	}
	
}

