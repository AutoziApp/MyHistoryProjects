/**  
 * GalleryAdapter.java
 * @version 1.0
 */
package cn.com.mapuni.meshing.base.digitalchina.gallery;

//import com.digitalchina.gallery.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> arraylist;
	private String attch;
//	private int images[] = { R.drawable.image0, R.drawable.image1, R.drawable.image2,
//			R.drawable.image7,R.drawable.image4,R.drawable.image5,R.drawable.image6,
//			R.drawable.image3};
	
//	private String images[] = {"5adab56e-8fc1-48e5-bf87-99de7b091810.jpg",
//			"dc426a63-07d2-404a-bab9-ab5cfef0ec8b.jpg",
//			"48039540-1e4d-412a-8f5d-27b0d739c96b.jpg"};

	public GalleryAdapter(Context context,ArrayList<String> arraylist,String attch) {
		this.context = context;
		this.arraylist = arraylist;
		this.attch = attch;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), images[position]);
		Bitmap bmp = loadmap(attch+"/"+arraylist.get(position));
		ZoomImageView view = new ZoomImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

	public Bitmap loadmap(String fileName){
//		fileName   -->  headers/1.jpg
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mapuni/MobileEnforcement/Attach/"+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return bit;
	}
}
