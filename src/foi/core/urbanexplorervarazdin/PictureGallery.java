package foi.core.urbanexplorervarazdin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PictureGallery extends Activity {
	
	private Gallery picGallery;
	private ImageView picView;
	private PicAdapter imgAdapt;
	
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		
		Intent intent = getIntent();
		this.id = intent.getIntExtra("foi.core.urbanexplorervarazdin.ID", 0);
		
		picView = (ImageView) findViewById(R.id.picture);
		         
		picGallery = (Gallery) findViewById(R.id.gallery);
		
		imgAdapt = new PicAdapter(this, this.id);
		 
		picGallery.setAdapter(imgAdapt);
		
	}
	
	
	
	
	public class PicAdapter extends BaseAdapter {
		
		int defaultItemBackground;
		         
		private Context galleryContext;
		 
		private Bitmap[] imageBitmaps;
		 
		Bitmap placeholder;
		
		public PicAdapter(Context c, int id) {
			 
		    galleryContext = c;
		 
		    imageBitmaps  = new Bitmap[10];
		    
		    imageBitmaps = MainActivity.activ.pois.get(id).imageBitmaps.clone();
		             
		    placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		     
		    for(int i=0; i<imageBitmaps.length; i++)
		    	
		        imageBitmaps[i]=placeholder;
		    
		    TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
		     
		    defaultItemBackground = styleAttrs.getResourceId(
		        R.styleable.PicGallery_android_galleryItemBackground, 0);
		     
		    styleAttrs.recycle();
		     
		}

		@Override
		public int getCount() {
		    return imageBitmaps.length;
		}

		@Override
		public Object getItem(int position) {
		    return position;
		}

		@Override
		public long getItemId(int position) {
		    return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 
		    ImageView imageView = new ImageView(galleryContext);
		    imageView.setImageBitmap(imageBitmaps[position]);
		    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    imageView.setBackgroundResource(defaultItemBackground);
		    return imageView;
		}
	}
}
